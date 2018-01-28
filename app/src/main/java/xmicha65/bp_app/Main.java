package xmicha65.bp_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import xmicha65.bp_app.View.CameraFragment;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_camera);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new CameraFragment())
                    .commit();
        }
    }
}