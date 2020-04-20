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
import xmicha65.bp_app.controller.tmo.TMODrago;
import xmicha65.bp_app.model.ImageHDR;
import xmicha65.bp_app.model.ImageType;
import xmicha65.bp_app.model.TmoParams;

/**
 * Screen: control params of Drago global TMO
 * @author xmicha65
 */
public class EditDragoFragment extends Fragment implements View.OnClickListener {
    /**
     * Class for tonemap original size image and async affecting view
     */
    private class SaveJpeg extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            tonemapper = new TMODrago(hdrImage.getMatHdrImg(), gamma, saturation, bias);
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
    private TMODrago tonemapper;

    // seekBars
    private SeekBar barGama;
    private SeekBar barSaturation;
    private SeekBar barBias;

    // actual values
    private float gamma;
    private float saturation;
    private float bias;

    private ImageView hint;
    private boolean hintVisibility = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // restore arguments from main activity
        if (savedInstanceState != null) {
            hdrImage = (ImageHDR) savedInstanceState.getSerializable(ARG_HDR);
        }
        return inflater.inflate(R.layout.fragment_edit_drago, container, false);
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
        imageView = (ImageView) view.findViewById(R.id.drago_image);
        // buttons
        view.findViewById(R.id.drago_back).setOnClickListener(this);
        view.findViewById(R.id.drago_hint).setOnClickListener(this);
        view.findViewById(R.id.drago_reset).setOnClickListener(this);
        view.findViewById(R.id.drago_rotate).setOnClickListener(this);
        view.findViewById(R.id.drago_save_hdr).setOnClickListener(this);
        view.findViewById(R.id.drago_save_jpg).setOnClickListener(this);
        // seekBars
        barGama = (SeekBar) view.findViewById(R.id.drago_bar0);
        barSaturation = (SeekBar) view.findViewById(R.id.drago_bar1);
        barBias = (SeekBar) view.findViewById(R.id.drago_bar2);

        resetTmoValues();
        setSeekBarsListeners();
        initRotateView();

        hint = (ImageView) view.findViewById(R.id.drago_hint0);
        hint.setVisibility(View.GONE);
        hintVisibility = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drago_back: {
                ((Main) getActivity()).tonemapOperators(hdrImage);
                break;
            }
            case R.id.drago_hint: {
                hintVisibility = !hintVisibility;
                hint.setVisibility(hintVisibility ? View.VISIBLE : View.GONE);
                break;
            }
            case R.id.drago_reset: {
                resetTmoValues();
                displayResult();
                break;
            }
            case R.id.drago_rotate: {
                rotateView();
                break;
            }
            case R.id.drago_save_hdr: {
                DialogFragment saveDialog = SaveDialog.newInstance(hdrImage, ImageType.HDR);
                saveDialog.show(getActivity().getFragmentManager(), "saveHdrDialog");
                break;
            }
            case R.id.drago_save_jpg: {
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
        barSaturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                saturation = TmoParams.getProgressValue(TmoParams.saturation, progress);
                displayResult();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        barBias.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                bias = TmoParams.getProgressValue(TmoParams.drBias, progress);
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
        tonemapper = new TMODrago(hdrImage.getMatHdrTemp(), gamma, saturation, bias);
        imageView.setImageBitmap(tonemapper.getImageBmp());
    }

    /**
     * Reset to default tmo params
     */
    private void resetTmoValues() {
        gamma = TmoParams.getDefaultValue(TmoParams.gamma);
        saturation = TmoParams.getDefaultValue(TmoParams.saturation);
        bias = TmoParams.getDefaultValue(TmoParams.drBias);

        barGama.setProgress(TmoParams.getDefaultProgressValue(TmoParams.gamma));
        barSaturation.setProgress(TmoParams.getDefaultProgressValue(TmoParams.saturation));
        barBias.setProgress(TmoParams.getDefaultProgressValue(TmoParams.drBias));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current state in case we need to recreate the fragment
        outState.putSerializable(ARG_HDR, hdrImage);
    }
}
