package xmicha65.bp_app.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import xmicha65.bp_app.Main;
import xmicha65.bp_app.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private List<ImageView> hints = new ArrayList<>();
    private boolean hintsVisibility = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        view.findViewById(R.id.home_capture).setOnClickListener(this);
        view.findViewById(R.id.home_load).setOnClickListener(this);
        view.findViewById(R.id.home_settings).setOnClickListener(this);
        view.findViewById(R.id.home_hint).setOnClickListener(this);

        hints.add((ImageView) view.findViewById(R.id.home_hint_0));
        hints.add((ImageView) view.findViewById(R.id.home_hint_1));
        hints.add((ImageView) view.findViewById(R.id.home_hint_2));

        setHintsVisibility(hintsVisibility);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_capture: {
                ((Main) getActivity()).homeSelectCapture();
                break;
            }
            case R.id.home_load: {
                ((Main) getActivity()).homeSelectLoadHdr();
                break;
            }
            case R.id.home_settings: {
                DialogFragment settingsDialog = new SettingsDialog();
                settingsDialog.show(getActivity().getFragmentManager(), "settingsDialog");
                break;
            }
            case R.id.home_hint: {
                setHintsVisibility(!hintsVisibility);
                break;
            }
        }
    }

    /**
     * Hints visibility
     */
    private void setHintsVisibility(boolean visible) {
        hintsVisibility = visible;
        for (ImageView hint : hints) {
            hint.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
}
