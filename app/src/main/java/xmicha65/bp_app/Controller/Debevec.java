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

    private int[][] Z;              // Z_ij pixel values of pixel i in image j
    private int N;                  // num of pixels to process (Z_i - 50 shades)
    private int P;                  // num of images (Z_j)

    private double lambda = 50;     // smoothness scaling factor
    private double lnT[];           // B(j)

    private double[] g;             // log response curve
    private double[] lnE;           // log irradiance map

    public Debevec(Image[] images) {
        this.images = images;
        this.P = images.length;
        initZij(this.images);
        initLnT(this.images);
    }

    private void initZij(Image[] images) {
        int middleOne = Math.round(images.length / 2);
        int[] fiftyPositions = images[middleOne].getfiftyPositions();
        this.N = fiftyPositions.length;
        this.Z = new int[this.N][this.P];

        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.P; j++) {
                this.Z[i][j] = images[j].getValue(fiftyPositions[i]);
            }
        }
    }

    private void initLnT(Image[] images) {
        lnT = new double[this.P];
        for (int i = 0; i < this.P; i++) {
            lnT[i] = Math.log(images[i].getExposure());
        }
    }

    public void solveG() {
        try {
            int n = 256;
            int mrows = this.N * this.P + n + 1;
            int mcols = n + this.N;

            // init A, b
            double[][] A = new double[mrows][mcols];
            double[] b = new double[mrows];

            // data-fitting equations
            int k = 0;
            for (int i = 0; i < this.N; i++) {
                for (int j = 0; j < this.P; j++) {
                    double wij = w(this.Z[i][j] + 1);

                    A[k][this.Z[i][j] + 1] = wij;
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
                A[k][i] = this.lambda * w(i + 2);
                A[k][i + 1] = -2 * this.lambda * w(i + 2);
                A[k][i + 2] = this.lambda * w(i + 2);
                k++;
            }

            // init Mat A
            Mat Amat = new Mat(mrows, mcols, CvType.CV_64F);
            for (int i = 0; i < Amat.rows(); i++) {
                for (int j = 0; j < Amat.cols(); j++) {
                    Amat.put(i, j, A[i][j]);
                }
            }

            // init Mat b
            Mat bmat = new Mat(mrows, 1, CvType.CV_64F);
            for (int i = 0; i < bmat.rows(); i++) {
                bmat.put(i, 0, b[i]);
            }

            // solve using SVD
            Mat solved = new Mat(mrows, 1, CvType.CV_64F);
            Core.solve(Amat, bmat, solved, Core.DECOMP_SVD);

            // init g
            this.g = new double[n];
            for (int i = 0; i < n; i++) {
                this.g[i] = solved.get(i, 0)[0];
            }

            // init lnE
            this.lnE = new double[solved.rows() - n];
            int i = 0;
            for (int j = n; j < solved.rows(); j++) {
                this.lnE[i] = solved.get(j, 0)[0];
                i++;
            }
        } catch (Exception e) {
            System.out.println("chyba solveG " + e.getMessage());
        }
    }

    /**
     * weighting function w(Z_ij)
     */
    private double w(double z) {
        double w0 = z <= 127 ? z : 255 - z;
        // double w1 =  Math.max((z <= 127) ? z + 1 : 256 - z, 0.0001);
        return w0;
    }

    /**
     * GETTERS
     */
    public double[] getG() {
        return this.g;
    }

    public double[] getLnE() {
        return this.lnE;
    }
}
