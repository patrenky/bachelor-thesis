package xmicha65.bp_app.controller.camera;


import org.opencv.core.Mat;
import org.opencv.photo.Photo;

import java.util.ArrayList;
import java.util.List;

import xmicha65.bp_app.model.ImageLDR;

public class AlignImages {

    public AlignImages(List<ImageLDR> capturedImages) {
        List<Mat> matUnaligned = new ArrayList<>();
        List<Mat> matAligned = new ArrayList<>(capturedImages.size());

        for (int i = 0; i < capturedImages.size(); i++) {
            matUnaligned.add(i, capturedImages.get(i).getMatImg());
        }

        System.out.println("### 1 " + matUnaligned.size());

        Photo.createAlignMTB().process(matUnaligned, matAligned);
        // TODO vracia aligned size 0
        System.out.println("### 2 " + matAligned.size());

//        for (int i = 0; i < matAligned.size(); i++) {
//            System.out.println("### aligned " + i);
//        }
        System.out.println("### 3");
    }
}
