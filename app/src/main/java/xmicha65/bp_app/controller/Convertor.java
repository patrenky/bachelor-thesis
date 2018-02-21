package xmicha65.bp_app.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Convertor {
    public static byte[] bitmapToByte(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap byteToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static byte[] imageToByte(Image image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        buffer.rewind();
        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        return byteArray;
    }

    public static Bitmap matToBitmap(Mat image) {
        Mat tmpRGBA = new Mat();
        image.convertTo(tmpRGBA, CvType.CV_8UC4, 255);
        Bitmap bmp = Bitmap.createBitmap(tmpRGBA.cols(), tmpRGBA.rows(), Bitmap.Config.ARGB_8888);
        org.opencv.android.Utils.matToBitmap(tmpRGBA, bmp);
        return bmp;
    }

    public static Mat bitmapToMat(Bitmap image) {
        Bitmap bmp32 = image.copy(Bitmap.Config.ARGB_8888, true);
        Mat matBGRA = new Mat(bmp32.getHeight(), bmp32.getWidth(), CvType.CV_8UC4);
        org.opencv.android.Utils.bitmapToMat(bmp32, matBGRA);
        Mat matBGR = new Mat();
        Imgproc.cvtColor(matBGRA, matBGR, Imgproc.COLOR_BGRA2BGR);
        return matBGR;
    }
}
