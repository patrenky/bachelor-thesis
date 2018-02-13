package xmicha65.bp_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.List;

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
        // make HDR format & post to TMO
    }

    /**
     * Display edit screen, start tone mapping
     */
    private void startToneMap(double[] hdr) {
        EditFragment editScreen = new EditFragment();

        // passing hdr format into edit screen
        Bundle args = new Bundle();
        args.putDoubleArray(EditFragment.ARG_HDR, hdr);
        editScreen.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editScreen).commit();
    }
}