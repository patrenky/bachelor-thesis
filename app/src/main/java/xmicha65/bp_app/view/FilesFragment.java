package xmicha65.bp_app.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xmicha65.bp_app.R;
import xmicha65.bp_app.controller.Storages;

public class FilesFragment extends Fragment {
    private List<String> fileList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);

        File path = Storages.getPublicImagesStorageDir("/hdr");
        listDirectory(path);

        ListView listView = (ListView) view.findViewById(R.id.files_list);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                fileList
        );

        listView.setAdapter(listViewAdapter);

        return view;
    }

    private void listDirectory(File path) {
        File[] files = path.listFiles();
        fileList.clear();
        for (File file : files) {
            fileList.add(file.getPath());
        }
    }
}
