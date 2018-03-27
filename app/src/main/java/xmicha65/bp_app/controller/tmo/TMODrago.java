package xmicha65.bp_app.controller.tmo;

import android.graphics.Bitmap;

import org.opencv.core.Mat;
import org.opencv.photo.Photo;

import xmicha65.bp_app.controller.Convertor;

/**
 * OpenCV Drago global TMO
 */
public class TMODrago {
    private Mat ldrImage = new Mat(); // result of tonemapping

    public TMODrago(Mat matImage, float gamma, float saturation, float bias) {
        Photo.createTonemapDrago(gamma, saturation, bias).process(matImage, ldrImage);
    }

    public Bitmap getImageBmp() {
        return Convertor.matToBitmap(this.ldrImage);
    }
}
