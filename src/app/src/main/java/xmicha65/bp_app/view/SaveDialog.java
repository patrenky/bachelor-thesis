package xmicha65.bp_app.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import xmicha65.bp_app.R;
import xmicha65.bp_app.model.ImageHDR;
import xmicha65.bp_app.model.ImageType;

/**
 * Dialog for edit name of storing content
 * @author xmicha65
 */
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

        // content layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_save, null);

        // dialog title
        TextView title = (TextView) layout.findViewById(R.id.save_title);
        title.setText(imageType == ImageType.HDR ? R.string.save_hdr : R.string.save_jpeg);

        // text input
        EditText input = (EditText) layout.findViewById(R.id.save_input);

        builder.setView(layout)
                .setPositiveButton("save", (dialog, id) -> {
                    hdrImage.save(input.getText().toString(), imageType);
                })
                .setNegativeButton("cancel", (dialog, id) -> {
                    dialog.cancel();
                });

        return builder.create();
    }
}
