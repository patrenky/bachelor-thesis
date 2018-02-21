package xmicha65.bp_app.model;

import org.opencv.core.Mat;

import java.io.Serializable;

/**
 * Prepared class for HDR content of merged exposures
 * This class will have methods for saving HDR content into HDR formats
 * @author xmicha65
 */
public class ImageHDR implements Serializable {
    private Mat matHdrImg;
    private double[] E; // radiance map
    private int w;      // width
    private int h;      // height

    public ImageHDR(Mat hdrImage) {
        this.matHdrImg = hdrImage;
    }

    /**
     * GETTERS
     */

    public Mat getMatHdrImg() {
        return this.matHdrImg;
    }
}
