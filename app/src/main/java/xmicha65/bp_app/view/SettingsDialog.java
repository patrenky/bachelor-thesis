package xmicha65.bp_app.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;

import xmicha65.bp_app.Main;
import xmicha65.bp_app.R;

public class SettingsDialog extends DialogFragment {
    private Integer[] optsExposures = {3, 4, 5, 6};
    private Integer[] optsStep = {1, 2};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // content layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_settings, null);

        // exposures dropdown
        Spinner spExposures = (Spinner) layout.findViewById(R.id.settings_spExposures);
        ArrayAdapter<Integer> adapterE = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, optsExposures);
        spExposures.setAdapter(adapterE);
        spExposures.setSelection(Arrays.asList(optsExposures).indexOf(((Main) getActivity()).getCaptureExposures()));

        // steps dropdown
        Spinner spStep = (Spinner) layout.findViewById(R.id.settings_spStep);
        ArrayAdapter<Integer> adapterS = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, optsStep);
        spStep.setAdapter(adapterS);
        spStep.setSelection(Arrays.asList(optsStep).indexOf(((Main) getActivity()).getCaptureStep()));

        // build
        builder.setView(layout)
                .setPositiveButton("save", (dialog, id) -> {
                    ((Main) getActivity()).setCaptureExposures((int) spExposures.getSelectedItem());
                    ((Main) getActivity()).setCaptureStep((int) spStep.getSelectedItem());
                })
                .setNegativeButton("cancel", (dialog, id) -> {
                    dialog.cancel();
                });

        return builder.create();
    }
}
