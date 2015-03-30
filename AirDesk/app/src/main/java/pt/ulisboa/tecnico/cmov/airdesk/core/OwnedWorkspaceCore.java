package pt.ulisboa.tecnico.cmov.airdesk.core;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by tiago on 26-03-2015.
 */
public class OwnedWorkspaceCore extends WorkspaceCore {

    public static ArrayList<WorkspaceCore> workspaces = null;

    private boolean isPublic;

    public OwnedWorkspaceCore(String name, int quota, String tag, String owner_email, boolean isPublic) {
        super(name, quota, tag, owner_email);
        this.isPublic = isPublic;
    }

    public static void loadWorkspaces(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String workspacesJSONString = prefs.getString("ownedWorkspaces", "");
        if (workspacesJSONString.equals("")) OwnedWorkspaceCore.workspaces = new ArrayList<WorkspaceCore>();
        else {
            Type type = new TypeToken<ArrayList<OwnedWorkspaceCore>>(){}.getType();
            OwnedWorkspaceCore.workspaces = new Gson().fromJson(workspacesJSONString, type);
        }
    }

    public static void saveWorkspaces(Context context) {
        if (OwnedWorkspaceCore.workspaces != null) {
            Type type = new TypeToken<ArrayList<OwnedWorkspaceCore>>(){}.getType();
            String workspacesJSONString = new Gson().toJson(OwnedWorkspaceCore.workspaces, type);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            prefs.edit().putString("ownedWorkspaces", workspacesJSONString).apply();
        }
    }
}
