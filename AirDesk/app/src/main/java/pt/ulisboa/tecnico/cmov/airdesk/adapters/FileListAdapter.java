package pt.ulisboa.tecnico.cmov.airdesk.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;

/**
 * Created by dinis_000 on 29/03/2015.
 */
public class FileListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private int layoutResourceId;
    private HashSet<String> files;

    private static final String LOG_TAG = "WorkspaceAdapter";

    public FileListAdapter(Context context, int textViewResourceId, HashSet<String> files) {
        this.mInflater = LayoutInflater.from(context);
        this.layoutResourceId = textViewResourceId;
        this.files = files;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = mInflater.inflate(layoutResourceId, parent, false);
        } else {
            view = convertView;
        }

        String item = (String) getItem(position);
        TextView header = (TextView) view.findViewById(R.id.workspace_file_list_item);
        header.setText(item);
        return view;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return files.toArray()[position]; //FIXME: hashset has no order, it might not work
    }

    public int getCount() {
        return files.size();
    }
}
