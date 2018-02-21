package xmicha65.bp_app.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.opencv.core.Mat;

import java.io.Serializable;

import xmicha65.bp_app.R;
import xmicha65.bp_app.controller.tmo.TMOReinhard;

public class EditFragment extends Fragment implements View.OnClickListener {
    public static String ARG_HDR;
    private Mat hdrImage;
    private TMOReinhard tonemapper;
    private ImageView iv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // restore arguments from main activity
        if (savedInstanceState != null) {
            hdrImage = (Mat) savedInstanceState.getSerializable(ARG_HDR);
        }
        return inflater.inflate(R.layout.activity_frag_edit, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // check if there are arguments passed to the fragment
        Bundle args = getArguments();
        if (args != null) {
            // arguments passed in
            hdrImage = (Mat) args.getSerializable(ARG_HDR);
            displayResult();
        } else if (hdrImage != null) {
            // saved instance state defined during onCreateView
            displayResult();
        }
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        view.findViewById(R.id.edit_back).setOnClickListener(this);
        view.findViewById(R.id.edit_save).setOnClickListener(this);
        view.findViewById(R.id.edit_bar0).setOnClickListener(this);
        view.findViewById(R.id.edit_bar1).setOnClickListener(this);
        view.findViewById(R.id.edit_bar2).setOnClickListener(this);
        iv = (ImageView) view.findViewById(R.id.edit_image);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_back: {
                break;
            }
            case R.id.edit_save: {
                break;
            }
            case R.id.edit_bar0: {
                break;
            }
            case R.id.edit_bar1: {
                break;
            }
            case R.id.edit_bar2: {
                break;
            }
        }
    }

    private void displayResult() {
        tonemapper = new TMOReinhard(hdrImage);
        iv.setImageBitmap(tonemapper.getImageBmp());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current state in case we need to recreate the fragment
        outState.putSerializable(ARG_HDR, (Serializable) hdrImage);
    }
}
