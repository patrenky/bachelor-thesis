package xmicha65.bp_app.controller;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Recover Camera Response Function
 * @author xmicha65
 * source: Debevec, P.; Malik, J.: Recovering High Dynamic Range Radiance Maps from Photographs
 * http://www.pauldebevec.com/Research/HDR/debevec-siggraph97.pdf
 * Result of algorithm is CRF curve double[] g
 */
public class RecoverCRF {
    private int[][] Z;              // Z_ij pixel values of pixel i in image j
    private int N;                  // num of pixels to process (Z_i - 50 shades)
    private int P;                  // num of images (Z_j)

    private double lambda;          // smoothness scaling factor
    private double[] w;             // weighting function
    private double lnT[];           // log delta t for image j (B(j))

    private double[] g;             // log response curve
    private double[] lnE;           // log irradiance map

    public RecoverCRF(int[][] Zij, double[] lnT, double lambda, double[] weights) {
        this.Z = Zij;
        this.lnT = lnT;
        this.lambda = lambda;
        this.w = weights;
        this.P = Z[0].length;
        this.N = Z.length;

        createCRF();
    }

    /**
     * Solving quadratic objective function
     */
    public void createCRF() {
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
                    double wij = this.w[this.Z[i][j]]; // Z_ij + 1

                    A[k][this.Z[i][j] + 1] = wij;
                    A[k][n + i] = -wij;
                    b[k] = wij * this.lnT[j];

                    k++;
                }
            }

            // set middle value to 0
            A[k][128] = 1;
            k++;

            // smoothness equations
            for (int i = 0; i < n - 2; i++) { // n - 1
                A[k][i] = this.lambda * this.w[i + 1]; // + 2
                A[k][i + 1] = -2 * this.lambda * this.w[i + 1]; // + 2
                A[k][i + 2] = this.lambda * this.w[i + 1]; // + 2
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

            // createCRF using OpenCV SVD
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
     * GETTERS
     */

    public double[] getG() {
        return this.g;
    }

    public double[] getLnE() {
        return this.lnE;
    }
}
