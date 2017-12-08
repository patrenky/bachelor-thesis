package xmicha65.bp_app.Controller;

import xmicha65.bp_app.Model.Image;

/**
 *
 */
public class Merge {
    private int[][] ZijRed;
    private int[][] ZijGreen;
    private int[][] ZijBlue;
    private double[] gRed;
    private double[] gGreen;
    private double[] gBlue;
    private double[] w;
    private double[] lnT;
    private int numExposures;
    private int numValues;
    private int numPixels;
    private Image[] images;

    private double[] lnE;
    private double[] lnERed;
    private double[] lnEGreen;
    private double[] lnEBlue;

    public Merge(int[][] ZijRed,
                 int[][] ZijGreen,
                 int[][] ZijBlue,
                 double[] gRed,
                 double[] gGreen,
                 double[] gBlue,
                 double[] weights,
                 double[] lnT,
                 int exposures,
                 int pixels,
                 Image[] images) {
        this.ZijRed = ZijRed;
        this.ZijGreen = ZijGreen;
        this.ZijBlue = ZijBlue;

        this.gRed = gRed;
        this.gGreen = gGreen;
        this.gBlue = gBlue;

        this.w = weights;
        this.lnT = lnT;
        this.numExposures = exposures;
        this.numValues = ZijRed.length;
        this.numPixels = pixels;
        this.images = images;

        this.lnE = new double[numValues];
        this.lnERed = new double[numValues];
        this.lnEGreen = new double[numValues];
        this.lnEBlue = new double[numValues];

        computeRed();
        computeGreen();
        computeBlue();
    }


    private void computeRed() {
        this.lnERed = debevecLnE(this.ZijRed, this.gRed);
    }

    private void computeGreen() {
        this.lnEGreen = debevecLnE(this.ZijGreen, this.gGreen);
    }

    private void computeBlue() {
        this.lnEBlue = debevecLnE(this.ZijBlue, this.gBlue);
    }

    /**
     * Debevec's constructing HDR radiance map
     */
    private double[] debevecLnE(int[][] Z, double[] g) {
        double[] lnE = new double[numPixels];
        double numerator;
        double denominator;

        try {
            for (int i = 0; i < this.numValues; i++) {
                numerator = 0;
                denominator = 0;
                for (int j = 0; j < this.numExposures; j++) {
                    numerator += this.w[Z[i][j]] * (g[Z[i][j]] - this.lnT[j]);
                    denominator += this.w[Z[i][j]];
                }
                lnE[i] = numerator / denominator;
            }
        } catch (Exception e) {
            System.out.println("chyba debevecLnE: " + e.getMessage());
        }

        return lnE;
    }

//    private void createLnE() {
//
//    }
}
