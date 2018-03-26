package xmicha65.bp_app.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import xmicha65.bp_app.Main;
import xmicha65.bp_app.R;
import xmicha65.bp_app.controller.tmo.TMOReinhard;
import xmicha65.bp_app.model.ImageHDR;
import xmicha65.bp_app.model.TmoParams;

public class TmoFragment extends Fragment implements View.OnClickListener {
    public static String ARG_HDR = "ARG_HDR";
    private ImageHDR hdrImage;
    private int rotation = 0;

    private ImageView imageView0;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;

    private TMOReinhard tmoReinhard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // restore arguments from main activity
        if (savedInstanceState != null) {
            hdrImage = (ImageHDR) savedInstanceState.getSerializable(ARG_HDR);
        }
        return inflater.inflate(R.layout.fragment_tmo, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // check if there are arguments passed to the fragment
        Bundle args = getArguments();
        if (args != null) {
            // arguments passed in
            hdrImage = (ImageHDR) args.getSerializable(ARG_HDR);
            displayResults();
        } else if (hdrImage != null) {
            // saved instance state defined during onCreateView
            displayResults();
        }
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        imageView0 = (ImageView) view.findViewById(R.id.tmo_image0);
        imageView1 = (ImageView) view.findViewById(R.id.tmo_image1);
        imageView2 = (ImageView) view.findViewById(R.id.tmo_image2);
        imageView3 = (ImageView) view.findViewById(R.id.tmo_image3);

        view.findViewById(R.id.tmo_back).setOnClickListener(this);
        view.findViewById(R.id.tmo_rotate).setOnClickListener(this);

        view.findViewById(R.id.tmo_image0).setOnClickListener(this);
        view.findViewById(R.id.tmo_image1).setOnClickListener(this);
        view.findViewById(R.id.tmo_image2).setOnClickListener(this);
        view.findViewById(R.id.tmo_image3).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tmo_back: {
                ((Main) getActivity()).goHome();
                break;
            }
            case R.id.tmo_rotate: {
//                rotateView();
                break;
            }
            case R.id.tmo_image0: {
                ((Main) getActivity()).tonemapReinhard(hdrImage);
                break;
            }
        }
    }

    /**
     * Rotate image views
     */
    private void rotateView() {
        rotation += 90;
        imageView0.setRotation(rotation);
        imageView1.setRotation(rotation);
        imageView2.setRotation(rotation);
        imageView3.setRotation(rotation);
    }

    /**
     * Tonemap and diplay results
     */
    private void displayResults() {
        tmoReinhard = new TMOReinhard(
                hdrImage.getMatHdrTemp(),
                TmoParams.getDefaultValue(TmoParams.rGama),
                TmoParams.getDefaultValue(TmoParams.rIntensity),
                TmoParams.getDefaultValue(TmoParams.rLightAdapt),
                TmoParams.getDefaultValue(TmoParams.rColorAdapt)
        );
        imageView0.setImageBitmap(tmoReinhard.getImageBmp());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current state in case we need to recreate the fragment
        outState.putSerializable(ARG_HDR, hdrImage);

    }
}
