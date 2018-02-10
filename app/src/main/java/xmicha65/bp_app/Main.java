package xmicha65.bp_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
    }

    public void homeSelectCapture() {
        CameraFragment cameraScreen = new CameraFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, cameraScreen).commit();
    }

    public void cameraAfterCaptured(byte[] capturedImage0) {
        EditFragment editScreen = new EditFragment();

        // passing captured images into edit screen
        Bundle args = new Bundle();
        args.putByteArray(EditFragment.ARG_IMAGE0, capturedImage0);
        editScreen.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editScreen).commit();
    }
}