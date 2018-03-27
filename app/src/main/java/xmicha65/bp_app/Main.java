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
import xmicha65.bp_app.view.CameraFragment2;
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
    private boolean useOpenCVforMerge = true;

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

            goHome();
        }
    }

    /**
     * Home fragment handler
     */
    public void homeSelectCapture() {
        CameraFragment2 cameraScreen = new CameraFragment2();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, cameraScreen).commit();
    }

    /**
     * Start HDR process pipeline
     */
    public void processImages(List<ImageLDR> capturedImages) {
        // post process image variables
        capturedImages.forEach(ImageLDR::postProcess);

        System.out.println("### starting merging");
        HDRController hdrController = new HDRController(capturedImages, useOpenCVforMerge);

        System.out.println("### starting TMO");
        startToneMap(hdrController.getHdrImage());
    }

    /**
     * Display edit screen, start tone mapping
     */
    public void startToneMap(ImageHDR hdrImage) {
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
     * Open list of .hdr files
     */
    public void homeSelectLoadHdr() {
        FilesFragment filesScreen = new FilesFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, filesScreen).commit();
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
     * Temporary methods for init images from assets
     */
    public void homeSelectinitLampicka() {
        double[] expTimes = {0.001, 0.0166, 0.25, 8};
        List<ImageLDR> loadedImages = new ArrayList<>();

        for (int i = 0; i < expTimes.length; i++) {
            try {
                // nacitanie obrazku z assets do inp streamu
                InputStream ins = getAssets().open(String.format("room/lampicka%d.jpg", i));
                loadedImages.add(new ImageLDR(ins, expTimes[i]));
            } catch (IOException ignored) {
            }
        }

        processImages(loadedImages);
    }

    public void homeSelectinitScene(String name) {
        double[] exposures = {
                0.000061035000,
                0.000122070000,
                0.000244140000,
                0.000488281000,
                0.000976562000,
                0.001953125000,
                0.003906250000,
                0.007812500000,
                0.015625000000,
                0.031250000000,
                0.062500000000
        };
        List<ImageLDR> loadedImages = new ArrayList<>();

        for (int i = 0; i < exposures.length; i++) {
            try {
                InputStream ins = getAssets().open(String.format("%s/%.12f.jpg", name, exposures[i]));
                loadedImages.add(new ImageLDR(ins, exposures[i]));
            } catch (IOException ignored) {
            }
        }

        processImages(loadedImages);
    }
}