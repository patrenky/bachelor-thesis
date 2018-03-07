package xmicha65.bp_app.model;

import java.util.List;

public enum Exposure {
    e14(123);

    private int value;

    Exposure(int value) {
        this.value = value;
    }

    static {
        for (Exposure expo : Exposure.values()) {
            // push expo.value
        }
    }

    public int closest(int of, List<Integer> in) {
        int min = Integer.MAX_VALUE;
        int closest = of;

        for (int v : in) {
            final int diff = Math.abs(v - of);

            if (diff < min) {
                min = diff;
                closest = v;
            }
        }

        return closest;
    }

}
