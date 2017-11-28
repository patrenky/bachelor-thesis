package xmicha65.bp_app.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.io.InputStream;

/**
 * luminance: https://stackoverflow.com/questions/596216/formula-to-determine-brightness-of-rgb-color
 * histogram: http://hdr-photographer.com/2014/03/tutorial-understanding-histogram/
 */
public class Image {
    private double exposureTime = -1;
    private Bitmap originalImg;
    private Bitmap grayscale;
    private int[] pixels;               // raw rgba android color
    private int[] luminance;            // 0-255
    private int w;
    private int h;
//    private int[] histogram = new int[256];
//    private int min = -1;
//    private int max = -1;

    public Image(InputStream ins, double exposition) {
        this.originalImg = BitmapFactory.decodeStream(ins);
        this.w = originalImg.getWidth();
        this.h = originalImg.getHeight();
        this.grayscale = toGrayscale(this.originalImg, w, h);
        this.exposureTime = exposition;
        getData();
    }

    private Bitmap toGrayscale(Bitmap bmpOriginal, int width, int height) {
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    private int getLuminance(int r, int g, int b) {
        return (int) Math.round(0.299 * r + 0.587 * g + 0.114 * b);
    }

    private void getData() {
        this.pixels = new int[this.w * this.h];
        this.luminance = new int[this.w * this.h];
        this.originalImg.getPixels(pixels, 0, this.w, 0, 0, this.w, this.h);

        // luminance
        for (int i = 0; i < this.pixels.length; i++) {
            int r = (this.pixels[i] >> 16) & 0xff;
            int g = (this.pixels[i] >> 8) & 0xff;
            int b = (this.pixels[i]) & 0xff;
            this.luminance[i] = getLuminance(r, g, b);
        }

        // update histogram of luminance values
        // updateHistogram();
    }

    /**
     * returns luminance value of this picture in a given index.
     *
     * @param i
     * @return
     */
    public int getValue(int i) {
        if (luminance != null && i >= 0 && i < w * h) {
            return luminance[i];
        } else {
            return -1;
        }
    }

    public Bitmap getOriginalImg() {
        return this.originalImg;
    }

    public Bitmap getGrayscaleImg() {
        return this.grayscale;
    }

    public int getLength() {
        return this.pixels.length;
    }

    public double getExposure() {
        return this.exposureTime;
    }

    /**
     * @return height of image
     */
    public int getHeight() {
        return h;
    }

    /**
     * @return width of image
     */
    public int getWidth() {
        return w;
    }

    //    private void updateHistogram() {
//        // calculate histogram of
//        this.histogram = new int[256];
//        if (this.luminance.length > 0) {
//            this.max = this.luminance[0];
//            this.min = this.luminance[0];
//            for (int i = 0; i < this.luminance.length; i++) {
//                this.histogram[this.luminance[i]] += 1;
//                this.max = Math.max(this.luminance[i], this.max);
//                this.min = Math.min(this.luminance[i], this.min);
//            }
//        }
//    }

//    public int[] getHistogram() {
//        return this.histogram;
//    }
}
