package xmicha65.bp_app.Model;

public class HDRImage {
    private double[] E; // radiance map
    private int w;
    private int h;

    public HDRImage(double[] E, int width, int height) {
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
