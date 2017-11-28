package xmicha65.bp_app;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.photo.Photo;

import xmicha65.bp_app.Model.Image;

public class Main extends AppCompatActivity {
    Image[] images = new Image[4];
    double[] expTimes = {1 / 8, 1 / 4, 1 / 2, 2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initImages();
        displayImage(this.images[2].getGrayscaleImg());
//        new debevec

        testCV();
    }

    public void initImages() {
        for (int i = 0; i < 4; i++) {
            try {
                // nacitanie obrazku z assets do inp streamu
                InputStream ins = getAssets().open(String.format("stlouis/stlouis%d-sm.jpg", i));
                this.images[i] = new Image(ins, this.expTimes[i]);
            } catch (IOException e) {
                System.out.println("chyba nacitania obrazku z assets");
            }
        }
    }

    public void displayImage(Bitmap img) {
        Drawable drawable = new BitmapDrawable(getResources(), img);

        // ukazatel na obrazok vo view + vykreslenie drawable
        ImageView imageView = (ImageView) findViewById(R.id.myImage);
        imageView.setImageDrawable(drawable);

    }

    public void testCV() {
//        List<Mat> cvimages = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            Bitmap bmp32 = images[i].getOriginalImg().copy(Bitmap.Config.ARGB_8888, true);
//            Mat tmpImg = new Mat();
//            Utils.bitmapToMat(bmp32, tmpImg);
//            cvimages.add(tmpImg);
//        }
//
//        Mat hdrImage = new Mat();
////        Mat ldrImage = new Mat();
//        Mat expTimes = new MatOfDouble(this.expTimes);
//
//        Photo.createMergeDebevec().process(cvimages, hdrImage, expTimes);

        System.out.println("OKK");
    }
}
