package xmicha65.bp_app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.io.InputStream;

//import xmicha65.bp_app.Controller.HDR;
import xmicha65.bp_app.Controller.HDRCV;
import xmicha65.bp_app.Model.Image;
import xmicha65.bp_app.View.Histogram;

public class _MainHdr extends AppCompatActivity {
    private Image[] images;
    private ImageView ivUp;
    private ImageView ivDown;
    //   private double[] expTimes = {0.125, 0.25, 0.5, 1}; // stLuis
    private double[] expTimes = {0.001, 0.0166, 0.25, 8}; // lampicka
    int index = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.ivUp = (ImageView) findViewById(R.id.imageView0);
        this.ivDown = (ImageView) findViewById(R.id.imageView1);

        initImages();
        displayImage(this.images[this.index].getRgbImg());
    }

    /**
     * Init OpenCV before using
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
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

            // OpenCV loaded
            /** HDRCV */
            HDRCV hdrcv = new HDRCV(this.images, this.expTimes);
            Histogram.displayHdrcvCurve(this.ivDown, hdrcv.getResponse(1));
            displayImage(hdrcv.getLdrImage());

            /** SolveG */
//            HDR algorithm = new HDR(this.images);

//            displayCurves(algorithm.getRedG(), algorithm.getGreenG(), algorithm.getBlueG());
//            displayCurve(algorithm.getRedG());
//            displayCurve(algorithm.getLnE());

            /** 50 shades */
            // displayHistogram(this.index);
            // displayImageWithPoints(this.index);
        }
    }

    /**
     * TMP init 4 images from assets
     */
    public void initImages() {
        this.images = new Image[4];
        for (int i = 0; i < 4; i++) {
            try {
                // nacitanie obrazku z assets do inp streamu
                InputStream ins = getAssets().open(String.format("room/lampicka%d.jpg", i));
                this.images[i] = new Image(ins, this.expTimes[i]);
            } catch (IOException e) {
                System.out.println("chyba nacitania obrazku z assets");
            }
        }
    }

    /**
     * Display bitmap image
     */
    public void displayImage(Bitmap img) {
        this.ivUp.setImageBitmap(img);
    }
}
