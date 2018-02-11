package xmicha65.bp_app.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.sql.SQLOutput;
import java.util.Arrays;

/**
 * Image class with attributes
 * @author xmicha65
 */
public class ImageLDR {
    private double exposureTime = -1;       // exposure time of image
    private int width;                      // image width
    private int height;                     // image height
    private int[] pixels;                   // raw argb android color
    private Bitmap rgbImg;                  // RGB bitmap
    private int[] luminance;                // avg rgb values (0-255)
    private int[] histogram = new int[256]; // histogram of luminance
    private int[] fiftyShades;              // (max) 50 selected values from histogram (0-255)
    private int[] fiftyPositions;           // positions of 50 shades in luminance array

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ImageLDR(Image im, double exposition) {
        this.exposureTime = exposition;
        this.rgbImg = imageToBitmap(im);
        System.out.println("@@@@@@" + rgbImg);
//        this.width = this.rgbImg.getWidth();
//        this.height = this.rgbImg.getHeight();
//
//        this.pixels = new int[this.width * this.height];
//        this.rgbImg.getPixels(this.pixels, 0, this.width, 0, 0, this.width, this.height);
//
//        this.luminance = new int[this.width * this.height];
//
//        getLuminance();
//        updateHistogram();
//        getFiftyMostFrequented();
//        getFiftyPositions();
    }

    private int getLuminanceValue(int r, int g, int b) {
        return (int) Math.round(0.299 * r + 0.587 * g + 0.114 * b);
    }

    /**
     * Get luminance value for each pixel
     */
    private void getLuminance() {
        for (int i = 0; i < this.pixels.length; i++) {
            int r = (this.pixels[i] >> 16) & 0xff;
            int g = (this.pixels[i] >> 8) & 0xff;
            int b = (this.pixels[i]) & 0xff;
            this.luminance[i] = getLuminanceValue(r, g, b);
        }
    }

    /**
     * Update histogram of luminance values
     */
    private void updateHistogram() {
        this.histogram = new int[256];
        if (this.luminance.length > 0) {
            for (int i = 0; i < this.luminance.length; i++) {
                this.histogram[this.luminance[i]] += 1;
            }
        }
    }

    /**
     * init 50 most frequented values of luminance in image
     */
    private void getFiftyMostFrequented() {
        int[] sorted = this.histogram.clone();
        Arrays.sort(sorted);
        int size = sorted.length < 50 ? sorted.length : 50;
        this.fiftyShades = new int[size];
        for (int i = 0; i < size; i++) {
            int value = findIndex(this.histogram, sorted[sorted.length - i - 1]);
            this.fiftyShades[i] = value;
        }
    }

    /**
     * find positions of fifty shades values in luminance array
     */
    private void getFiftyPositions() {
        int size = this.fiftyShades.length;
        this.fiftyPositions = new int[size];
        for (int i = 0; i < size; i++) {
            int value = findIndex(this.luminance, this.fiftyShades[i]);
            this.fiftyPositions[i] = value;
        }
    }

    /**
     * GETTERS
     */
    public int getValue(int i) {
        if (luminance != null && i >= 0 && i < width * height) {
            return luminance[i];
        } else {
            return -1;
        }
    }

    public int getPixel(int idx) {
        return this.pixels[idx];
    }

    public int getPixelRed(int idx) {
        return (this.pixels[idx] >> 16) & 0xff;
    }

    public int getPixelGreen(int idx) {
        return (this.pixels[idx] >> 8) & 0xff;
    }

    public int getPixelBlue(int idx) {
        return (this.pixels[idx]) & 0xff;
    }

    public Bitmap getRgbImg() {
        return this.rgbImg;
    }

    public double getExposure() {
        return this.exposureTime;
    }

    public int getLength() {
        return this.pixels.length;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[] getHistogram() {
        return this.histogram;
    }

    public int[] getFiftyShades() {
        return this.fiftyShades;
    }

    public int[] getfiftyPositions() {
        return this.fiftyPositions;
    }

    /**
     * UTILS
     *
     * Find index of value in array
     * Returns first index found
     */
    public int findIndex(int[] array, int value) {
        for (int i = 0; i < array.length; i++)
            if (array[i] == value)
                return i;
        return -1;
    }

    public void printArray(int[] a) {
        System.out.println("----------------");
        for (int i = 0; i < a.length; i++)
            System.out.print(a[i] + " ");
        System.out.println("\n----------------");
    }

    public byte[] bitmapToByte(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public Bitmap byteToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Bitmap imageToBitmap(Image im) {
        try {
            ByteBuffer buffer = im.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.capacity()];
            buffer.get(bytes);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
        } catch (Exception e) {
            System.out.println("@@@@ imageToBitmap exception: " + e.getMessage());
            return null;
        }
    }
}
