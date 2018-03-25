package xmicha65.bp_app.model;

import android.graphics.Bitmap;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;

import xmicha65.bp_app.controller.Storages;

/**
 * HDR content of merged exposures
 *
 * @author xmicha65
 */
public class ImageHDR implements Serializable {
    private Mat matHdrImage;
    private Mat matHdrTemp = new Mat();
    private Bitmap bmpImage;

    public ImageHDR(String path) {
        openHdr(path);
        compressImage();
    }

    public ImageHDR(Mat matHdrImage) {
        this.matHdrImage = matHdrImage;
        compressImage();
    }

    public ImageHDR(Bitmap bmp) {
        this.bmpImage = bmp;
    }

    private void compressImage() {
        System.out.println("### compressing temp");
        Size dSize = new Size(matHdrImage.cols() / 5, matHdrImage.rows() / 5);
        Imgproc.resize(matHdrImage, matHdrTemp, dSize);
    }

    public void save(String name, ImageType imageType) {
        boolean success = false;

        if (imageType == ImageType.HDR) success = saveHdr(name);
        else if (imageType == ImageType.LDR) success = saveLdr(name);

        System.out.println("### image saved: " + success);
    }

    private boolean saveLdr(String name) {
        try {
            File file = Storages.getPublicImagesFile("/hdr", String.format("%s.jpg", name));
            if (file == null) return false;

            System.out.println("### saving LDR: " + file.toString());
            FileOutputStream outputStream = new FileOutputStream(file);
            bmpImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();

            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean saveHdr(String name) {
        File file = Storages.getPublicImagesFile("/hdr", String.format("%s.hdr", name));
        if (file == null) return false;

        String filename = file.toString();
        System.out.println("### saving HDR: " + filename);

        return Imgcodecs.imwrite(filename, matHdrImage);
    }

    private void openHdr(String path) {
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

    public Mat getMatHdrTemp() {
        return matHdrTemp;
    }
}
