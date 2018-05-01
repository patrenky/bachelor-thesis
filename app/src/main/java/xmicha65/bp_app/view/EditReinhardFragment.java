package xmicha65.bp_app.view;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import xmicha65.bp_app.Main;
import xmicha65.bp_app.R;
import xmicha65.bp_app.controller.tmo.TMOReinhard;
import xmicha65.bp_app.model.ImageHDR;
import xmicha65.bp_app.model.ImageType;
import xmicha65.bp_app.model.TmoParams;

/**
 * Screen: control params of Reinhard global TMO
 */
public class EditReinhardFragment extends Fragment implements View.OnClickListener {
    /**
     * Class for tonemap original size image and async affecting view
     */
    private class SaveJpeg extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            tonemapper = new TMOReinhard(hdrImage.getMatHdrImg(), gamma, intensity, lightAdapt, colorAdapt);
            DialogFragment saveDialog = SaveDialog.newInstance(
                    new ImageHDR(tonemapper.getImageBmp()), ImageType.LDR);
            saveDialog.show(getActivity().getFragmentManager(), "saveLdrDialog");
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            ((Main) getActivity()).hideProgress();
        }

        @Override
        protected void onPreExecute() {
            ((Main) getActivity()).showProgress();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public static String ARG_HDR = "ARG_HDR";

    private ImageHDR hdrImage;
    private ImageView imageView;
    private TMOReinhard tonemapper;

    // seekBars
    private SeekBar barGama;
    private SeekBar barIntensity;
    private SeekBar barLightAdapt;
    private SeekBar barColorAdapr;

    // actual values
    private float gamma;
    private float intensity;
    private float lightAdapt;
    private float colorAdapt;

    private ImageView hint;
    private boolean hintVisibility = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // restore arguments from main activity
        if (savedInstanceState != null) {
            hdrImage = (ImageHDR) savedInstanceState.getSerializable(ARG_HDR);
        }
        return inflater.inflate(R.layout.fragment_edit_reinhard, container, false);
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
        imageView = (ImageView) view.findViewById(R.id.reinhard_image);
        // buttons
        view.findViewById(R.id.reinhard_back).setOnClickListener(this);
        view.findViewById(R.id.reinhard_hint).setOnClickListener(this);
        view.findViewById(R.id.reinhard_reset).setOnClickListener(this);
        view.findViewById(R.id.reinhard_rotate).setOnClickListener(this);
        view.findViewById(R.id.reinhard_save_hdr).setOnClickListener(this);
        view.findViewById(R.id.reinhard_save_jpg).setOnClickListener(this);
        // seekBars
        barGama = (SeekBar) view.findViewById(R.id.reinhard_bar0);
        barIntensity = (SeekBar) view.findViewById(R.id.reinhard_bar1);
        barLightAdapt = (SeekBar) view.findViewById(R.id.reinhard_bar2);
        barColorAdapr = (SeekBar) view.findViewById(R.id.reinhard_bar3);

        resetTmoValues();
        setSeekBarsListeners();
        initRotateView();

        hint = (ImageView) view.findViewById(R.id.reinhard_hint0);
        hint.setVisibility(View.GONE);
        hintVisibility = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reinhard_back: {
                ((Main) getActivity()).tonemapOperators(hdrImage);
                break;
            }
            case R.id.reinhard_hint: {
                hintVisibility = !hintVisibility;
                hint.setVisibility(hintVisibility ? View.VISIBLE : View.GONE);
                break;
            }
            case R.id.reinhard_reset: {
                resetTmoValues();
                displayResult();
                break;
            }
            case R.id.reinhard_rotate: {
                rotateView();
                break;
            }
            case R.id.reinhard_save_hdr: {
                DialogFragment saveDialog = SaveDialog.newInstance(hdrImage, ImageType.HDR);
                saveDialog.show(getActivity().getFragmentManager(), "saveHdrDialog");
                break;
            }
            case R.id.reinhard_save_jpg: {
                new SaveJpeg().execute("");
                break;
            }
        }
    }

    /**
     * Progress bars listeners
     */
    public void setSeekBarsListeners() {
        barGama.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                gamma = TmoParams.getProgressValue(TmoParams.gamma, progress);
                displayResult();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        barIntensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                intensity = TmoParams.getProgressValue(TmoParams.rIntensity, progress);
                displayResult();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        barLightAdapt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                lightAdapt = TmoParams.getProgressValue(TmoParams.rLightAdapt, progress);
                displayResult();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        barColorAdapr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                colorAdapt = TmoParams.getProgressValue(TmoParams.rColorAdapt, progress);
                displayResult();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * Rotate image view
     */
    private void initRotateView() {
        imageView.setRotation(((Main) getActivity()).getViewRotation());
    }

    private void rotateView() {
        int rotation = ((Main) getActivity()).getViewRotation() + 90;
        imageView.setRotation(rotation);
        ((Main) getActivity()).setViewRotation(rotation);
    }

    /**
     * Tonemap and diplay result
     */
    private void displayResult() {
        tonemapper = new TMOReinhard(hdrImage.getMatHdrTemp(), gamma, intensity, lightAdapt, colorAdapt);
        imageView.setImageBitmap(tonemapper.getImageBmp());
    }

    /**
     * Reset to default tmo params
     */
    private void resetTmoValues() {
        gamma = TmoParams.getDefaultValue(TmoParams.gamma);
        intensity = TmoParams.getDefaultValue(TmoParams.rIntensity);
        lightAdapt = TmoParams.getDefaultValue(TmoParams.rLightAdapt);
        colorAdapt = TmoParams.getDefaultValue(TmoParams.rColorAdapt);

        barGama.setProgress(TmoParams.getDefaultProgressValue(TmoParams.gamma));
        barIntensity.setProgress(TmoParams.getDefaultProgressValue(TmoParams.rIntensity));
        barLightAdapt.setProgress(TmoParams.getDefaultProgressValue(TmoParams.rLightAdapt));
        barColorAdapr.setProgress(TmoParams.getDefaultProgressValue(TmoParams.rColorAdapt));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current state in case we need to recreate the fragment
        outState.putSerializable(ARG_HDR, hdrImage);
    }
}
