package xmicha65.bp_app.controller.tmo;

import android.graphics.Bitmap;

import org.opencv.core.Mat;
import org.opencv.photo.Photo;

import xmicha65.bp_app.controller.Convertor;
import xmicha65.bp_app.model.ImageHDR;

/**
 * Tonemapping of HDR content with OpenCV createTonemapReinhard() method
 */
public class TMOReinhard {
    private Mat ldrImage = new Mat(); // result of tonemapping

    public TMOReinhard(ImageHDR hdrImg, float gamma, float intensity, float lightAdapt, float colorAdapt) {
        Mat hdrImage = hdrImg.getMatHdrImg();
        Photo.createTonemapReinhard(gamma, intensity, lightAdapt, colorAdapt).process(hdrImage, ldrImage);
    }

    public Bitmap getImageBmp() {
        return Convertor.matToBitmap(this.ldrImage);
    }
}
