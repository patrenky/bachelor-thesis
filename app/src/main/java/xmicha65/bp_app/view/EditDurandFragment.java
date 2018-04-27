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
import xmicha65.bp_app.controller.tmo.TMODurand;
import xmicha65.bp_app.model.ImageHDR;
import xmicha65.bp_app.model.ImageType;
import xmicha65.bp_app.model.TmoParams;

/**
 * Screen: control params of Durand local TMO
 */
public class EditDurandFragment extends Fragment implements View.OnClickListener {
    public static String ARG_HDR = "ARG_HDR";

    private ImageHDR hdrImage;
    private ImageView imageView;
    private TMODurand tonemapper;

    // seekBars
    private SeekBar barGama;
    private SeekBar barContrast;
    private SeekBar barSaturation;
    private SeekBar barSigmaSpace;
    private SeekBar barSigmaColor;

    // actual values
    private float gamma;
    private float contrast;
    private float saturation;
    private float sigmaSpace;
    private float sigmaColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // restore arguments from main activity
        if (savedInstanceState != null) {
            hdrImage = (ImageHDR) savedInstanceState.getSerializable(ARG_HDR);
        }
        return inflater.inflate(R.layout.fragment_edit_durand, container, false);
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
        imageView = (ImageView) view.findViewById(R.id.durand_image);
        // buttons
        view.findViewById(R.id.durand_back).setOnClickListener(this);
        view.findViewById(R.id.durand_reset).setOnClickListener(this);
        view.findViewById(R.id.durand_rotate).setOnClickListener(this);
        view.findViewById(R.id.durand_save_hdr).setOnClickListener(this);
        view.findViewById(R.id.durand_save_jpg).setOnClickListener(this);
        // seekBars
        barGama = (SeekBar) view.findViewById(R.id.durand_bar0);
        barContrast = (SeekBar) view.findViewById(R.id.durand_bar1);
        barSaturation = (SeekBar) view.findViewById(R.id.durand_bar2);
        barSigmaSpace = (SeekBar) view.findViewById(R.id.durand_bar3);
        barSigmaColor = (SeekBar) view.findViewById(R.id.durand_bar4);

        resetTmoValues();
        setSeekBarsListeners();
        initRotateView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.durand_back: {
                ((Main) getActivity()).tonemapOperators(hdrImage);
                break;
            }
            case R.id.durand_reset: {
                resetTmoValues();
                displayResult();
                break;
            }
            case R.id.durand_rotate: {
                rotateView();
                break;
            }
            case R.id.durand_save_hdr: {
                DialogFragment saveDialog = SaveDialog.newInstance(hdrImage, ImageType.HDR);
                saveDialog.show(getActivity().getFragmentManager(), "saveHdrDialog");
                break;
            }
            case R.id.durand_save_jpg: {
                // save original size result
                tonemapper = new TMODurand(hdrImage.getMatHdrImg(), gamma, contrast, saturation, sigmaSpace, sigmaColor);
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
        barContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                contrast = TmoParams.getProgressValue(TmoParams.dContrast, progress);
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
        barSigmaSpace.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                sigmaSpace = TmoParams.getProgressValue(TmoParams.dSigmaSpace, progress);
                displayResult();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        barSigmaColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                sigmaColor = TmoParams.getProgressValue(TmoParams.dSigmaColor, progress);
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
        tonemapper = new TMODurand(hdrImage.getMatHdrTemp(), gamma, contrast, saturation, sigmaSpace, sigmaColor);
        imageView.setImageBitmap(tonemapper.getImageBmp());
    }

    /**
     * Reset to default tmo params
     */
    private void resetTmoValues() {
        gamma = TmoParams.getDefaultValue(TmoParams.gama);
        contrast = TmoParams.getDefaultValue(TmoParams.dContrast);
        saturation = TmoParams.getDefaultValue(TmoParams.saturation);
        sigmaSpace = TmoParams.getDefaultValue(TmoParams.dSigmaSpace);
        sigmaColor = TmoParams.getDefaultValue(TmoParams.dSigmaColor);

        barGama.setProgress(TmoParams.getDefaultProgressValue(TmoParams.gama));
        barContrast.setProgress(TmoParams.getDefaultProgressValue(TmoParams.dContrast));
        barSaturation.setProgress(TmoParams.getDefaultProgressValue(TmoParams.saturation));
        barSigmaSpace.setProgress(TmoParams.getDefaultProgressValue(TmoParams.dSigmaSpace));
        barSigmaColor.setProgress(TmoParams.getDefaultProgressValue(TmoParams.dSigmaColor));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current state in case we need to recreate the fragment
        outState.putSerializable(ARG_HDR, hdrImage);
    }
}
