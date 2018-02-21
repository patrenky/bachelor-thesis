package xmicha65.bp_app.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.opencv.core.Mat;

import java.io.InputStream;

import xmicha65.bp_app.controller.Convertor;

/**
 * LDR image with attributes
 *
 * @author xmicha65
 */
public class ImageLDR {
    private double exposureTime = -1;       // exposure time of image
    private Bitmap bmpImg;                  // RGB bitmap image
    private int width;                      // image width
    private int height;                     // image height
    private int[] pixels;                   // raw argb android color

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ImageLDR(Image image, double exposition) {
        this.exposureTime = exposition;
        this.bmpImg = Convertor.byteToBitmap(Convertor.imageToByte(image));

        this.width = this.bmpImg.getWidth();
        this.height = this.bmpImg.getHeight();

        this.pixels = new int[this.width * this.height];
        this.bmpImg.getPixels(this.pixels, 0, this.width, 0, 0, this.width, this.height);
    }

    public ImageLDR(InputStream ins, double exposition) {
        this.exposureTime = exposition;
        this.bmpImg = BitmapFactory.decodeStream(ins);

        this.width = this.bmpImg.getWidth();
        this.height = this.bmpImg.getHeight();

        this.pixels = new int[this.width * this.height];
        this.bmpImg.getPixels(this.pixels, 0, this.width, 0, 0, this.width, this.height);
    }

    /**
     * GETTERS
     */

    public double getExposureTime() {
        return this.exposureTime;
    }

    public Bitmap getBmpImg() {
        return this.bmpImg;
    }

    public Mat getMatImg() {
        return Convertor.bitmapToMat(this.bmpImg);
    }

    public int getLength() {
        return this.pixels.length;
    }

    public double getExposure() {
        return this.exposureTime;
    }
}
