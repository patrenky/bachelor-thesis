package xmicha65.bp_app.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

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
        this.bmpImg = byteToBitmap(imageToByte(image));

        this.width = this.bmpImg.getWidth();
        this.height = this.bmpImg.getHeight();

        this.pixels = new int[this.width * this.height];
        this.bmpImg.getPixels(this.pixels, 0, this.width, 0, 0, this.width, this.height);
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

    public Bitmap matToBitmap(Mat image) {
        Mat tmpRGBA = new Mat();
        image.convertTo(tmpRGBA, CvType.CV_8UC4, 255);
        Bitmap bmp = Bitmap.createBitmap(tmpRGBA.cols(), tmpRGBA.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(tmpRGBA, bmp);
        return bmp;
    }

    public Mat bitmapToMat(Bitmap image) {
        Bitmap bmp32 = image.copy(Bitmap.Config.ARGB_8888, true);
        Mat matBGRA = new Mat(bmp32.getHeight(), bmp32.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(bmp32, matBGRA);
        Mat matBGR = new Mat();
        Imgproc.cvtColor(matBGRA, matBGR, Imgproc.COLOR_BGRA2BGR);
        return matBGR;
    }

    public Mat bitmapToMat() {
        return bitmapToMat(this.bmpImg);
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
}
