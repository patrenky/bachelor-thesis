package xmicha65.bp_app.view;

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
        view.findViewById(R.id.home_init_lampicka).setOnClickListener(this);
        view.findViewById(R.id.home_init_scene1).setOnClickListener(this);
        view.findViewById(R.id.home_init_scene3).setOnClickListener(this);
        view.findViewById(R.id.home_init_scene4).setOnClickListener(this);
        view.findViewById(R.id.home_init_scene5).setOnClickListener(this);
        view.findViewById(R.id.home_init_scene6).setOnClickListener(this);
        view.findViewById(R.id.home_init_scene7).setOnClickListener(this);
        view.findViewById(R.id.home_init_pohyb).setOnClickListener(this);
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
                break;
            }
            case R.id.home_init_lampicka: {
                ((Main) getActivity()).homeSelectinitLampicka();
                break;
            }
            case R.id.home_init_scene1: {
                ((Main) getActivity()).homeSelectinitScene("scena1");
                break;
            }
            case R.id.home_init_scene3: {
                ((Main) getActivity()).homeSelectinitScene("scena3");
                break;
            }
            case R.id.home_init_scene4: {
                ((Main) getActivity()).homeSelectinitScene("scena4");
                break;
            }
            case R.id.home_init_scene5: {
                ((Main) getActivity()).homeSelectinitScene("scena5");
                break;
            }
            case R.id.home_init_scene6: {
                ((Main) getActivity()).homeSelectinitScene("scena6");
                break;
            }
            case R.id.home_init_scene7: {
                ((Main) getActivity()).homeSelectinitScene("scena7");
                break;
            }
            case R.id.home_init_pohyb: {
                ((Main) getActivity()).homeSelectinitScene("scena5pohyb");
                break;
            }
        }
    }
}
