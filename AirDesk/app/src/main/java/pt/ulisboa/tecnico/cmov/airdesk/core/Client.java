package pt.ulisboa.tecnico.cmov.airdesk.core;

import android.content.Context;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.AirDeskContext;

/**
 * Created by dinis_000 on 30/03/2015.
 */
public class Client {
    public static ArrayList<String> mountedWorkspaces;

    public static ArrayList<String> getMountedWorkspaces(Context context) {
        ArrayList<WorkspaceCore> workspaces = (ArrayList) ((AirDeskContext) context).getWorkspaces();
        mountedWorkspaces = new ArrayList<String>();
        WorkspaceCore workspace = workspaces.get(0);
        if (workspace != null) mountedWorkspaces.add(workspace.getName());
        return mountedWorkspaces;
    }

}
