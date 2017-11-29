package xmicha65.bp_app.Controller;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.photo.Photo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xmicha65.bp_app.Model.Image;

/**
 * HDR with only OpenCV methods
 */
public class HDRCV {
    Image[] inImages;
    double[] expTimes;

    public HDRCV(Image[] images, double[] times) {
        this.inImages = images;
        this.expTimes = times;

        fullAlg();
    }

    public void fullAlg() {
        System.out.println("----------------------------- START ------------------------------");

        List<Mat> cvimages = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Bitmap bmp32 = inImages[i].getRgbImg().copy(Bitmap.Config.ARGB_8888, true);
            Mat tmpImg = new Mat(bmp32.getHeight(), bmp32.getWidth(), CvType.CV_8UC3);
            Utils.bitmapToMat(bmp32, tmpImg);
            cvimages.add(tmpImg);
        }

        Mat expTimes = new MatOfDouble(this.expTimes);
        Mat hdrImage = new Mat();
        Mat ldrImage = new Mat();

        Photo.createMergeDebevec().process(cvimages, hdrImage, expTimes);
        Photo.createTonemapReinhard().process(hdrImage, ldrImage);

        System.out.println("img: " + Arrays.toString(cvimages.get(0).get(0, 10)));
        System.out.println("hdr: " + Arrays.toString(hdrImage.get(0, 10)));
        System.out.println("ldr: " + Arrays.toString(ldrImage.get(0, 10)));

//        try {
//            Bitmap bm = Bitmap.createBitmap(hdrImage.cols(), hdrImage.rows(), Bitmap.Config.ARGB_8888);
//            Utils.matToBitmap(ldrImage, bm);
//        } catch (Exception e) {
//            System.out.println("chyba mat to bitmap");
//        }

        System.out.println("------------------------------ END ------------------------------");
    }
}
