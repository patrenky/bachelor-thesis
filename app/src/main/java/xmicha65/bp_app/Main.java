package xmicha65.bp_app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.List;

import xmicha65.bp_app.controller.hdr.HDRController;
import xmicha65.bp_app.model.ImageHDR;
import xmicha65.bp_app.model.ImageLDR;
import xmicha65.bp_app.view.CameraFragment;
import xmicha65.bp_app.view.EditDragoFragment;
import xmicha65.bp_app.view.EditDurandFragment;
import xmicha65.bp_app.view.EditMantiukFragment;
import xmicha65.bp_app.view.EditReinhardFragment;
import xmicha65.bp_app.view.FilesFragment;
import xmicha65.bp_app.view.HomeFragment;
import xmicha65.bp_app.view.TmoFragment;

/**
 * Main class of app
 * Init OpenCV library
 * Logic of displaying screens
 *
 * @author xmicha65
 */
public class Main extends AppCompatActivity {
    private static AppCompatActivity instance;
    private boolean useOpenCVforMerge = true;
    private int captureExposures = 5;
    private int captureStep = 2;
    private int viewRotation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
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

            goHome();
        }
    }

    /**
     * Show/hide global progress bar
     */
    public void showProgress() {
        findViewById(R.id.main_curtain).setVisibility(View.VISIBLE);
        findViewById(R.id.main_loading).setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        findViewById(R.id.main_curtain).setVisibility(View.GONE);
        findViewById(R.id.main_loading).setVisibility(View.GONE);
    }

    /**
     * Display Home screen
     */
    public void goHome() {
        HomeFragment homeScreen = new HomeFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, homeScreen).commit();
    }

    /**
     * Open camera
     */
    public void homeSelectCapture() {
        viewRotation = 0;
        CameraFragment cameraScreen = new CameraFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, cameraScreen).commit();
    }

    /**
     * Open list of .hdr files
     */
    public void homeSelectLoadHdr() {
        viewRotation = 0;
        FilesFragment filesScreen = new FilesFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, filesScreen).commit();
    }

    /**
     * Start generating HDR content
     */
    public void processImages(List<ImageLDR> capturedImages) {
        // post process image variables
        capturedImages.forEach(ImageLDR::postProcess);

        System.out.println("### starting merging");
        HDRController hdrController = new HDRController(capturedImages, useOpenCVforMerge);

        System.out.println("### starting TMO");
        tonemapOperators(hdrController.getHdrImage());
    }

    /**
     * Display TMOs screen
     */
    public void tonemapOperators(ImageHDR hdrImage) {
        TmoFragment tmoScreen = new TmoFragment();

        // passing hdr image to screen
        Bundle args = new Bundle();
        args.putSerializable(TmoFragment.ARG_HDR, hdrImage);
        tmoScreen.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, tmoScreen).commit();
    }

    /**
     * Display Drago controls
     */
    public void tonemapDrago(ImageHDR hdrImage) {
        EditDragoFragment editScreen = new EditDragoFragment();

        // passing hdr image to screen
        Bundle args = new Bundle();
        args.putSerializable(EditDragoFragment.ARG_HDR, hdrImage);
        editScreen.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editScreen).commit();
    }

    /**
     * Display Durand controls
     */
    public void tonemapDurand(ImageHDR hdrImage) {
        EditDurandFragment editScreen = new EditDurandFragment();

        // passing hdr image to screen
        Bundle args = new Bundle();
        args.putSerializable(EditDurandFragment.ARG_HDR, hdrImage);
        editScreen.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editScreen).commit();
    }

    /**
     * Display Reinhard controls
     */
    public void tonemapReinhard(ImageHDR hdrImage) {
        EditReinhardFragment editScreen = new EditReinhardFragment();

        // passing hdr image to screen
        Bundle args = new Bundle();
        args.putSerializable(EditReinhardFragment.ARG_HDR, hdrImage);
        editScreen.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editScreen).commit();
    }

    /**
     * Display Mantiuk controls
     */
    public void tonemapMantiuk(ImageHDR hdrImage) {
        EditMantiukFragment editScreen = new EditMantiukFragment();

        // passing hdr image to screen
        Bundle args = new Bundle();
        args.putSerializable(EditMantiukFragment.ARG_HDR, hdrImage);
        editScreen.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editScreen).commit();
    }

    /**
     * Settings setters
     */
    public void setCaptureExposures(int num) {
        captureExposures = num;
    }

    public void setCaptureStep(int num) {
        captureStep = num;
    }

    public void setViewRotation(int rotation) {
        viewRotation = rotation;
    }

    /**
     * Settings getters
     */
    public int getCaptureExposures() {
        return captureExposures;
    }

    public int getCaptureStep() {
        return captureStep;
    }

    public int getViewRotation() {
        return viewRotation;
    }
}