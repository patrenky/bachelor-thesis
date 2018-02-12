package xmicha65.bp_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import xmicha65.bp_app.Model.ImageLDR;
import xmicha65.bp_app.View.CameraFragment;
import xmicha65.bp_app.View.EditFragment;
import xmicha65.bp_app.View.HomeFragment;

/**
 * Main class of app
 * Displaying fragments logic
 *
 * @author xmicha65
 */
public class Main extends AppCompatActivity {
    /**
     * Activity is created, display home screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            HomeFragment homeScreen = new HomeFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, homeScreen).commit();
        }
        homeSelectCapture();
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