package xmicha65.bp_app.controller.tmo;

import android.graphics.Bitmap;

import org.opencv.core.Mat;
import org.opencv.photo.Photo;

import xmicha65.bp_app.controller.Convertor;

/**
 * Reinhard global TMO: OpenCV createTonemapReinhard() method
 */
public class TMOReinhard {
    private Mat ldrImage = new Mat(); // result of tonemapping

    public TMOReinhard(Mat matImage, float gamma, float intensity, float lightAdapt, float colorAdapt) {
        Photo.createTonemapReinhard(gamma, intensity, lightAdapt, colorAdapt).process(matImage, ldrImage);
    }

    public Bitmap getImageBmp() {
        return Convertor.matToBitmap(this.ldrImage);
    }
}
