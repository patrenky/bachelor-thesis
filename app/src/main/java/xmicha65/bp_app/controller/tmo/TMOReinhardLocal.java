package xmicha65.bp_app.controller.tmo;

import android.graphics.Bitmap;

import org.opencv.core.Mat;

import xmicha65.bp_app.controller.Convertor;

/**
 * zdroj: http://cybertron.cg.tu-berlin.de/eitz/hdr/
 */

public class TMOReinhardLocal {
    private Mat ldrImage = new Mat(); // result of tonemapping

    public TMOReinhardLocal(Mat matImage, double saturation, double eps, double phi) {
    }

    public Bitmap getImageBmp() {
        return Convertor.matToBitmap(this.ldrImage);
    }
}
