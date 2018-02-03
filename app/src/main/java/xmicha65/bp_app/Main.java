package xmicha65.bp_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import xmicha65.bp_app.View.CameraFragment;

/**
 * Temporary Main for working with Camera2 API
 * @author https://github.com/googlesamples
 */
public class Main extends AppCompatActivity {
    CameraFragment camera = new CameraFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_main);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, camera)
                    .commit();
        }
    }
}