package xmicha65.bp_app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;

import xmicha65.bp_app.Controller.HDR;
import xmicha65.bp_app.Controller.SolveG;
import xmicha65.bp_app.Model.Image;

public class Main extends AppCompatActivity {
    Image[] images;
//    double[] expTimes = {0.125, 0.25, 0.5, 1}; // stLuis
    double[] expTimes = {0.001, 0.0166, 0.25, 8}; // lampicka
    int index = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
//            HDRCV hdrcv = new HDRCV(this.images, this.expTimes);
//            displayCurve(hdrcv.getResponse(2));
//            displayImage(hdrcv.getLdrImage());

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
        ImageView iv = (ImageView) findViewById(R.id.imageView0);
        iv.setImageBitmap(img);
    }

//    /**
//     * Display histogram
//     */
//    public void displayHistogram(int idx) {
//        int[] histogram = this.images[idx].getHistogram();
//        int[] fiftyShades = this.images[idx].getFiftyShades();
//        int magic = 3;
//        int width = 256 * magic;
//        int height = 500;
//        Mat his = new Mat(height, width, CvType.CV_8UC3);
//        Imgproc.line(his, new Point(width / 2, 0), new Point(width / 2, height), new Scalar(255, 255, 255), width);
//
//        for (int i = 0; i < 256; i++) {
//            Point pt1 = new Point(width - i * magic - magic, 0);
//            Point pt2 = new Point(width - i * magic - magic, histogram[i] / 30);
//            Imgproc.line(his, pt1, pt2, new Scalar(18, 131, 223), magic);
//            if (this.images[idx].findIndex(fiftyShades, i) > -1) {
//                Imgproc.circle(his, pt1, magic, new Scalar(0, 0, 0), magic);
//            }
//        }
//
//        Bitmap bm = Bitmap.createBitmap(his.cols(), his.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(his, bm);
//
//        ImageView iv = (ImageView) findViewById(R.id.imageView1);
//        iv.setImageBitmap(bm);
//        iv.setRotation(180);
//    }

//    /**
//     * Display bitmap image with selected values
//     */
//    public void displayImageWithPoints(int idx) {
//        Bitmap bmp32 = this.images[idx].getGrayscaleImg().copy(Bitmap.Config.ARGB_8888, true);
//        Mat matImg = new Mat(bmp32.getHeight(), bmp32.getWidth(), CvType.CV_8UC3);
//        Utils.bitmapToMat(bmp32, matImg);
//
//        int[] fiftyPositions = this.images[idx].getfiftyPositions();
//        int width = matImg.width();
//
//        for (int i = 0; i < fiftyPositions.length; i++) {
//            int row = fiftyPositions[i] / width;
//            int col = fiftyPositions[i] % width;
//            Imgproc.circle(matImg, new Point(row, col), width / 50, new Scalar(255, 255, 255), width / 300);
//        }
//
//        Bitmap bm = Bitmap.createBitmap(matImg.cols(), matImg.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(matImg, bm);
//
//        ImageView iv = (ImageView) findViewById(R.id.imageView0);
//        iv.setImageBitmap(bm);
//    }

    /**
     * Display response curve in graph
     */
    public void displayCurve(double[] array) {
        int height = 400;
        int width = array.length;
        Mat his = new Mat(height, width, CvType.CV_8UC3);
        Imgproc.line(his, new Point(width / 2, 0), new Point(width / 2, height), new Scalar(200, 200, 200), width);

        for (int i = 0; i < width; i++) {
            Point pt1 = new Point(width - i, array[i] * 100 + height / 2); // * 100 + height / 2
            Imgproc.circle(his, pt1, 1, new Scalar(0, 0, 0), 1);
        }

        Bitmap bm = Bitmap.createBitmap(his.cols(), his.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(his, bm);

        ImageView iv = (ImageView) findViewById(R.id.imageView1);
        iv.setImageBitmap(bm);
        iv.setRotation(180);
    }

    /**
     * Display RGB response curves in graph
     */
    public void displayCurves(double[] red, double[] green, double[] blue) {
        int height = 400;
        int width = red.length;
        Mat his = new Mat(height, width, CvType.CV_8UC3);
        Imgproc.line(his, new Point(width / 2, 0), new Point(width / 2, height), new Scalar(200, 200, 200), width);

        for (int i = 0; i < width; i++) {
            Point pt1 = new Point(width - i, red[i] * 100 + height / 2);
            Imgproc.circle(his, pt1, 1, new Scalar(255, 0, 0), 1);
        }

        for (int i = 0; i < width; i++) {
            Point pt1 = new Point(width - i, green[i] * 100 + height / 2);
            Imgproc.circle(his, pt1, 1, new Scalar(0, 255, 0), 1);
        }

        for (int i = 0; i < width; i++) {
            Point pt1 = new Point(width - i, blue[i] * 100 + height / 2);
            Imgproc.circle(his, pt1, 1, new Scalar(0, 0, 255), 1);
        }

        Bitmap bm = Bitmap.createBitmap(his.cols(), his.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(his, bm);

        ImageView iv = (ImageView) findViewById(R.id.imageView1);
        iv.setImageBitmap(bm);
        iv.setRotation(180);
    }
}
