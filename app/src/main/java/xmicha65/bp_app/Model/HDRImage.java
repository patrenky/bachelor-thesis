package xmicha65.bp_app.Model;

public class HDRImage {
    private Vector E; // radiance map
    private int w;
    private int h;

    /**
     * Default constructor
     *
     * @param E      the radiance map
     * @param width  the width of the picture
     * @param height the height of the picture
     */
    public HDRImage(Vector E, int width, int height) {
        this.E = E;
        this.w = width;
        this.h = height;
    }

    /**
     * returns radiance map.
     *
     * @return the currently calculated radiance map
     */
    public Vector getE() {
        return E;
    }

    /**
     * Width of the HDRI
     *
     * @return width
     */
    public int getWidth() {
        return w;
    }

    /**
     * Height of the HDRI
     *
     * @return height
     */
    public int getHeight() {
        return h;
    }
}
