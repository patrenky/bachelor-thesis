package xmicha65.bp_app.controller.hdr;

import java.util.List;

import xmicha65.bp_app.model.ImageLDR;

/**
 * Select 50 values for RecoverCRF algorithm
 * Methods to be implemented for experiment:
 * - random values (source: shortly mentioned in the book E. Reinhard, HDR Imaging)
 * - regularly arranged pixels (source: own idea)
 * - selecting samples with histogram (source: own idea) (implemented)
 * - matching samples over exposures (source: E. Reinhard, HDR Imaging)
 *
 * @author xmicha65
 */
public class SamplesSelector {
    private List<ImageLDR> images;
    private int numSamples = 50; // TODO cislo urcit podla vzorca debevec-siggraph
    private int numPixels;
    private int numExposures;

    private int[][] samplesRed;
    private int[][] samplesGreen;
    private int[][] samplesBlue;

    public SamplesSelector(List<ImageLDR> imgs, int pixels, int exposures) {
        this.images = imgs;
        this.numPixels = pixels;
        this.numExposures = exposures;

        this.samplesRed = new int[numSamples][numExposures];
        this.samplesGreen = new int[numSamples][numExposures];
        this.samplesBlue = new int[numSamples][numExposures];

        initRandomSamples();
    }

    /**
     * Init samples on random position for each RGB channel
     */
    public void initRandomSamples() {
        for (int i = 0; i < numSamples; i++) {
            // random pixel index
            int idx = (int) Math.floor(Math.random() * numPixels);
            for (int j = 0; j < numExposures; j++) {
                // get pixel on idx
                int pixel = images.get(j).getPixel(idx);
                // get pixel channels
                samplesRed[i][j] = (pixel >> 16) & 0xff;
                samplesGreen[i][j] = (pixel >> 8) & 0xff;
                samplesBlue[i][j] = (pixel) & 0xff;
            }
        }
    }

    /**
     * GETTERS
     */

    public int getNumExposures() {
        return numExposures;
    }

    public int getNumSamples() {
        return numSamples;
    }

    public int[][] getSamplesRed () {
        return samplesRed;
    }

    public int[][] getSamplesGreen () {
        return samplesGreen;
    }

    public int[][] getSamplesBlue () {
        return samplesBlue;
    }
}
