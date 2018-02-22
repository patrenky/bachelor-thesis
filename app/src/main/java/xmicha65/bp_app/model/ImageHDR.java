package xmicha65.bp_app.model;

import org.opencv.core.Mat;

import java.io.Serializable;

/**
 * HDR content of merged exposures
 * todos:
 * - saving HDR content into HDR formats
 *
 * @author xmicha65
 */
public class ImageHDR implements Serializable {
    private Mat matHdrImage;
    private double[] hdrImage;

    public ImageHDR(Mat matHdrImage) {
        this.matHdrImage = matHdrImage;
    }

    public ImageHDR(double[] E) {
        this.hdrImage = E;
    }

    /**
     * GETTERS
     */

    public Mat getMatHdrImg() {
        return this.matHdrImage;
    }
}
