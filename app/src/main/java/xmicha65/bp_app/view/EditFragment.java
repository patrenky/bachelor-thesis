package xmicha65.bp_app.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import xmicha65.bp_app.R;
import xmicha65.bp_app.controller.tmo.TMOReinhard;
import xmicha65.bp_app.model.ImageHDR;

public class EditFragment extends Fragment implements View.OnClickListener {
    public static String ARG_HDR;
    private ImageHDR hdrImage;
    private ImageView imageView;

    // TMO default values
    private float defaultGamma = 2.2f;
    private float defaultIntensity = 0.0f;
    private float defaultLightAdapt = 0.0f;
    private float defaultColorAdapt = 0.0f;

    // TMO seekBars
    private SeekBar barGama;
    private SeekBar barIntensity;
    private SeekBar barLightAdapt;
    private SeekBar barColorAdapr;

    private int defaultBarGama = 60;
    private int defaultBarIntensity = 0;
    private int defaultBarLightAdapt = 0;
    private int defaultBarColorAdapr = 0;

    // TMO values
    private float gamma = defaultGamma;
    private float intensity = defaultIntensity;
    private float lightAdapt = defaultLightAdapt;
    private float colorAdapt = defaultColorAdapt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // restore arguments from main activity
        if (savedInstanceState != null) {
            hdrImage = (ImageHDR) savedInstanceState.getSerializable(ARG_HDR);
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
            hdrImage = (ImageHDR) args.getSerializable(ARG_HDR);
            displayResult();
        } else if (hdrImage != null) {
            // saved instance state defined during onCreateView
            displayResult();
        }
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        imageView = (ImageView) view.findViewById(R.id.edit_image);
        // buttons
        view.findViewById(R.id.edit_back).setOnClickListener(this);
        view.findViewById(R.id.edit_reset).setOnClickListener(this);
        view.findViewById(R.id.edit_save_hdr).setOnClickListener(this);
        view.findViewById(R.id.edit_save_jpg).setOnClickListener(this);
        // seekBars
        barGama = (SeekBar) view.findViewById(R.id.edit_bar0);
        barIntensity = (SeekBar) view.findViewById(R.id.edit_bar1);
        barLightAdapt = (SeekBar) view.findViewById(R.id.edit_bar2);
        barColorAdapr = (SeekBar) view.findViewById(R.id.edit_bar3);

        resetTmoValues();
        setSeekBarsListeners();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_back: {
                break;
            }
            case R.id.edit_reset: {
                resetTmoValues();
                displayResult();
                break;
            }
            case R.id.edit_save_hdr: {
                DialogFragment saveDialog = SaveDialog.newInstance(hdrImage);
                saveDialog.show(getActivity().getFragmentManager(), "saveHdrDialog");
                break;
            }
            case R.id.edit_save_jpg: {
                break;
            }
        }
    }

    public void setSeekBarsListeners() {
        barGama.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // range [1, 3]
                gamma = (float) seekBar.getProgress() / 50 + 1;
                System.out.println("#### gamma " + gamma);
                displayResult();
            }
        });
        barIntensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // range [-8, 8]
                intensity = (float) (seekBar.getProgress() - 50) / (50 / 8);
                System.out.println("#### intensity " + intensity);
                displayResult();
            }
        });
        barLightAdapt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // range [0, 1]
                lightAdapt = (float) seekBar.getProgress() / 100;
                System.out.println("#### lightAdapt " + lightAdapt);
                displayResult();
            }
        });
        barColorAdapr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // range [0, 1]
                colorAdapt = (float) seekBar.getProgress() / 100;
                System.out.println("#### colorAdapt " + colorAdapt);
                displayResult();
            }
        });
    }

    private void displayResult() {
        TMOReinhard tonemapper = new TMOReinhard(hdrImage, gamma, intensity, lightAdapt, colorAdapt);
        imageView.setImageBitmap(tonemapper.getImageBmp());
    }

    private void resetTmoValues() {
        gamma = defaultGamma;
        intensity = defaultIntensity;
        lightAdapt = defaultLightAdapt;
        colorAdapt = defaultColorAdapt;

        barGama.setProgress(defaultBarGama);
        barIntensity.setProgress(defaultBarIntensity);
        barLightAdapt.setProgress(defaultBarLightAdapt);
        barColorAdapr.setProgress(defaultBarColorAdapr);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current state in case we need to recreate the fragment
        outState.putSerializable(ARG_HDR, hdrImage);

    }
}
