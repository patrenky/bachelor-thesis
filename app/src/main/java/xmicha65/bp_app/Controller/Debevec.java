package xmicha65.bp_app.Controller;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import xmicha65.bp_app.Model.Image;

/**
 * Debevec & Malik
 */
public class Debevec {
    private Image[] images;         // input images array
    private int N;                  // num of pixels in one image
    private int P;                  // num of images

    private double lambda = 50;     // smoothness scaling factor
    private double lnT[];           // B(j)

    public Debevec(Image[] images) {
        this.images = images;
        this.N = images[0].getLength();
        this.P = images.length;
        initLnT(this.images);
    }

    public void solveG() {
        int n = 256;
        int mrows = this.N * this.P + n;
        int mcols = n + this.N;
        double[][] A = new double[mrows][mcols];
        double[] b = new double[mrows];

        // init A
        for (int i = 0; i < mrows + 1; i++) {
            double[] tmp = new double[n + this.N];
            for (int j = 0; j < mcols; j++) {
                tmp[j] = 0.0;
            }
            A[i] = tmp;
        }
        // init b
        for (int i = 0; i < mrows + 1; i++) {
            b[i] = 0.0;
        }

        // data-fitting equations
        int k = 0;
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.P; j++) {
                double wij = w(Z(i, j) + 1);

                A[k][Z(i, j) + 1] = wij;
                A[k][n + i] = -wij;
                b[k] = wij * lnT[j];

                k++;
            }
        }

        // set middle value to 0
        A[k][128] = 1;
        k++;

        // smoothness equations
        for (int i = 0; i < n - 1; i++) {
            A[k][i] = lambda * w(i + 2);
            A[k][i + 1] = -2 * lambda * w(i + 2);
            A[k][i + 2] = lambda * w(i + 2);
            k++;
        }

        // solve using SVD
        Mat Amat = new Mat(mrows, mcols, CvType.CV_64F);
        for (int i = 0; i < Amat.rows(); i++) {
            for (int j = 0; j < Amat.cols(); j++) {
                Amat.put(i, j, A[i][j]);
            }
        }

        Mat bmat = new Mat(mrows, 1, CvType.CV_64F);
        for (int i = 0; i < Amat.rows(); i++) {
            bmat.put(i, 0, b[i]);
        }

        Mat solved = new Mat(mrows, 1, CvType.CV_64F);
        Core.solve(Amat, bmat, solved, Core.DECOMP_SVD);

        // double[] g = solved[0 <-> n]
        // lnE = solved[n + 1 <-> mrows]
    }

    /**
     * weighting function w(Z_ij)
     *
     * @param z value
     * @return weightning value
     */
    protected double w(double z) {
        double w0 = z <= 127 ? z : 255 - z;
        // double w1 =  Math.max((z <= 127) ? z + 1 : 256 - z, 0.0001);
        return w0;
    }

    /**
     * get luminance value i of picture j
     * TODO get value from 50array
     */
    private int Z(int i, int j) {
        return images[j].getValue(i);
    }

    /**
     * array to store the ln(t_j)
     *
     * @param images
     */
    private void initLnT(Image[] images) {
        lnT = new double[images.length];
        for (int j = 0; j < lnT.length; j++) {
            lnT[j] = Math.log(images[j].getExposure());
        }
    }
}
