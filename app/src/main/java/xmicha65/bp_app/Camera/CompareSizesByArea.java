package xmicha65.bp_app.Camera;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Size;

import java.util.Comparator;

/**
 * Compares two {@code Size}s based on their areas.
 * @author https://github.com/googlesamples
 */
public class CompareSizesByArea implements Comparator<Size> {
    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int compare(Size lhs, Size rhs) {
        return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                (long) rhs.getWidth() * rhs.getHeight());
    }
}