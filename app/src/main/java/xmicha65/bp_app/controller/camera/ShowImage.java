package xmicha65.bp_app.controller.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

import java.nio.ByteBuffer;

/**
 * Temporary class for displaying captured images on ImageView
 * @author xmicha65
 */
public class ShowImage implements Runnable {
    private final ImageView iv;
    private final Image image;

    public ShowImage(ImageView siv, Image simage) {
//        System.out.println("#### captured: " + simage);
        image = simage;
        iv = siv;
    }

    @Override
    public void run() {}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void display() {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);

        try {
            iv.setImageBitmap(bitmapImage);
        } catch (Exception e) {}
    }
}