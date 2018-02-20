package xmicha65.bp_app.controller.hdr;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.util.ArrayList;
import java.util.List;

import xmicha65.bp_app.model.CameraCRF;
import xmicha65.bp_app.model.ImageHDR;
import xmicha65.bp_app.model.ImageLDR;

/**
 * HDR algorithm main controller
 *
 * @author xmicha65
 */
public class HDRController {
    private double lambda = 50;     // smoothness scaling factor
    private double[] weights;       // weighting function
    private double[] lnT;           // log delta t for image j (B(j))

    private List<ImageLDR> capturedImages;
    private int numPixels;          // num of pixels (i)
    private int numExposures;       // num of exposures (j)

    private CameraCRF responseCurves;
    private ImageHDR hdrImage;

    private Mat cvHdrImage; // openCV

    public HDRController(List<ImageLDR> captImages, boolean opencv) {
        float[] expTimes;
        Mat response = new Mat();
        int numImages = captImages.size();

        // init float exposure times
        expTimes = new float[numImages];
        for (int i = 0; i < numImages; i++) {
            expTimes[i] = (float) captImages.get(i).getExposureTime();
        }

        // init List of image Mat
        List<Mat> cvimages = new ArrayList<>();
        for (int i = 0; i < numImages; i++) {
            cvimages.add(captImages.get(i).bitmapToMat());
        }

        Mat mExpTimes = new MatOfFloat(expTimes);
        cvHdrImage = new Mat();

        Photo.createCalibrateDebevec().process(cvimages, response, mExpTimes);
        Photo.createMergeDebevec().process(cvimages, cvHdrImage, mExpTimes, response);
    }

    public Mat getOpencvHDR() {
        return this.cvHdrImage;
    }

    public HDRController(List<ImageLDR> captImages) {

    }
}
