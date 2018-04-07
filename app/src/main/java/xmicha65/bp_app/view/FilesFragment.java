package xmicha65.bp_app.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xmicha65.bp_app.Main;
import xmicha65.bp_app.R;
import xmicha65.bp_app.controller.Storages;
import xmicha65.bp_app.model.ImageHDR;

/**
 * Screen for listing .hdr files from public images directory
 * Implements selecting .hdr content for tonemap on file item click
 */
public class FilesFragment extends Fragment implements View.OnClickListener {
    private List<FileItem> fileList = new ArrayList<>();

    /**
     * List Row item object
     */
    private class FileItem {
        private String path;
        private String title;

        FileItem(String filePath, String fileName) {
            this.path = filePath;
            this.title = fileName;
        }

        String getPath() {
            return this.path;
        }

        String getTitle() {
            return title.replace("/", "");
        }
    }

    /**
     * Customized List Row view
     */
    private class FileAdapter extends BaseAdapter {
        private List<FileItem> data;
        private LayoutInflater inflater = null;

        FileAdapter(Context context, List<FileItem> data) {
            this.data = data;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.list_row, null);
            TextView text = (TextView) vi.findViewById(R.id.file_text);
            text.setText(data.get(position).getTitle());
            return vi;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);

        // get all .hdr files
        File path = Storages.getPublicImagesStorageDir("/hdr");
        listDirectory(path);

        ListView listView = (ListView) view.findViewById(R.id.files_list);

        // set custom rows with file data
        listView.setAdapter(new FileAdapter(getContext(), fileList));

        // on row click listener, open .hdr file for tonemap
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            ((Main) getActivity()).tonemapOperators(new ImageHDR(fileList.get(i).getPath()));
        });

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        // back to home button listener
        view.findViewById(R.id.files_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.files_back: {
                ((Main) getActivity()).goHome();
                break;
            }
        }
    }

    /**
     * List all .hdr files from public images directory
     */
    private void listDirectory(File path) {
        File[] files = path.listFiles();
        fileList.clear();
        for (File file : files) {
            String filePath = file.getPath();
            if (filePath.endsWith(".hdr")) {
                fileList.add(new FileItem(filePath, filePath.replace(path.toString(), "")));
            }
        }
    }
}
