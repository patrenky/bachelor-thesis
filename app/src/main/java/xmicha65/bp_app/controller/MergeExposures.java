package xmicha65.bp_app.controller;

import xmicha65.bp_app.model.Image;

/**
 * Constructing the High Dynamic Range Radiance Map algorithm
 * @author xmicha65
 * source: Debevec, P.; Malik, J.: Recovering High Dynamic Range Radiance Maps from Photographs
 * http://www.pauldebevec.com/Research/HDR/debevec-siggraph97.pdf
 */
public class MergeExposures {
    private double[] gRed;
    private double[] gGreen;
    private double[] gBlue;
    private double[] w;
    private double[] lnT;
    private int numExposures;
    private int numPixels;
    private Image[] images;

    private double[] lnE;
    private double[] lnERed;
    private double[] lnEGreen;
    private double[] lnEBlue;

    public MergeExposures(double[] gRed,
                          double[] gGreen,
                          double[] gBlue,
                          double[] weights,
                          double[] lnT,
                          int exposures,
                          int pixels,
                          Image[] images) {
        this.gRed = gRed;
        this.gGreen = gGreen;
        this.gBlue = gBlue;

        this.w = weights;
        this.lnT = lnT;
        this.numExposures = exposures;
        this.numPixels = pixels;
        this.images = images;

        // compute for each RGB color
        computeRed();
        computeGreen();
        computeBlue();
    }


    private void computeRed() {
        this.lnERed = new double[this.numPixels];
        this.lnERed = createHDR(this.gRed, 0);
    }

    private void computeGreen() {
        this.lnEGreen = new double[this.numPixels];
        this.lnEGreen = createHDR(this.gGreen, 1);
    }

    private void computeBlue() {
        this.lnEBlue = new double[this.numPixels];
        this.lnEBlue = createHDR(this.gBlue, 2);
    }

    private double[] createHDR(double[] g, int color) {
        double[] lnE = new double[this.numPixels];
        double numerator;
        double denominator;

        try {
            for (int i = 0; i < this.numPixels; i++) {
                numerator = 0;
                denominator = 0;
                for (int j = 0; j < this.numExposures; j++) {
                    int value = 0;
                    switch (color) {
                        case 0:
                            value = images[j].getPixelRed(i);
                            break;
                        case 1:
                            value = images[j].getPixelGreen(i);
                            break;
                        case 2:
                            value = images[j].getPixelBlue(i);
                            break;
                        default:
                            break;
                    }
                    numerator += this.w[value] * (g[value] - this.lnT[j]);
                    denominator += this.w[value];
                }
                lnE[i] = numerator / denominator;
            }
        } catch (Exception e) {
            System.out.println("chyba createHDR: " + e.getMessage());
        }

        return lnE;
    }
}
