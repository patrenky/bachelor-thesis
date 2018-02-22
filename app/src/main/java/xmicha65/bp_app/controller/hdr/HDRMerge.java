package xmicha65.bp_app.controller.hdr;

import java.util.List;

import xmicha65.bp_app.model.CameraCRF;
import xmicha65.bp_app.model.Color;
import xmicha65.bp_app.model.ImageHDR;
import xmicha65.bp_app.model.ImageLDR;

/**
 * Constructing the High Dynamic Range Radiance Map algorithm
 *
 * @author xmicha65
 */
public class HDRMerge {
    private double[] w;
    private double[] lnT;
    private int numExposures;
    private int numPixels;
    private List<ImageLDR> images;

    private ImageHDR hdrImage;

    public HDRMerge(CameraCRF cameraCRF, double[] weights, double[] lnT, int numPixels, int numExposures, List<ImageLDR> capturedImages) {
        this.w = weights;
        this.lnT = lnT;
        this.numPixels = numPixels;
        this.numExposures = numExposures;
        this.images = capturedImages;

        // create HDR content for each channel
        double[] lnERed = createHDR(cameraCRF.getCrfRed(), Color.RED);
        double[] lnEGreen = createHDR(cameraCRF.getCrfGreen(), Color.GREEN);
        double[] lnEBlue = createHDR(cameraCRF.getCrfBlue(), Color.BLUE);

        // merge hdr content
        double[] lnE = mergeHDR(lnERed, lnEGreen, lnEBlue);
        // TODO return this.lnE
    }

    /**
     * source: Debevec, P.; Malik, J.: Recovering High Dynamic Range Radiance Maps from Photographs
     * http://www.pauldebevec.com/Research/HDR/debevec-siggraph97.pdf
     */
    private double[] createHDR(double[] crf, Color color) {
        double[] lnE = new double[this.numPixels];
        double numerator;
        double denominator;

        try {
            for (int i = 0; i < this.numPixels; i++) {
                numerator = 0;
                denominator = 0;
                for (int j = 0; j < this.numExposures; j++) {
                    int value = images.get(j).getPixel(i, color);
                    numerator += this.w[value] * (crf[value] - this.lnT[j]);
                    denominator += this.w[value];
                }
                lnE[i] = numerator / denominator;
            }
        } catch (Exception e) {
            System.out.println("### catch createHDR " + e.getMessage());
        }

        return lnE;
    }

    /**
     * Merge HDR content channels into double[]
     */
    private double[] mergeHDR(double[] lnERed, double[] lnEGreen, double[] lnEBlue) {
        return null;
        // TODO double [ pixel ] [ channel ]
    }
}
