package xmicha65.bp_app.controller.hdr;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import xmicha65.bp_app.model.CameraCRF;

/**
 * Recover Camera Response Function
 * Result of algorithm is CRF curve double[] g
 * source: P. Debevec
 *
 * @author xmicha65
 */
public class CRFRecover {
    private int N;                  // num of samples to process
    private int P;                  // num of exposures (Zj)

    private double lambda;          // smoothness scaling factor
    private double[] w;             // weighting function
    private double[] lnT;           // log delta t for image j (B(j))

    private CameraCRF cameraCRF;    // response curves of device camera

    public CRFRecover(double lambda, double[] weights, double[] lnT, SamplesSelector samples) {
        this.lambda = lambda;
        this.w = weights;
        this.lnT = lnT;
        this.N = samples.getNumSamples();
        this.P = samples.getNumExposures();

        // recover crf for each channel
        double[] crfRed = recoverCRF(samples.getSamplesRed());
        double[] crfGreen = recoverCRF(samples.getSamplesGreen());
        double[] crfBlue = recoverCRF(samples.getSamplesBlue());

        // push curves into CameraCRF object
        cameraCRF = new CameraCRF(crfRed, crfGreen, crfBlue);
    }

    /**
     * Solving quadratic objective function
     * Returns response curve for specified channel
     */
    public double[] recoverCRF(int[][] Z) {
        try {
            int n = 256;
            int matRows = this.N * this.P + n + 1;
            int matCols = n + this.N;

            // init A, b
            double[][] A = new double[matRows][matCols];
            double[] b = new double[matRows];

            // data-fitting equations
            int k = 0;
            for (int i = 0; i < this.N; i++) {
                for (int j = 0; j < this.P; j++) {
                    double wij = this.w[Z[i][j]]; // Z_ij + 1

                    A[k][Z[i][j] + 1] = wij;
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
            Mat Amat = new Mat(matRows, matCols, CvType.CV_64F);
            for (int i = 0; i < Amat.rows(); i++) {
                for (int j = 0; j < Amat.cols(); j++) {
                    Amat.put(i, j, A[i][j]);
                }
            }

            // init Mat b
            Mat bmat = new Mat(matRows, 1, CvType.CV_64F);
            for (int i = 0; i < bmat.rows(); i++) {
                bmat.put(i, 0, b[i]);
            }

            // createCRF using OpenCV SVD
            Mat solved = new Mat(matRows, 1, CvType.CV_64F);
            Core.solve(Amat, bmat, solved, Core.DECOMP_SVD);

            // init g
            double[] g = new double[n];
            for (int i = 0; i < n; i++) {
                g[i] = solved.get(i, 0)[0];
            }

            // init side product lnE
//        double[] lnE = new double[solved.rows() - n];
//        int i = 0;
//        for (int j = n; j < solved.rows(); j++) {
//            lnE[i] = solved.get(j, 0)[0];
//            i++;
//        }

            return g;
        } catch (Exception e) {
            System.out.println("### catch recoverCRF " + e.getMessage());
            return null;
        }
    }

    public CameraCRF getCameraCRF() {
        return cameraCRF;
    }
}
