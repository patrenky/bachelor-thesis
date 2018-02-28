package xmicha65.bp_app.model;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.Serializable;

import xmicha65.bp_app.controller.Storages;

/**
 * HDR content of merged exposures
 * todos:
 * - saving HDR content into HDR formats
 *
 * @author xmicha65
 */
public class ImageHDR implements Serializable {
    private Mat matHdrImage;

    public ImageHDR(String path) {
        open(path);
    }

    public ImageHDR(Mat matHdrImage) {
        this.matHdrImage = matHdrImage;
    }

    public void save(String name) {
        File path = Storages.getPublicImagesStorageDir("/hdr");
        File file = new File(path, String.format("%s.hdr", name));
        String filename = file.toString();
        System.out.println("### saving HDR: " + filename);

        if (Imgcodecs.imwrite(filename, matHdrImage))
            System.out.println("### HDR saved");
        else
            System.out.println("### HDR not saved!");
    }

    public void open(String path) {
        System.out.println("### opening HDR: " + path);
        matHdrImage = Imgcodecs.imread(path, -1);
        if (matHdrImage.empty())
            System.out.println("### HDR not loaded!");
        else
            System.out.println("### HDR loaded");
    }

    /**
     * GETTERS
     */

    public Mat getMatHdrImg() {
        return this.matHdrImage;
    }
}
