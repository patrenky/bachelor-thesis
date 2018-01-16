package xmicha65.bp_app.CameraComponents;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 * Shows OK/Cancel confirmation dialog about camera permission.
 */
public class ConfirmationDialog extends DialogFragment {

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Fragment parent = getParentFragment();
        return new AlertDialog.Builder(getActivity())
                .setMessage("This sample needs camera permission.")
                .setPositiveButton(android.R.string.ok, (dialog, which) ->
                        parent.requestPermissions(
                                new String[]{Manifest.permission.CAMERA},
                                REQUEST_CAMERA_PERMISSION))
                .setNegativeButton(android.R.string.cancel,
                        (dialog, which) -> {
                            Activity activity = parent.getActivity();
                            if (activity != null) {
                                activity.finish();
                            }
                        })
                .create();
    }
}
