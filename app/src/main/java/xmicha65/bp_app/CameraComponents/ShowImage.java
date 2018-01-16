package xmicha65.bp_app.CameraComponents;

import android.media.Image;

public class ShowImage implements Runnable {
    private final Image mImage;

    public ShowImage(Image image) {
        mImage = image;
    }

    @Override
    public void run() {
        System.out.println("#### I have image ^_^");
    }

}