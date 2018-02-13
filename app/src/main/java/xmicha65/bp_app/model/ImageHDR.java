package xmicha65.bp_app.model;

/**
 * Prepared class for HDR content of merged exposures
 * This class will have methods for saving HDR content into HDR formats
 * @author xmicha65
 */
public class ImageHDR {
    private double[] E; // radiance map
    private int w;      // width
    private int h;      // height

    public ImageHDR(double[] E, int width, int height) {
        this.E = E;
        this.w = width;
        this.h = height;
    }

    /**
     * GETTERS
     */

    public double[] getE() {
        return E;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }
}
