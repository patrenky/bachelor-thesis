package xmicha65.bp_app.controller.tmo;

import android.graphics.Bitmap;

import org.opencv.core.Mat;
import org.opencv.photo.Photo;

import xmicha65.bp_app.controller.Convertor;

/**
 * OpenCV Reinhard global TMO
 */
public class TMOReinhardGlobal {
    private Mat ldrImage = new Mat(); // result of tonemapping

    public TMOReinhardGlobal(Mat matImage, float gamma, float intensity, float lightAdapt, float colorAdapt) {
        Photo.createTonemapReinhard(gamma, intensity, lightAdapt, colorAdapt).process(matImage, ldrImage);
    }

    public Bitmap getImageBmp() {
        return Convertor.matToBitmap(this.ldrImage);
    }
}
