package xmicha65.bp_app.controller.tmo;

import android.graphics.Bitmap;

import org.opencv.core.Mat;
import org.opencv.photo.Photo;

import xmicha65.bp_app.controller.Convertor;

/**
 * OpenCV Mantiuk local TMO
 */
public class TMOMantiuk {
    private Mat ldrImage = new Mat(); // result of tonemapping

    public TMOMantiuk(Mat matImage, float gamma, float scale, float saturation) {
        Photo.createTonemapMantiuk(gamma, scale, saturation).process(matImage, ldrImage);
    }

    public Bitmap getImageBmp() {
        return Convertor.matToBitmap(this.ldrImage);
    }
}
