package xmicha65.bp_app.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.EditText;

import xmicha65.bp_app.model.ImageHDR;

public class SaveDialog extends DialogFragment {
    public static String ARG_HDR;
    private ImageHDR hdrImage;

    static SaveDialog newInstance(ImageHDR hdrImage) {
        SaveDialog dialog = new SaveDialog();

        // passing arguments
        Bundle args = new Bundle();
        args.putSerializable(ARG_HDR, hdrImage);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hdrImage = (ImageHDR) getArguments().getSerializable(ARG_HDR);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        EditText input = new EditText(getActivity());

        builder.setTitle("Save HDR file")
                .setView(input)
                .setPositiveButton("save", (dialog, id) -> {
                    hdrImage.save(input.getText().toString());
                })
                .setNegativeButton("cancel", (dialog, id) -> {
                    dialog.cancel();
                });

        return builder.create();
    }
}
