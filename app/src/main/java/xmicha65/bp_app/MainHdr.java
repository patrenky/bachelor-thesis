//package xmicha65.bp_app;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.ImageView;
//
//import org.opencv.android.BaseLoaderCallback;
//import org.opencv.android.LoaderCallbackInterface;
//import org.opencv.android.OpenCVLoader;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//import xmicha65.bp_app.controller.BuildHDR;
//import xmicha65.bp_app.model.Image;
//import xmicha65.bp_app.view.Histogram;
//
///**
// * Temporary main class
// * init images from assets, OpenCV library and call BuildHDR algorithm
// * @author xmicha65
// * Source of async OpenCV initialization, methods mLoaderCallback() and onResume() statement:
// * https://github.com/opencv/opencv/blob/master/samples/android/tutorial-1-camerapreview/src/org/opencv/samples/tutorial1/Tutorial1Activity.java
// */
//public class MainHdr extends AppCompatActivity {
//    private Image[] images;
//    private ImageView ivUp;
//    private ImageView ivDown;
//    private double[] expTimes = {0.125, 0.25, 0.5, 1}; // stLuis
////    private double[] expTimes = {0.001, 0.0166, 0.25, 8}; // lampicka
//    int index = 2; // index of showed image on ImageView
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_tmp);
//        this.ivUp = (ImageView) findViewById(R.id.imageView0);
//        this.ivDown = (ImageView) findViewById(R.id.imageView1);
//
//        initImages();
//        displayImage(this.images[this.index].getRgbImg());
//    }
//
//    /**
//     * Init OpenCV callback
//     */
//    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
//        @Override
//        public void onManagerConnected(int status) {
//            if (status != LoaderCallbackInterface.SUCCESS)
//                super.onManagerConnected(status);
//        }
//    };
//
//    /**
//     * Test if OpenCV is initialized
//     */
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!OpenCVLoader.initDebug()) {
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
//        } else {
//            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
//
//            // OpenCV loaded
//            /** OpencvHDR */
////            OpencvHDR opencvHDR = new OpencvHDR(this.images, this.expTimes);
////            Histogram.displayHdrcvCurve(this.ivDown, opencvHDR.getCRF(1));
////            displayImage(opencvHDR.getLdrImage());
//
//            /** BuildHDR */
//            BuildHDR algorithm = new BuildHDR(this.images);
//
//            Histogram.displayCurves(this.ivDown, algorithm.getRedG(), algorithm.getGreenG(), algorithm.getBlueG());
////            Histogram.displayCurve(this.ivDown, algorithm.getRedG());
//        }
//    }
//
//    /**
//     * Temporary method for init 4 images from assets
//     */
//    public void initImages() {
//        this.images = new Image[4];
//        for (int i = 0; i < 4; i++) {
//            try {
//                // nacitanie obrazku z assets do inp streamu
//                InputStream ins = getAssets().open(String.format("stlouis/stlouis%d-sm.jpg", i));
//                this.images[i] = new Image(ins, this.expTimes[i]);
//            } catch (IOException e) {
//                System.out.println("chyba nacitania obrazku z assets");
//            }
//        }
//    }
//
//    /**
//     * Display bitmap image
//     */
//    public void displayImage(Bitmap img) {
//        this.ivUp.setImageBitmap(img);
//    }
//}
