package pt.ulisboa.tecnico.cmov.airdesk.core;

import android.content.Context;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.AirDeskContext;

/**
 * Created by dinis_000 on 30/03/2015.
 */
//TODO: we should use ForeignWorkspaceCore, so we can override some methods to use remote retrievals instead
public class Client {
    public static ArrayList<WorkspaceCore> mountedWorkspaces;

    public static ArrayList<WorkspaceCore> getMountedWorkspaces(Context context) {
        //for now we return local workspaces
        ArrayList<WorkspaceCore> workspaces = (ArrayList) ((AirDeskContext) context).getWorkspaces();
        return mountedWorkspaces = workspaces;
    }

    public static WorkspaceCore getWorkspace(String owner, String workspace) {
        //for now we get it locally
        return Server.getWorkspace("", workspace);
    }
}
