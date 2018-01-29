package xmicha65.bp_app.Controller;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xmicha65.bp_app.Model.Image;

/**
 * Creating HDR with OpenCV methods
 * source: OpenCV tutorial
 * https://docs.opencv.org/3.2.0/d3/db7/tutorial_hdr_imaging.html
 */
public class OpencvHDR {
    private Image[] inImages;
    private float[] expTimes;

    private Mat response = new Mat(); // CRF curve
    private Mat ldrImage = new Mat(); // result of tonemapping

    public OpencvHDR(Image[] images, double[] times) {
        this.inImages = images;

        // init float exposure times
        this.expTimes = new float[times.length];
        for (int i = 0; i < times.length; i++) {
            this.expTimes[i] = (float) times[i];
        }

        // init List of image Mat
        List<Mat> cvimages = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Bitmap bmp32 = inImages[i].getRgbImg().copy(Bitmap.Config.ARGB_8888, true);
            Mat tmpBGRA = new Mat(bmp32.getHeight(), bmp32.getWidth(), CvType.CV_8UC4);
            Mat tmpBGR = new Mat();
            Utils.bitmapToMat(bmp32, tmpBGRA);
            Imgproc.cvtColor(tmpBGRA, tmpBGR, Imgproc.COLOR_BGRA2BGR);
            cvimages.add(tmpBGR);
        }

        Mat mExpTimes = new MatOfFloat(this.expTimes);
        Mat hdrImage = new Mat();

        try {
            // merge HDR methods
            Photo.createCalibrateDebevec().process(cvimages, this.response, mExpTimes);
            Photo.createMergeDebevec().process(cvimages, hdrImage, mExpTimes, this.response);
//            Photo.createCalibrateRobertson().process(cvimages, this.response, mExpTimes);
//            Photo.createMergeRobertson().process(cvimages, hdrImage, mExpTimes, this.response);

            // tonemap Reinhard params
            float gamma = 2.2f; // for most displays
            float intensity = 0.0f; // range [-8, 8]
            float light_adapt = 0.0f; // range [0, 1]
            float color_adapt = 0.0f; // range [0, 1]

            // tonemapping methods
            Photo.createTonemapReinhard(gamma, intensity, light_adapt, color_adapt).process(hdrImage, this.ldrImage);
//            Photo.createTonemapReinhard().process(hdrImage, this.ldrImage);
//            Photo.createTonemapDrago().process(hdrImage, this.ldrImage);
//            Photo.createTonemapDurand().process(hdrImage, this.ldrImage);
//            Photo.createTonemapMantiuk().process(hdrImage, this.ldrImage);
        } catch (Exception e) {
            System.out.println("chyba opencv " + e.getMessage());
        }
    }

    /**
     * Get CRF from createMergeDebevec
     */
    public double[] getCRF(int color) {
        double[] res = new double[this.response.rows()];
        for (int i = 0; i < this.response.rows(); i++) {
            res[i] = this.response.get(i, 0)[color];
        }
        return res;
    }

    /**
     * Get result image of HDR algorithm
     */
    public Bitmap getLdrImage() {
        try {
            Mat tmpRGBA = new Mat();
            this.ldrImage.convertTo(tmpRGBA, CvType.CV_8UC4, 255);
            Bitmap bm = Bitmap.createBitmap(tmpRGBA.cols(), tmpRGBA.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tmpRGBA, bm);
            return bm;
        } catch (Exception e) {
            System.out.println("chyba mat to bitmap " + e.getMessage());
        }
        return null;
    }
}
