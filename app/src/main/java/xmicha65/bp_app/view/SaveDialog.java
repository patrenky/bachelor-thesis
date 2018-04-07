package xmicha65.bp_app.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.EditText;

import xmicha65.bp_app.model.ImageHDR;
import xmicha65.bp_app.model.ImageType;

public class SaveDialog extends DialogFragment {
    public static String ARG_IMG = "ARG_IMG";
    public static String ARG_TYPE = "ARG_TYPE";
    private ImageHDR hdrImage;
    private ImageType imageType;

    static SaveDialog newInstance(ImageHDR hdrImage, ImageType imageType) {
        SaveDialog dialog = new SaveDialog();

        // passing arguments
        Bundle args = new Bundle();
        args.putSerializable(ARG_IMG, hdrImage);
        args.putSerializable(ARG_TYPE, imageType);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hdrImage = (ImageHDR) getArguments().getSerializable(ARG_IMG);
        imageType = (ImageType) getArguments().getSerializable(ARG_TYPE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        EditText input = new EditText(getActivity());

        builder.setTitle(imageType == ImageType.HDR ? "Save HDR file" : "Save JPEG file")
                .setView(input)
                .setPositiveButton("save", (dialog, id) -> {
                    hdrImage.save(input.getText().toString(), imageType);
                })
                .setNegativeButton("cancel", (dialog, id) -> {
                    dialog.cancel();
                });

        return builder.create();
    }
}
