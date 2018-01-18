package xmicha65.bp_app.CameraComponents;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

import java.nio.ByteBuffer;

public class ShowImage implements Runnable {
    private final ImageView iv;
    private final Image image;

    public ShowImage(ImageView siv, Image simage) {
        System.out.println("#### captured: " + simage);
        image = simage;
        iv = siv;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
//        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//        byte[] bytes = new byte[buffer.capacity()];
//        buffer.get(bytes);
//        Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
//
//        try {
//            iv.setImageBitmap(bitmapImage);
//        } catch (Exception e) {}
    }

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