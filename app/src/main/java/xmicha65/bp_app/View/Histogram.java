package xmicha65.bp_app.View;

import android.graphics.Bitmap;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Temporary class for displaying histograms and CRF curves on ImageView
 * Uses OpenCV {@link Mat} for drawing
 * @author xmicha65
 */
public class Histogram {

    /**
     * Display RGB response curves on ImageView
     * (ImageView) findViewById(R.id.imageView1)
     */
    public static void displayCurves(ImageView iv, double[] red, double[] green, double[] blue) {
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

        iv.setImageBitmap(bm);
        iv.setRotation(180);
    }

    /**
     * Display response curve on ImageView
     */
    public static void displayCurve(ImageView iv, double[] array) {
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

        iv.setImageBitmap(bm);
        iv.setRotation(180);
    }

    /**
     * Display CRF from OpenCV
     */
    public static void displayHdrcvCurve(ImageView iv, double[] array) {
        int height = 300;
        int width = array.length;
        Mat his = new Mat(height, width, CvType.CV_8UC3);
        Imgproc.line(his, new Point(width / 2, 0), new Point(width / 2, height), new Scalar(200, 200, 200), width);

        for (int i = 0; i < width; i++) {
            Point pt1 = new Point(width - i, array[i] * 100); // * 100 + height / 2
            Imgproc.circle(his, pt1, 1, new Scalar(0, 0, 0), 1);
        }

        Bitmap bm = Bitmap.createBitmap(his.cols(), his.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(his, bm);

        iv.setImageBitmap(bm);
        iv.setRotation(180);
    }

//    /**
//     * Display histogram on ImageView
//     */
//    public void displayHistogram(ImageView iv, Image image) {
//        int[] histogram = image.getHistogram();
//        int[] fiftyShades = image.getFiftyShades();
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
//            if (image.findIndex(fiftyShades, i) > -1) {
//                Imgproc.circle(his, pt1, magic, new Scalar(0, 0, 0), magic);
//            }
//        }
//
//        Bitmap bm = Bitmap.createBitmap(his.cols(), his.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(his, bm);
//
//        iv.setImageBitmap(bm);
//        iv.setRotation(180);
//    }

//    /**
//     * Display bitmap image with selected values
//     */
//    public void displayImageWithPoints(ImageView iv, int idx) {
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
//        iv.setImageBitmap(bm);
//    }
}
