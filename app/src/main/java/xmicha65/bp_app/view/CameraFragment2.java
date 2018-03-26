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
import android.hardware.camera2.TotalCaptureResult;
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
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xmicha65.bp_app.Main;
import xmicha65.bp_app.R;
import xmicha65.bp_app.controller.camera.ImageSaver;
import xmicha65.bp_app.model.ImageLDR;

/**
 * Capture image sequence using Camera2 API
 *
 * @author xmicha65
 *         https://github.com/googlesamples
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraFragment2 extends Fragment {
    private long MICRO_SECOND = 1000;
    private long MILI_SECOND = MICRO_SECOND * 1000;
    private long ONE_SECOND = MILI_SECOND * 1000;

    private TextureView textureView;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions; // only for preview
    private CaptureRequest.Builder captureRequestBuilder; // also
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

    private long exposureMin; // range min from camera characteristics (33 600 ns)
    private long exposureMax; // range max from camera characteristics (356 732 928 ns)
    private long[] exposures = { // range (1/2^14 <-> 1/2^2 ns)
            (long) ((1 / (Math.pow(2, 14))) * ONE_SECOND),
            (long) ((1 / (Math.pow(2, 13))) * ONE_SECOND),
            (long) ((1 / (Math.pow(2, 12))) * ONE_SECOND),
            (long) ((1 / (Math.pow(2, 11))) * ONE_SECOND),
            (long) ((1 / (Math.pow(2, 10))) * ONE_SECOND), // stred svetlej
            (long) ((1 / (Math.pow(2, 9))) * ONE_SECOND),
            (long) ((1 / (Math.pow(2, 8))) * ONE_SECOND), // stred tmavej
            (long) ((1 / (Math.pow(2, 7))) * ONE_SECOND),
            (long) ((1 / (Math.pow(2, 6))) * ONE_SECOND),
            (long) ((1 / (Math.pow(2, 5))) * ONE_SECOND),
            (long) ((1 / (Math.pow(2, 4))) * ONE_SECOND)
    };

    private boolean processImages = true;
    private int numImages = 11;
    private int photoIndex = 0;

    private ImageView iv0;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;

    private List<ImageLDR> capturedImages = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera2, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        view.findViewById(R.id.camera2_save).setOnClickListener(v -> takePicture(false));
        view.findViewById(R.id.camera2_capture).setOnClickListener(v -> takePicture(true));
        view.findViewById(R.id.camera2_back).setOnClickListener(v -> ((Main) getActivity()).goHome());
        textureView = (TextureView) view.findViewById(R.id.camera2_texture);
        textureView.setSurfaceTextureListener(textureListener);
        iv0 = (ImageView) view.findViewById(R.id.camera2_img0);
        iv1 = (ImageView) view.findViewById(R.id.camera2_img1);
        iv2 = (ImageView) view.findViewById(R.id.camera2_img2);
        iv3 = (ImageView) view.findViewById(R.id.camera2_img3);
        iv4 = (ImageView) view.findViewById(R.id.camera2_img4);
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

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

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

            // camera exposure time range
            Range exposureRange = characteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);
            exposureMin = (long) exposureRange.getLower();
            exposureMax = (long) exposureRange.getUpper();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();

            // configure the size of default buffer to be the size of camera preview
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

            // the output Surface we need to start preview
            Surface surface = new Surface(texture);

            // set up a CaptureRequest.Builder with the output Surface
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            // create a CameraCaptureSession for camera preview
            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    // camera is already closed
                    if (cameraDevice == null) {
                        return;
                    }
                    cameraCaptureSessions = cameraCaptureSession;

                    // automatic control mode for preview
                    captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

                    try {
                        // start displaying the camera preview
                        cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
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
    protected void takePicture(boolean process) {
        this.processImages = process;
        Activity activity = getActivity();
        if (activity == null || cameraDevice == null) {
            return;
        }
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        System.out.println("### starting capturing");

        try {
            int width = 640;
            int height = 480;
            int iso = 32;
            float aperture = (float) 1 / 2;

            // configure image dimensions TODO do this before capturing (optimalization)
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes;
            jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            if (jpegSizes != null && jpegSizes.length > 0) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }

            // output instance
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

            // list for capture burst
            List<CaptureRequest> captureBuildersList = new ArrayList<>();

            for (int i = 0; i < numImages; i++) {
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
                captureBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, iso);

                // aperture
                captureBuilder.set(CaptureRequest.LENS_APERTURE, aperture);

                // exposure time
                captureBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, exposures[i]);

                // lock white balance
                captureBuilder.set(CaptureRequest.CONTROL_AWB_LOCK, true);

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

                if (!processImages) {
                    // tmp save images
                    mBackgroundHandler.post(new ImageSaver(image, (double) exposures[photoIndex] / ONE_SECOND));

//                    switch (photoIndex) {
//                        case 0:
//                            ShowImage shI0;
//                            mBackgroundHandler.post(shI0 = new ShowImage(iv0, image));
//                            shI0.display();
//                            break;
//                        case 1:
//                            ShowImage shI1;
//                            mBackgroundHandler.post(shI1 = new ShowImage(iv1, image));
//                            shI1.display();
//                            break;
//                        case 2:
//                            ShowImage shI2;
//                            mBackgroundHandler.post(shI2 = new ShowImage(iv2, image));
//                            shI2.display();
//                            break;
//                        case 3:
//                            ShowImage shI3;
//                            mBackgroundHandler.post(shI3 = new ShowImage(iv3, image));
//                            shI3.display();
//                            break;
//                        case 4:
//                            ShowImage shI4;
//                            mBackgroundHandler.post(shI4 = new ShowImage(iv4, image));
//                            shI4.display();
//                            break;
//                    }
                }

                if (processImages) {
                    capturedImages.add(new ImageLDR(image, (double) exposures[photoIndex] / ONE_SECOND));
                    image.close();
                }
                photoIndex++;
                if (processImages && photoIndex == numImages)
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
