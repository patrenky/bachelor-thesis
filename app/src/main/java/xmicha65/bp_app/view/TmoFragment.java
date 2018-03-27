package xmicha65.bp_app.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import xmicha65.bp_app.Main;
import xmicha65.bp_app.R;
import xmicha65.bp_app.controller.tmo.TMODrago;
import xmicha65.bp_app.controller.tmo.TMODurand;
import xmicha65.bp_app.controller.tmo.TMOMantiuk;
import xmicha65.bp_app.controller.tmo.TMOReinhardGlobal;
import xmicha65.bp_app.model.ImageHDR;
import xmicha65.bp_app.model.TmoParams;

public class TmoFragment extends Fragment implements View.OnClickListener {
    public static String ARG_HDR = "ARG_HDR";
    private ImageHDR hdrImage;
    private int rotation = 0;

    private ImageView imageView0; // Mantiuk
    private ImageView imageView1; // Reinhard
    private ImageView imageView2; // Durand
    private ImageView imageView3; // Drago

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
                ((Main) getActivity()).tonemapMantiuk(hdrImage);
                break;
            }
            case R.id.tmo_image1: {
                ((Main) getActivity()).tonemapReinhard(hdrImage);
                break;
            }
            case R.id.tmo_image2: {
                ((Main) getActivity()).tonemapDurand(hdrImage);
                break;
            }
            case R.id.tmo_image3: {
                ((Main) getActivity()).tonemapDrago(hdrImage);
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
        TMOMantiuk tmoMantiuk = new TMOMantiuk(
                hdrImage.getMatHdrTemp(),
                TmoParams.getDefaultValue(TmoParams.gama),
                TmoParams.getDefaultValue(TmoParams.mScale),
                TmoParams.getDefaultValue(TmoParams.saturation)
        );
        imageView0.setImageBitmap(tmoMantiuk.getImageBmp());

        TMOReinhardGlobal tmoReinhard = new TMOReinhardGlobal(
                hdrImage.getMatHdrTemp(),
                TmoParams.getDefaultValue(TmoParams.gama),
                TmoParams.getDefaultValue(TmoParams.rIntensity),
                TmoParams.getDefaultValue(TmoParams.rLightAdapt),
                TmoParams.getDefaultValue(TmoParams.rColorAdapt)
        );
        imageView1.setImageBitmap(tmoReinhard.getImageBmp());

        TMODurand tmoDurand = new TMODurand(
                hdrImage.getMatHdrTemp(),
                TmoParams.getDefaultValue(TmoParams.gama),
                TmoParams.getDefaultValue(TmoParams.dContrast),
                TmoParams.getDefaultValue(TmoParams.saturation),
                TmoParams.getDefaultValue(TmoParams.dSigmaSpace),
                TmoParams.getDefaultValue(TmoParams.dSigmaColor)
        );
        imageView2.setImageBitmap(tmoDurand.getImageBmp());

        TMODrago tmoDrago = new TMODrago(
                hdrImage.getMatHdrTemp(),
                TmoParams.getDefaultValue(TmoParams.gama),
                TmoParams.getDefaultValue(TmoParams.saturation),
                TmoParams.getDefaultValue(TmoParams.dBias)
        );
        imageView3.setImageBitmap(tmoDrago.getImageBmp());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current state in case we need to recreate the fragment
        outState.putSerializable(ARG_HDR, hdrImage);

    }
}
