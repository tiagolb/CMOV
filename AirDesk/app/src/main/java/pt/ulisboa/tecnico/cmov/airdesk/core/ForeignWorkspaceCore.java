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
public class ForeignWorkspaceCore extends WorkspaceCore {

    public static ArrayList<ForeignWorkspaceCore> workspaces = null;

    private boolean isPublic;

    public ForeignWorkspaceCore(String name, int quota, String tag, String owner_email, boolean isPublic) {
        super(name, quota, tag, owner_email);
        this.isPublic = isPublic;
    }

    public static void loadWorkspaces(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String workspacesJSONString = prefs.getString("foreignWorkspaces", "");
        if (workspacesJSONString.equals("")) ForeignWorkspaceCore.workspaces = new ArrayList<ForeignWorkspaceCore>();
        else {
            Type type = new TypeToken<ArrayList<ForeignWorkspaceCore>>(){}.getType();
            ForeignWorkspaceCore.workspaces = new Gson().fromJson(workspacesJSONString, type);
        }
    }

    public static void saveWorkspaces(Context context) {
        if (ForeignWorkspaceCore.workspaces != null) {
            Type type = new TypeToken<ArrayList<ForeignWorkspaceCore>>(){}.getType();
            String workspacesJSONString = new Gson().toJson(ForeignWorkspaceCore.workspaces, type);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            prefs.edit().putString("foreignWorkspaces", workspacesJSONString).apply();
        }
    }
}