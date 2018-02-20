package xmicha65.bp_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.Serializable;
import java.util.List;

import xmicha65.bp_app.controller.hdr.HDRController;
import xmicha65.bp_app.controller.tmo.TMOReinhard;
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

    private CameraFragment cameraScreen;

    /**
     * Home fragment handler
     */
    public void homeSelectCapture() {
        cameraScreen = new CameraFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, cameraScreen).commit();
    }

    private Mat hdrImage;
//    private TMOReinhard tonemapper;

    /**
     * Camera fragment handler
     */
    public void cameraAfterCaptured(List<ImageLDR> capturedImages) {
        HDRController hdrController = new HDRController(capturedImages, true);
        hdrImage = hdrController.getOpencvHDR();
        startToneMap();
    }

    /**
     * Display edit screen, start tone mapping
     */
    private void startToneMap() {
//        tonemapper = new TMOReinhard(hdrImage);

        EditFragment editScreen = new EditFragment();

        // passing hdr image to edit screen
        Bundle args = new Bundle();
        // TODO org.opencv.core.Mat cannot be cast to java.io.Serializable
        args.putSerializable(EditFragment.ARG_HDR, (Serializable) hdrImage);
        editScreen.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editScreen).commit();
    }
}