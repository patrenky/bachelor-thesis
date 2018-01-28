package xmicha65.bp_app.Controller;

import xmicha65.bp_app.Model.Image;

/**
 * Select 50 values for SolveG algorithm
 */
public class ValueSelector {
    private Image[] images;
    private int numExposures;
    private int numValues;
    private int[] fiftyPositions;

    private int[][] valuesR;
    private int[][] valuesG;
    private int[][] valuesB;

    public ValueSelector(Image[] images) {
        this.images = images;
        this.numExposures = images.length;

        int middleOne = Math.round(images.length / 2);
//        this.fiftyPositions = images[middleOne].getfiftyPositions(); // best idx 3
//        this.numValues = this.fiftyPositions.length;

        initValues();
    }

    private void initValues() {
        this.valuesR = new int[this.numValues][this.numExposures];
        this.valuesG = new int[this.numValues][this.numExposures];
        this.valuesB = new int[this.numValues][this.numExposures];

        for (int i = 0; i < this.numValues; i++) {
            for (int j = 0; j < this.numExposures; j++) {
                int pixel = this.images[j].getPixel(fiftyPositions[i]);
                this.valuesR[i][j] = (pixel >> 16) & 0xff;
                this.valuesG[i][j] = (pixel >> 8) & 0xff;
                this.valuesB[i][j] = (pixel) & 0xff;
            }
        }
    }


    public int[][] getRed() {
        return this.valuesR;
    }

    public int[][] getGreen() {
        return this.valuesG;
    }

    public int[][] getBlue() {
        return this.valuesB;
    }
}
