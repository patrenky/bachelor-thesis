package xmicha65.bp_app.controller;

import xmicha65.bp_app.model.ImageLDR;

/**
 * Select 50 values for RecoverCRF algorithm
 * Methods to be implemented for experiment:
 * - random values (source: shortly mentioned in the book E. Reinhard, HDR Imaging)
 * - regularly arranged pixels (source: own idea)
 * - selecting samples with histogram (source: own idea) (implemented)
 * - matching samples over exposures (source: E. Reinhard, HDR Imaging)
 * @author xmicha65
 */
public class ValueSelector {
    private ImageLDR[] images;
    private int numExposures;
    private int numValues;
    private int[] fiftyPositions;

    private int[][] valuesR;
    private int[][] valuesG;
    private int[][] valuesB;

    public ValueSelector(ImageLDR[] images) {
        this.images = images;
//        this.numExposures = images.length;
//
//        this.fiftyPositions = images[3].getfiftyPositions();
//        this.numValues = this.fiftyPositions.length;

//        initValues();
    }

//    /**
//     * Init values for each RGB color
//     */
//    private void initValues() {
//        this.valuesR = new int[this.numValues][this.numExposures];
//        this.valuesG = new int[this.numValues][this.numExposures];
//        this.valuesB = new int[this.numValues][this.numExposures];
//
//        for (int i = 0; i < this.numValues; i++) {
//            for (int j = 0; j < this.numExposures; j++) {
//                int pixel = this.images[j].getPixel(fiftyPositions[i]);
//                this.valuesR[i][j] = (pixel >> 16) & 0xff;
//                this.valuesG[i][j] = (pixel >> 8) & 0xff;
//                this.valuesB[i][j] = (pixel) & 0xff;
//            }
//        }
//    }
//
//
//    public int[][] getRed() {
//        return this.valuesR;
//    }
//
//    public int[][] getGreen() {
//        return this.valuesG;
//    }
//
//    public int[][] getBlue() {
//        return this.valuesB;
//    }
}
