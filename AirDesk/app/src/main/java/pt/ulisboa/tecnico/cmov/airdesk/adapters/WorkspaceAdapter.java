package pt.ulisboa.tecnico.cmov.airdesk.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;

/**
 * Created by dinis_000 on 29/03/2015.
 */
public class WorkspaceAdapter extends ArrayAdapter<WorkspaceCore> {


    private int layoutResourceId;
    private ArrayList<WorkspaceCore> workspaces;

    private static final String LOG_TAG = "WorkspaceAdapter";

    public WorkspaceAdapter(Context context, int textViewResourceId, ArrayList<WorkspaceCore> workspaces) {
        super(context, textViewResourceId, workspaces);
        this.layoutResourceId = textViewResourceId;
        this.workspaces = workspaces;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            WorkspaceCore item = getItem(position);
            View v = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(layoutResourceId, null);
                //v.setLongClickable(true);
                //v.setClickable(true);
            } else {
                v = convertView;
            }

            TextView header = (TextView) v.findViewById(R.id.workspace_list_item);
            header.setText(item.getName());
            //description.setText(item.getValue());

            return v;
        } catch (Exception e) {
            Log.e(LOG_TAG, "error", e);
            return null;
        }
    }

}
