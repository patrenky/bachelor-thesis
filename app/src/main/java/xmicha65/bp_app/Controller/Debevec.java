package xmicha65.bp_app.Controller;

import xmicha65.bp_app.Model.BandMatrix;
import xmicha65.bp_app.Model.EquationSolver;
import xmicha65.bp_app.Model.EquationSolverAlgorithm;
import xmicha65.bp_app.Model.EquationSolverException;
import xmicha65.bp_app.Model.HDRImage;
import xmicha65.bp_app.Model.Image;
import xmicha65.bp_app.Model.Vector;

/**
 * Debevec & Malik
 */
public class Debevec {
    private double lambda = 50;     // smoothness scaling factor (50)
    private double mu = 5;          // monotone scaling factor
    private double alpha = 0;       // smoothness term of E scaling factor
    private int N;                  // num of pixels in one image
    private int P;                  // num of images

    private BandMatrix dt, d, weight;
    private double ln_t[];
    private double EPSILON_2 = 0.0001d;

    private Image[] images;
    private HDRImage result;

    public Debevec(Image[] images) {
        this.images = images;
        this.N = images[0].getLength();
        this.P = images.length;
        initLnT(images); // OK
//        initWeightMatrix(); // treba?
//        initDerivativeMatrices(); // treba?

        merge();
    }

    public void merge() {
        try {
            Vector lnE = new Vector(N); // radiance map

            for (int i = 0; i < lnE.length(); i++)
                lnE.set(i, 1);

            System.out.println("CHP lnE inited");

            Vector g = new Vector(256); // response curve

            // init curve values g(0) = -5, g(127)=0, g(256) = 5
            for (int i = 0; i < g.length(); i++)
                g.set(i, -5 + i * 5.0 / 127.0);

            System.out.println("CHP g inited");

            // lnE = calculateLnE(g, lnE);
            // System.out.println("CHP ln E calculated");

            g = calculateG(lnE, g);
            // System.out.println("CHP g calculated");


            System.out.println("CHP debevec done with no err ");
//          hdrResult(new HDRImage(lnE.exp(), images[0].getWidth(), images[0].getHeight()));
        } catch (Exception e) {
            System.out.println("ERR debevec: " + e.getMessage());
        }
    }

//    private void hdrResult(HDRImage h) {
//        this.result = h;
//        double[] r = h.getE().toArray();
//        // System.out.println("KONIEC here is your result[20]: " + r[20]);
//    }

    /**
     * weighting function w(Z_ij)
     * OK
     *
     * @param z value
     * @return Weightning value
     */
    protected double w(double z) {
        double w0 = z <= 127 ? z : 255 - z;
        // double w1 =  Math.max((z <= 127) ? z + 1 : 256 - z, 0.0001);
        return w0;
    }

    /**
     * OK
     *
     * @param z value (0-255)
     * @return w(z) * w(z)
     */
    private double w2(int z) {
        return w(z) * w(z);
    }

    /**
     * array to store the ln(t_j)
     * OK
     *
     * @param images
     */
    private void initLnT(Image[] images) {
        ln_t = new double[images.length];
        for (int j = 0; j < ln_t.length; j++) {
            ln_t[j] = Math.log(images[j].getExposure());
        }
    }

    /**
     * get luminance value i of picture j
     * OK
     */
    private int Z(int i, int j) {
        return images[j].getValue(i);
    }

    /**
     * Calculates the new ln E. If required it integrates the neighbors in the calculation.
     * TODO
     *
     * @param g   current instance of reponse curve g
     * @param lnE current instance of the radiance map
     * @return new instance of lnE
     */
    private Vector calculateLnE(Vector g, Vector lnE) {
        double dividend;
        double denominator;
        for (int i = 0; i < N; i++) {
            dividend = 0;
            denominator = 0;
            for (int j = 0; j < P; j++) {
                dividend += (g.get(Z(i, j)) - ln_t[j]) * w(Z(i, j));
                denominator += w2(Z(i, j));
                System.out.println("ln E_i: " + dividend + " / " + denominator);
            }
            lnE.set(i, dividend / denominator);
        }
        return lnE;
    }

//    /**
//     * Generates the required derivative matrices for the calculation
//     */
//    private void initDerivativeMatrices() {
//        int size = 256;
//        d = new BandMatrix(size, new int[]{-1, 0});
//        d.set(0, 0, 0);
//        for (int row = 1; row < size; row++) {
//            d.set(row, row - 1, -1);
//            d.set(row, row, 1);
//        }
//        dt = d.transpose();
//    }

//    /**
//     * inits the weight matrix. it is a diagonale matrix with the weights of w(z) on each entry
//     */
//    private void initWeightMatrix() {
//        weight = new BandMatrix(256, new int[]{0});
//        for (int i = 0; i < weight.cols(); i++) {
//            weight.set(i, i, w(i));
//        }
//    }

    /**
     * This method constructs a fourth derivative matrix approximation.
     *
     * @return fourth matrix derivative
     */
    private BandMatrix buildDerivativeMatrix() {
        int n = 256;
        BandMatrix d = new BandMatrix(n, new int[]{-2, -1, 0, 1, 2});
        d.set(0, 0, +1 * w2(1));
        d.set(0, 1, -2 * w2(1));
        d.set(0, 2, +1 * w2(1));
        // second row
        d.set(1, 0, -2 * w2(1));
        d.set(1, 1, 4 * w2(1) + w2(2));
        d.set(1, 2, -2 * (w2(1) + w2(2)));
        d.set(1, 3, w2(2));
        // diagonale
        for (int row = 2; row < n - 2; row++) {
            // default matrixG 2nd degree on the base.
            d.set(row, row - 2, w2(row - 1));
            d.set(row, row - 1, -2 * (w2(row - 1) + w2(row)));
            d.set(row, row, w2(row - 1) + 4 * w2(row) + w2(row + 1));
            d.set(row, row + 1, -2 * (w2(row) + w2(row + 1)));
            d.set(row, row + 2, w2(row + 1));
        }
        // second last row
        d.set(n - 2, n - 4, w2(253));
        d.set(n - 2, n - 3, -2 * (w2(253) + w2(254)));
        d.set(n - 2, n - 2, w2(253) + 4 * w2(254));
        d.set(n - 2, n - 1, -2 * w2(254));

        // last row
        d.set(n - 1, n - 3, w2(254));
        d.set(n - 1, n - 2, -2 * w2(254));
        d.set(n - 1, n - 1, w2(254));

        return d.mult(2 * lambda);
    }

    public void printMatrix(int n, BandMatrix m) {
        System.out.println("####");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                System.out.print(m.get(i, j) + " ");
            System.out.println();
        }
        System.out.println("####");
    }

    /**
     * Alternating solving step to calculate the discrete function g out of the Vector lnE and the old value of g
     *
     * @param lnE current value of lnE (ln E_i)
     * @param g   last calculated version of vector g
     * @return new version of g
     */
    private Vector calculateG(Vector lnE, Vector g) {
        BandMatrix m = buildDerivativeMatrix();

        Vector b = initializeB(lnE, g);
        // add on the diagonale the Matrix with the sums of each grayvalue in the picture.
        // Entry (k,k) says how many time the grayvalue k is present overall pictures and
        // is added to the derivate matrix above
        setupDataTerm(m);

//        if (mu > 0) {
//            m = monotonieConstraint(g, m);
//        }


        try {
            g = EquationSolver.solve(m, b, EquationSolverAlgorithm.LU);
        } catch (EquationSolverException e) {
            System.out.println("ERR equation solver" + e.getMessage());
        }
        // fix g to be zero at grey value 127
        g = g.subtract(g.get(127));
        return g;
    }


    /**
     * sets up the data term in the given band matrix m. if robustness is required in this data term it is addes as factors.
     *
     * @param m the current matrix.
     */
    private void setupDataTerm(BandMatrix m) {
        // DATA TERM
        for (int k = 0; k < m.rows(); k++) {
            // double t = w(k) * histogram[k]; // TODO

            // m.set(k, k, m.get(k, k) + t);
        }
    }

//    /**
//     * returns a matrix which represents the monotonie constraint. The result is the addition of the monotonie matrix and
//     * the current calculation matrix. This matrix will be returned.
//     *
//     * @param g last calculated version of g
//     * @param m the current band Matrix
//     * @return Matrix m + monotonie constraint matrix
//     */
//    private BandMatrix monotonieConstraint(Vector g, BandMatrix m) {
//        BandMatrix vwwv = new BandMatrix(256, new int[]{0});
//        for (int i = 1; i < g.length(); i++) {
//            double diff = g.get(i - 1) - (g.get(i));
//            if (diff > 0) {
//                vwwv.set(i, i, w2(i));
//            }
//        }
//
//        // mu *(dt * vt * wt * w * v * d)
//        // = mu (dt * vwwv * d)
//        BandMatrix mon = dt.mult(vwwv.mult(d)).mult(mu);
//        return m.add(mon);
//    }

    /**
     * sets the vector for the right hand side of a calculation. It keeps all the stuff like adding robustness functions
     * and so on encapsulated from the user.
     *
     * @param F    old Vector F (ln E)
     * @param oldG old Version of the discrete function g
     * @return Vector b for the right hand side.
     */
    private Vector initializeB(Vector F, Vector oldG) {
        Vector b;
        b = new Vector(oldG.length());
        for (int k = 0; k < b.length(); k++) {
            double w = (w(k));
            double s = 0;
            for (int i = 0; i < F.length(); i++) {
                for (int j = 0; j < ln_t.length; j++) {
                    if (Z(i, j) == k) {
                        double t = F.get(i) + ln_t[j];
                        s += t;
                    }
                }
            }
            b.set(k, s * w);
        }
        for (int k = 0; k < b.length(); k++)
            System.out.print(b.get(k) + " ");
        System.out.println();
        return b;
    }

//    /**
//     * Solves the calculation of new values for F (ln E) with activated influence of the neighbors in the resulting radiance map.
//     *
//     * @param g     current instance of reponse curve g
//     * @param F     current value of F (ln E_i)
//     * @param alpha smoothness term of E scaling factor (0 disables this)
//     * @return the new instance of F
//     * @throws EquationSolverException if the equotation could not be solved because of some issues.
//     */
//    private Vector calculateFWithNeighboorhood(Vector g, Vector F, double alpha) throws EquationSolverException {
//        int cols = images[0].getWidth();
//        int rows = images[0].getHeight();
//
//        BandMatrix res = generateNeighborsBandMatrix(F, alpha, cols, rows);
//        Vector b = new Vector(F.length());
//        for (int i = 0; i < b.length(); i++) {
//            double sum = 0;
//            for (int j = 0; j < P; j++) {
//                sum += w2(Z(i, j)) * (g.get(Z(i, j)) - ln_t[j]);
//            }
//            b.set(i, sum);
//        }
//        return EquationSolver.solve(res, b, EquationSolverAlgorithm.SOR);
//    }


//    /**
//     * returns the coefficient if a robust subquadratic penalty function is required.
//     *
//     * @param F current value of F (ln E_i)
//     * @param i first index in F
//     * @param j second index in F
//     * @return coefficient for the 2d smoothness of F
//     */
//    private double phi_smoothness_e(Vector F, int i, int j) {
//        return 1;
//    }


//    /**
//     * Generates the Matrix which calculates the influence of each pixel on the others. The distance of influence is only 1.
//     * <p/>
//     * The returning matrix will be a band matrix with 5 setted elements (the central, top, left, right, bottom pixel).
//     * The center pixel will be the positive sum of the 4 surrounding pixels.
//     *
//     * @param F     current instance of the radiance map
//     * @param alpha the factor of the smoothness term
//     * @param cols  number of cols in the picture
//     * @param rows  number of rows in the picture
//     * @return BandMatrix which represents the (scaled) influence of the surrounding pixels
//     */
//    private BandMatrix generateNeighborsBandMatrix(Vector F, double alpha, int cols, int rows) {
//        BandMatrix neighborsBandMatrix = new BandMatrix(cols * rows, new int[]{-cols, -1, 0, 1, cols});
//        for (int i = 0; i < cols * rows; i++) {
//            // weight
//            double sum = 0;
//            for (int j = 0; j < P; j++) {
//                sum += w2(Z(i, j));
//            }
//            double d = 0;
//            // left band
//            if (i - 1 >= 0) {
//                double v = phi_smoothness_e(F, i, i - 1);
//                d += v;
//                neighborsBandMatrix.set(i - 1, i, -v * alpha);
//            }
//            // right band
//            if (i + 1 < cols * rows) {
//                double v = phi_smoothness_e(F, i + 1, i);
//                d += v;
//                neighborsBandMatrix.set(i + 1, i, -v * alpha);
//            }
//            // upper band
//            if (i - cols >= 0) {
//                double v = phi_smoothness_e(F, i, i - cols);
//                d += v;
//                neighborsBandMatrix.set(i - cols, i, -v * alpha);
//            }
//            // lower band
//            if (i + cols < cols * rows) {
//                double v = phi_smoothness_e(F, i + cols, i);
//                d += v;
//                neighborsBandMatrix.set(i + cols, i, -v * alpha);
//            }
//
//            neighborsBandMatrix.set(i, i, sum + alpha * d);
//
//        }
//        return neighborsBandMatrix;
//    }

}
