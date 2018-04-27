package xmicha65.bp_app.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xmicha65.bp_app.Main;
import xmicha65.bp_app.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
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
        }
    }
}
