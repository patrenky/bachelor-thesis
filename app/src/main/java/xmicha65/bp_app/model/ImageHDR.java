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

    public ImageHDR(Mat matHdrImage) {
        this.matHdrImage = matHdrImage;
    }

    public void save(String filename) {
        System.out.println("### filename: " + filename);
    }

    /**
     * GETTERS
     */

    public Mat getMatHdrImg() {
        return this.matHdrImage;
    }
}
