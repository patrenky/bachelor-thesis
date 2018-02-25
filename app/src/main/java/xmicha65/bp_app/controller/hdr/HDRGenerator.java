//package xmicha65.bp_app.controller.hdr;
//
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//
//import java.util.Arrays;
//import java.util.List;
//
//import xmicha65.bp_app.model.CameraCRF;
//import xmicha65.bp_app.model.ImageHDR;
//import xmicha65.bp_app.model.ImageLDR;
//
//public class HDRGenerator {
//    private CameraCRF crfs;
//    private double[] w;
//    private double[] lnT;
//    private int numExposures;
//    private int numPixels;
//    private List<ImageLDR> images;
//
//    private ImageHDR hdrImage;
//
//    public HDRGenerator(CameraCRF cameraCRF, double[] weights, double[] lnT, int numPixels, int numExposures, List<ImageLDR> capturedImages) {
//        this.crfs = cameraCRF;
//        this.w = weights;
//        this.lnT = lnT;
//        this.numPixels = numPixels;
//        this.numExposures = numExposures;
//        this.images = capturedImages;
//
//        generateHDR();
//    }
//
//    private void generateHDR() {
//        Mat hdr = new Mat(numPixels, 1, CvType.CV_32FC3);
//        Mat sum = new Mat(numPixels, 1, CvType.CV_32FC3);
//
//        for (int i = 0; i < numExposures; i++) {
//            Mat pixels = new Mat(numPixels, 1, CvType.CV_8UC3);
//
//            for (int p = 0; p < numPixels; p++) {
//                pixels.put(p, 0, images.get(i).getPixelChannels(p));
//            }
//
//            Mat wij = new Mat(numPixels, 1, CvType.CV_32FC3);
//
//            for (int p = 0; p < numPixels; p++) {
//                double[] tmpPix = pixels.get(p, 0);
//                wij.put(p, 0,
//                        w[(int) tmpPix[0]],
//                        w[(int) tmpPix[1]],
//                        w[(int) tmpPix[2]]);
//            }
//
//            for (int p = 0; p < numPixels; p++) {
//                sum.put(p, 0,
//                        sum.get(p, 0)[0] + wij.get(p, 0)[0],
//                        sum.get(p, 0)[1] + wij.get(p, 0)[1],
//                        sum.get(p, 0)[2] + wij.get(p, 0)[2]);
//            }
//
//            // sum = sum + wij;
//            System.out.println("### wij: " + Arrays.toString(sum.get(0, 0)));
//        }
//    }
//
//    private double[] createHDR(double[] crf, Color color) {
//        double[] lnE = new double[this.numPixels];
//        double numerator;
//        double denominator;
//
//        try {
//            for (int i = 0; i < this.numPixels; i++) {
//                numerator = 0;
//                denominator = 0;
//                for (int j = 0; j < this.numExposures; j++) {
//                    int value = images.get(j).getPixel(i, color);
//                    numerator += this.w[value] * (crf[value] - this.lnT[j]);
//                    denominator += this.w[value];
//                }
//                lnE[i] = numerator / denominator;
//            }
//        } catch (Exception e) {
//            System.out.println("### catch createHDR " + e.getMessage());
//        }
//
//        return lnE;
//    }
//
//}
