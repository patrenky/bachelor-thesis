package xmicha65.bp_app.controller.hdr;

import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
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

    public HDRController(List<ImageLDR> captImages, boolean opencv) {
        this.capturedImages = captImages;

        if (opencv) opencvHDR();
    }

    private void opencvHDR() {
        Mat cvHdrImage = new Mat();
        Mat response = new Mat();

        int numImages = capturedImages.size();

        // init float exposure times
        float[] expTimes = new float[numImages];
        for (int i = 0; i < numImages; i++) {
            expTimes[i] = (float) capturedImages.get(i).getExposureTime();
        }
        Mat mExpTimes = new MatOfFloat(expTimes);

        // init List of image Mat
        List<Mat> cvimages = new ArrayList<>();
        for (int i = 0; i < numImages; i++) {
            cvimages.add(capturedImages.get(i).getMatImg());
        }

        Photo.createCalibrateDebevec().process(cvimages, response, mExpTimes);
        Photo.createMergeDebevec().process(cvimages, cvHdrImage, mExpTimes, response);

        this.hdrImage = new ImageHDR(cvHdrImage);
    }

    public ImageHDR getHdrImage() {
        return this.hdrImage;
    }
}
