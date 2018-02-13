package xmicha65.bp_app.controller;

import xmicha65.bp_app.model.Image;

/**
 * HDR algorithm main controller
 * @author xmicha65
 */
public class BuildHDR {
    private double lambda = 50;     // smoothness scaling factor
    private double[] weights;       // weighting function
    private double[] lnT;           // log delta t for image j (B(j))

    private Image[] images;         // input images array
    private int numPixels;          // num of pixels (i)
    private int numExposures;       // num of exposures (j)

    // objects
    private RecoverCRF solveRed;
    private RecoverCRF solveGreen;
    private RecoverCRF solveBlue;

    public BuildHDR(Image[] images) {
        this.images = images;
        this.numPixels = images[0].getLength();
        this.numExposures = images.length;

        initWeights();
        initLnT();

        // select samples for algorithm
        ValueSelector valueSelector = new ValueSelector(this.images);
        int[][] ZijRed = valueSelector.getRed();
        int[][] ZijGreen = valueSelector.getGreen();
        int[][] ZijBlue = valueSelector.getBlue();

        // recover CRF for each color channel
        this.solveRed = new RecoverCRF(ZijRed, this.lnT, this.lambda, this.weights);
        this.solveGreen = new RecoverCRF(ZijGreen, this.lnT, this.lambda, this.weights);
        this.solveBlue = new RecoverCRF(ZijBlue, this.lnT, this.lambda, this.weights);

        // merge exposures into HDR
        new MergeExposures(this.solveRed.getG(),
                this.solveGreen.getG(),
                this.solveBlue.getG(),
                this.weights,
                this.lnT,
                this.numExposures,
                this.numPixels,
                this.images);
    }

    /**
     * Weighting function
     * source: Debevec, P.; Malik, J.: Recovering High Dynamic Range Radiance Maps from Photographs
     * http://www.pauldebevec.com/Research/HDR/debevec-siggraph97.pdf
     */
    private double w(int z) {
        int zmin = 0;
        int zmax = 255;
        return z <= (zmin + zmax) / 2 ? (z - zmin) + 1 : (zmax - z) + 1;
        // other possible forms of function (source: internet)
//        double w0 = z <= 127 ? z : 255 - z;
//        double w1 = Math.max((z <= 127) ? z + 1 : 256 - z, 0.0001);
//        double w2 = z <= 127 ? z / 128 : (256 - z) / 128;
    }

    private void initWeights() {
        this.weights = new double[256];
        for (int i = 0; i < this.weights.length; i++) {
            this.weights[i] = w(i);
        }
    }

    private void initLnT() {
        this.lnT = new double[this.numExposures];
        for (int i = 0; i < this.numExposures; i++) {
            this.lnT[i] = Math.log(this.images[i].getExposure());
        }
    }

    /**
     * GETTERS
     */

    public double[] getRedG() {
        return this.solveRed.getG();
    }

    public double[] getGreenG() {
        return this.solveGreen.getG();
    }

    public double[] getBlueG() {
        return this.solveBlue.getG();
    }

    public double[] getLnE() {
        return this.solveRed.getLnE();
    }
}
