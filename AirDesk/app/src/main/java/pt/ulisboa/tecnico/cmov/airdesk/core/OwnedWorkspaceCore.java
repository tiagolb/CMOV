package pt.ulisboa.tecnico.cmov.airdesk.core;


import java.util.ArrayList;

public class OwnedWorkspaceCore extends WorkspaceCore {

    public static ArrayList<WorkspaceCore> workspaces = null;

    public OwnedWorkspaceCore(String name, int quota, String tags, String owner, boolean isPublic) {
        super(name, quota, tags, owner, isPublic);
    }

    public OwnedWorkspaceCore(String name, int quota, String owner, boolean isPublic) {
        super(name, quota, owner, isPublic);
    }

    //load the workspaces
    /*public static void loadWorkspaces(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String workspacesJSONString = prefs.getString("ownedWorkspaces", "");
        if (workspacesJSONString.equals(""))
            OwnedWorkspaceCore.workspaces = new ArrayList<>();
        else {
            Type type = new TypeToken<ArrayList<OwnedWorkspaceCore>>() {
            }.getType();
            OwnedWorkspaceCore.workspaces = new Gson().fromJson(workspacesJSONString, type);
        }
    }

    //store the workspaces
    public static void saveWorkspaces(Context context) {
        if (OwnedWorkspaceCore.workspaces != null) {
            Type type = new TypeToken<ArrayList<OwnedWorkspaceCore>>() {
            }.getType();
            String workspacesJSONString = new Gson().toJson(OwnedWorkspaceCore.workspaces, type);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            prefs.edit().putString("ownedWorkspaces", workspacesJSONString).apply();
        }
    }*/


    //get workspace by id
    /*public static WorkspaceCore getWorkspaceById(String id) {
        for (WorkspaceCore w : OwnedWorkspaceCore.workspaces)
            if (w.getId().equals(id)) return w;
        return null;
    }*/
}
