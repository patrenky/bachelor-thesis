package xmicha65.bp_app.view;

import android.app.DialogFragment;
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
 */
public class EditDragoFragment extends Fragment implements View.OnClickListener {
    public static String ARG_HDR = "ARG_HDR";
    private ImageHDR hdrImage;
    private ImageView imageView;
    private TMODrago tonemapper;
    private int rotation = 0;

    // seekBars
    private SeekBar barGama;
    private SeekBar barSaturation;
    private SeekBar barBias;

    // actual values
    private float gamma;
    private float saturation;
    private float bias;

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drago_back: {
                ((Main) getActivity()).tonemapOperators(hdrImage);
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
                // save original size result
                tonemapper = new TMODrago(hdrImage.getMatHdrImg(), gamma, saturation, bias);
                DialogFragment saveDialog = SaveDialog.newInstance(
                        new ImageHDR(tonemapper.getImageBmp()), ImageType.LDR);
                saveDialog.show(getActivity().getFragmentManager(), "saveLdrDialog");
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
                gamma = TmoParams.getProgressValue(TmoParams.gama, progress);
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
                bias = TmoParams.getProgressValue(TmoParams.dBias, progress);
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
    private void rotateView() {
        rotation += 90;
        imageView.setRotation(rotation);
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
        gamma = TmoParams.getDefaultValue(TmoParams.gama);
        saturation = TmoParams.getDefaultValue(TmoParams.saturation);
        bias = TmoParams.getDefaultValue(TmoParams.dBias);

        barGama.setProgress(TmoParams.getDefaultProgressValue(TmoParams.gama));
        barSaturation.setProgress(TmoParams.getDefaultProgressValue(TmoParams.saturation));
        barBias.setProgress(TmoParams.getDefaultProgressValue(TmoParams.dBias));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current state in case we need to recreate the fragment
        outState.putSerializable(ARG_HDR, hdrImage);

    }
}
