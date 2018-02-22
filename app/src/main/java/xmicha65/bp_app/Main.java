package xmicha65.bp_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import xmicha65.bp_app.controller.hdr.HDRController;
import xmicha65.bp_app.model.ImageHDR;
import xmicha65.bp_app.model.ImageLDR;
import xmicha65.bp_app.view.CameraFragment;
import xmicha65.bp_app.view.EditFragment;
import xmicha65.bp_app.view.HomeFragment;

/**
 * Main class of app
 * Init OpenCV library
 * Logic of displaying screens
 *
 * @author xmicha65
 */
public class Main extends AppCompatActivity {
    private boolean useOpenCVforMerge = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Init OpenCV callback
     */
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status != LoaderCallbackInterface.SUCCESS)
                super.onManagerConnected(status);
        }
    };

    /**
     * Test if OpenCV is initialized
     * If OpenCV, display home screen
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

            HomeFragment homeScreen = new HomeFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, homeScreen).commit();
        }
    }

    /**
     * Home fragment handler
     */
    public void homeSelectCapture() {
        CameraFragment cameraScreen = new CameraFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, cameraScreen).commit();
    }

    /**
     * Camera fragment handler
     */
    public void cameraAfterCaptured(List<ImageLDR> capturedImages) {
        HDRController hdrController = new HDRController(capturedImages, useOpenCVforMerge);
        System.out.println("### starting TMO");
//        startToneMap(hdrController.getHdrImage());
    }

    /**
     * Display edit screen, start tone mapping
     */
    private void startToneMap(ImageHDR hdrImage) {

        EditFragment editScreen = new EditFragment();

        // passing hdr image to edit screen
        Bundle args = new Bundle();
        args.putSerializable(EditFragment.ARG_HDR, hdrImage );
        editScreen.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editScreen).commit();
    }

    /**
     * Temporary method for init 4 images from assets
     */
    public void homeSelectinitImages() {
        double[] expTimes = {0.001, 0.0166, 0.25, 8};
        List<ImageLDR> loadedImages = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            try {
                // nacitanie obrazku z assets do inp streamu
                InputStream ins = getAssets().open(String.format("room/lampicka%d.jpg", i));
                loadedImages.add(new ImageLDR(ins, expTimes[i]));
            } catch (IOException ignored) {}
        }

        cameraAfterCaptured(loadedImages);
    }
}