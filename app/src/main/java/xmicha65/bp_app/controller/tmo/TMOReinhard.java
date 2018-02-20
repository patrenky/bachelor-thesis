package xmicha65.bp_app.controller.tmo;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.photo.Photo;

/**
 * @author xmicha65
 */
public class TMOReinhard {
    private float gamma = 2.2f;         // for most displays
    private float intensity = 0.0f;     // range [-8, 8]
    private float light_adapt = 0.0f;   // range [0, 1]
    private float color_adapt = 0.0f;   // range [0, 1]

    private Mat ldrImage = new Mat(); // result of tonemapping

    public TMOReinhard(Mat hdrImage) {
        Photo.createTonemapReinhard(gamma, intensity, light_adapt, color_adapt).process(hdrImage, ldrImage);
    }

    public Bitmap getImageBmp() {
        Mat tmpRGBA = new Mat();
        this.ldrImage.convertTo(tmpRGBA, CvType.CV_8UC4, 255);
        Bitmap bmp = Bitmap.createBitmap(tmpRGBA.cols(), tmpRGBA.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(tmpRGBA, bmp);
        return bmp;
    }
}
