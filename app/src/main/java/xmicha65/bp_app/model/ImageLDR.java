package xmicha65.bp_app.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * LDR image with attributes
 *
 * @author xmicha65
 */
public class ImageLDR {
    private double exposureTime = -1;       // exposure time of image
    private Bitmap rgbImg;                  // RGB bitmap image
    private int width;                      // image width
    private int height;                     // image height
    private int[] pixels;                   // raw argb android color

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ImageLDR(Image image, double exposition) {
        this.exposureTime = exposition;
        this.rgbImg = byteToBitmap(imageToByte(image));

        this.width = this.rgbImg.getWidth();
        this.height = this.rgbImg.getHeight();

        this.pixels = new int[this.width * this.height];
        this.rgbImg.getPixels(this.pixels, 0, this.width, 0, 0, this.width, this.height);
    }

    /**
     * UTILS
     */

    public byte[] bitmapToByte(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public Bitmap byteToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public byte[] imageToByte(Image image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        buffer.rewind();
        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        return byteArray;
    }
}
