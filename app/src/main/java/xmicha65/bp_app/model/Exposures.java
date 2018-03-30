package xmicha65.bp_app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Find exposure values in valid device range with middle value recovered from
 * automatic exposure and with custom step
 *
 * @author xmicha65
 */
public class Exposures {
    private List<Long> range = new ArrayList<>();       // exposures in valid range
    private List<Long> exposures = new ArrayList<>();   // selected exposures for capture
    private int numExposures = 5;                       // number of selected exposures
    private int step = 2;                               // select exposures in range step

    public Exposures(long exposureMin, long exposureMax) {
        long rangeMin = nextPowerOfTwo(exposureMin);
        long rangeMax = nextPowerOfTwo(exposureMax) / 2;

        long exposure = rangeMin;
        while (exposure <= rangeMax) {
            // push 2^n exposure values in range
            range.add(exposure);
            exposure *= 2;
        }
    }

    public void initExposures(long exposure) {
        // init first idx pointers
        long midExposure = nextPowerOfTwo(exposure);
        int midIdx = range.indexOf(midExposure);
        int lIdx = midIdx - step; // lower idx
        int gIdx = midIdx + step; // greater idx

        // push middle value into list
        exposures.add(midExposure);

        int num = 1;
        boolean larger = false;
        boolean found = false;

        while (num < numExposures) {
            if (larger) {
                if (gIdx <= rangeSize() - 1) {
                    exposures.add(range.get(gIdx));
                    found = true;
                    gIdx += step;
                    num++;
                } else {
                    if (!found) break;
                    found = false;
                }
                larger = false;
            } else {
                if (lIdx >= 0) {
                    exposures.add(range.get(lIdx));
                    found = true;
                    lIdx -= step;
                    num++;
                } else {
                    if (!found) break;
                    found = false;
                }
                larger = true;
            }
        }

        Collections.sort(exposures);
        numExposures = exposures.size();
    }

    private int nextPowerOfTwo(long value) {
        return 1 << (32 - Long.numberOfLeadingZeros(value - 1));
    }

    private int rangeSize() {
        return range.size();
    }

    public int getSize() {
        return numExposures;
    }

    public long getMiddleValue() {
        return range.get(rangeSize() / 2);
    }

    public long getValue(int idx) {
        return exposures.get(idx);
    }
}
