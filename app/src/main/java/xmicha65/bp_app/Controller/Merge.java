package xmicha65.bp_app.Controller;

import xmicha65.bp_app.Model.Image;

/**
 *
 */
public class Merge {
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

    public Merge(double[] gRed,
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

        computeRed();
        computeGreen();
        computeBlue();
    }


    private void computeRed() {
        this.lnERed = new double[this.numPixels];
        this.lnERed = debevecLnE(this.gRed, 0);
    }

    private void computeGreen() {
        this.lnEGreen = new double[this.numPixels];
        this.lnEGreen = debevecLnE(this.gGreen, 1);
    }

    private void computeBlue() {
        this.lnEBlue = new double[this.numPixels];
        this.lnEBlue = debevecLnE(this.gBlue, 2);
    }

    private double[] debevecLnE(double[] g, int color) {
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
            System.out.println("chyba debevecLnE: " + e.getMessage());
        }

        return lnE;
    }
}
