package xmicha65.bp_app.model;

/**
 * todos:
 * - save device CRF
 * - load device CRF
 *
 * @author xmicha65
 */
public class CameraCRF {
    private double[] crfRed;    // response curve for red color
    private double[] crfGreen;  // response curve for green color
    private double[] crfBlue;   // response curve for blue color

    public CameraCRF(double[] crfRed, double[] crfGreen, double[] crfBlue) {
        this.crfRed = crfRed;
        this.crfGreen = crfGreen;
        this.crfBlue = crfBlue;
    }

    /**
     * GETTERS
     */

    public double[] getCrfRed() {
        return crfRed;
    }

    public double[] getCrfGreen() {
        return crfGreen;
    }

    public double[] getCrfBlue() {
        return crfBlue;
    }
}
