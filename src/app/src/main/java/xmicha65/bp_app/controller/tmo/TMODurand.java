package xmicha65.bp_app.controller.tmo;

import android.graphics.Bitmap;

import org.opencv.core.Mat;
import org.opencv.photo.Photo;

import xmicha65.bp_app.controller.Convertor;

/**
 * OpenCV Durand local TMO
 */
public class TMODurand {
    private Mat ldrImage = new Mat(); // result of tonemapping

    public TMODurand(Mat matImage, float gamma, float contrast, float saturation, float sigma_space, float sigma_color) {
        Photo.createTonemapDurand(gamma, contrast, saturation, sigma_space, sigma_color).process(matImage, ldrImage);
    }

    public Bitmap getImageBmp() {
        return Convertor.matToBitmap(this.ldrImage);
    }
}
