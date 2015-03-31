package pt.ulisboa.tecnico.cmov.airdesk.core;

import java.util.ArrayList;

public class ForeignWorkspaceCore extends WorkspaceCore {

    public static ArrayList<WorkspaceCore> workspaces = null;

    private boolean isPublic;

    public ForeignWorkspaceCore(String name, int quota, String tags, String owner, boolean isPublic) {
        super(name, quota, tags, owner);
        this.isPublic = isPublic;
    }

    /*
    public static void loadWorkspaces(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String workspacesJSONString = prefs.getString("foreignWorkspaces", "");
        if (workspacesJSONString.equals(""))
            ForeignWorkspaceCore.workspaces = new ArrayList<WorkspaceCore>();
        else {
            Type type = new TypeToken<ArrayList<ForeignWorkspaceCore>>() {
            }.getType();
            ForeignWorkspaceCore.workspaces = new Gson().fromJson(workspacesJSONString, type);
        }
    }

    public static void saveWorkspaces(Context context) {
        if (ForeignWorkspaceCore.workspaces != null) {
            Type type = new TypeToken<ArrayList<ForeignWorkspaceCore>>() {
            }.getType();
            String workspacesJSONString = new Gson().toJson(ForeignWorkspaceCore.workspaces, type);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            prefs.edit().putString("foreignWorkspaces", workspacesJSONString).apply();
        }
    }
    */
}
