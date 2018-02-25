package xmicha65.bp_app.model;

public enum Color {
    RED,
    GREEN,
    BLUE;

    public static int channels() {
        return 3;
    }

    public static Color getColor(int i) {
        switch (i) {
            case 0:
                return RED;
            case 1:
                return GREEN;
            case 2:
                return BLUE;
        }
        return null;
    }
}
