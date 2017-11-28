package xmicha65.bp_app.Model;

/**
 * Exception which is thrown if a system of equations is not solvable in the @see EquationSolver.
 */
public class EquationSolverException extends Exception {
    public EquationSolverException(String s) {
        super(s);
    }

    public EquationSolverException(String s, AbstractMatrix m, Vector b) {
        this(String.format("%s : %n  %s %n  %s", s, m.debugString(), b.debugString()));
    }
}
