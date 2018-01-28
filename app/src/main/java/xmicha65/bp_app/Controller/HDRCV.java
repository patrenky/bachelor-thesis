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
 * HDR with only OpenCV methods
 */
public class HDRCV {
    private Image[] inImages;
    private float[] expTimes;

    private Mat response = new Mat();
    private Mat ldrImage = new Mat();

    public HDRCV(Image[] images, double[] times) {
        this.inImages = images;
        this.expTimes = new float[times.length];
        for (int i = 0; i < times.length; i++) {
            this.expTimes[i] = (float) times[i];
        }

        System.out.println("----------------------------- START ------------------------------");

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
            Photo.createCalibrateDebevec().process(cvimages, this.response, mExpTimes);
            Photo.createMergeDebevec().process(cvimages, hdrImage, mExpTimes, this.response);
//            Photo.createCalibrateRobertson().process(cvimages, this.response, mExpTimes);
//            Photo.createMergeRobertson().process(cvimages, hdrImage, mExpTimes, this.response);

            float gamma = 2.2f;
            float intensity = 0.0f; // [-8, 8]
            float light_adapt = 0.0f; // [0, 1]
            float color_adapt = 0.0f; // [0, 1]

            Photo.createTonemapReinhard(gamma, intensity, light_adapt, color_adapt).process(hdrImage, this.ldrImage);
//            Photo.createTonemapReinhard().process(hdrImage, this.ldrImage);
//            Photo.createTonemapDrago().process(hdrImage, this.ldrImage);
//            Photo.createTonemapDurand().process(hdrImage, this.ldrImage);
//            Photo.createTonemapMantiuk().process(hdrImage, this.ldrImage);
        } catch (Exception e) {
            System.out.println("chyba opencv " + e.getMessage());
        }

        System.out.println("img: " + Arrays.toString(cvimages.get(0).get(0, 10)));
        System.out.println("res: " + Arrays.toString(this.response.get(10, 0)));
        System.out.println("hdr: " + Arrays.toString(hdrImage.get(0, 10)));
        System.out.println("ldr: " + Arrays.toString(this.ldrImage.get(0, 10)));

        System.out.println("------------------------------ END ------------------------------");
    }

    public double[] getResponse(int color) {
        double[] res = new double[this.response.rows()];
        for (int i = 0; i < this.response.rows(); i++) {
            res[i] = this.response.get(i, 0)[color];
        }
        return res;
    }

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
