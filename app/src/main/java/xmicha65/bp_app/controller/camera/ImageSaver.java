package xmicha65.bp_app.controller.camera;

import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import xmicha65.bp_app.controller.Storages;

/**
 * Helpfull class for debug capturing exposures
 * Saves a JPEG {@link Image} into the specified {@link File}.
 * @author https://github.com/googlesamples
 */
public class ImageSaver implements Runnable {

    /**
     * The JPEG image
     */
    private final Image mImage;
    /**
     * The file we save the image into.
     */
    private final File mFile;

    public ImageSaver(Image image, double exposure) {
        System.out.println("### init image saver " + String.format("%.12f.jpg", exposure));
        mImage = image;
        File file = Storages.getPublicImagesFile("/hdr", String.format("%.12f.jpg", exposure));
        mFile = file;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void run() {
        ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(mFile);
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mImage.close();
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}