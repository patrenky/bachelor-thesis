package xmicha65.bp_app.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.RggbChannelVector;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Range;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xmicha65.bp_app.Main;
import xmicha65.bp_app.R;
import xmicha65.bp_app.model.Exposures;
import xmicha65.bp_app.model.ImageLDR;

/**
 * Capture image sequence using Camera2 API
 *
 * @author xmicha65
 *         https://github.com/googlesamples
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraFragment extends Fragment {
    private long MICRO_SECOND = 1000;
    private long MILI_SECOND = MICRO_SECOND * 1000;
    private long ONE_SECOND = MILI_SECOND * 1000;

    private TextureView textureView;
    private CameraDevice cameraDevice;
    private Size previewSize;
    private ImageReader imageReader;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private final int REQUEST_CAMERA_PERMISSION = 200;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private Exposures exposures;

    private int autoWidth = 640;
    private int autoHeight = 480;
    private int autoIso = 100;
    private float autoAperture = (float) 1 / 2;
    private long autoExposure;
    private RggbChannelVector autoColorCorrection;

    private List<ImageLDR> capturedImages = new ArrayList<>();
    private int photoIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        textureView = (TextureView) view.findViewById(R.id.camera_texture);
        textureView.setSurfaceTextureListener(textureListener);
        view.findViewById(R.id.camera_capture).setOnClickListener(v -> takePicture());
        view.findViewById(R.id.camera_back).setOnClickListener(v -> ((Main) getActivity()).goHome());
    }

    @Override
    public void onResume() {
        super.onResume();

        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    /**
     * Starts a background thread and its Handler
     */
    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    /**
     * Stops the background thread and its Handler
     */
    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * SurfaceTextureListener handles several lifecycle events on a TextureView
     */
    private final TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    /**
     * StateCallback is called when CameraDevice changes its state
     */
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            // camera is opened, start camera preview
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            closeCamera();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            closeCamera();
        }
    };

    /**
     * Opens the camera specified by CameraFragment
     * Configure preview and image dimensions and exposure range
     */
    private void openCamera() {
        // Check if permissions granted, request permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        }

        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            // camera characteristics
            String cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

            // configure preview dimensions
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            previewSize = map.getOutputSizes(SurfaceTexture.class)[0];
            manager.openCamera(cameraId, stateCallback, mBackgroundHandler);

            // configure image dimensions
            Size[] jpegSizes;
            jpegSizes = map.getOutputSizes(ImageFormat.JPEG);
            if (jpegSizes != null && jpegSizes.length > 0) {
                autoWidth = jpegSizes[0].getWidth();
                autoHeight = jpegSizes[0].getHeight();
            }

            // init camera exposure time range
            Range exposureRange = characteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);
            long exposureMin = (long) exposureRange.getLower();
            long exposureMax = (long) exposureRange.getUpper();

            // init exposure range
            exposures = new Exposures(exposureMin, exposureMax);
            // set autoExposure to range middle value
            autoExposure = exposures.getMiddleValue();

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new CameraCaptureSession for camera preview
     * Saves automatic preview configuration into variables
     */
    protected void createCameraPreview() {
        System.out.println("### starting preview");
        CaptureRequest.Builder captureRequestBuilder;
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();

            // configure the size of default buffer to be the size of camera preview
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

            // the output Surface we need to start preview
            Surface surface = new Surface(texture);

            // set up a CaptureRequest.Builder with the output Surface
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            // on capture preview completed, catch metadata
            CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);

                    autoExposure = result.get(CaptureResult.SENSOR_EXPOSURE_TIME);
                    autoIso = result.get(CaptureResult.SENSOR_SENSITIVITY);
                    autoAperture = result.get(CaptureResult.LENS_APERTURE);
                    autoColorCorrection = result.get(CaptureResult.COLOR_CORRECTION_GAINS);
                }
            };

            // create a CameraCaptureSession for camera preview
            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    // camera is already closed
                    if (cameraDevice == null) {
                        return;
                    }

                    // automatic control mode for preview
                    captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

                    try {
                        // start displaying the camera preview
                        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(getContext(), "Preview configuration failed", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Capture sequence of images with specific settings
     */
    protected void takePicture() {
        Activity activity = getActivity();
        if (activity == null || cameraDevice == null) {
            return;
        }
        System.out.println("### starting capturing");

        // init range of exposures with autoExposure as middle value
        exposures.initExposures(autoExposure);

        try {
            // output instance
            ImageReader reader = ImageReader.newInstance(autoWidth, autoHeight, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

            // list for capture burst
            List<CaptureRequest> captureBuildersList = new ArrayList<>();

            for (int i = 0; i < exposures.getSize(); i++) {
                // init capture builder
                CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                captureBuilder.addTarget(reader.getSurface());

                // orientation
                int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

                // control modes off
                captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_OFF);
                captureBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_OFF);

                // sensor ISO
                captureBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, autoIso);

                // aperture
                captureBuilder.set(CaptureRequest.LENS_APERTURE, autoAperture);

                // exposure time
                captureBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, exposures.getValue(i));

                // color correction
                captureBuilder.set(CaptureRequest.COLOR_CORRECTION_GAINS, autoColorCorrection);

                // push settings
                captureBuildersList.add(captureBuilder.build());
            }

            // on capture complete return to preview mode
            CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                }
            };

            // create session for capture burst
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.captureBurst(captureBuildersList, captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                }
            }, mBackgroundHandler);

            // captured image is ready to process
            ImageReader.OnImageAvailableListener readerListener = (readerResult) -> {
                Image image = readerResult.acquireNextImage();

                capturedImages.add(new ImageLDR(image, (double) exposures.getValue(photoIndex) / ONE_SECOND));
                image.close();
                photoIndex++;

                if (photoIndex == exposures.getSize())
                    ((Main) getActivity()).processImages(capturedImages);
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * If result of permission request is denied, close camera, go to home screen
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                closeCamera();
            }
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

}
