package pt.ulisboa.tecnico.cmov.airdesk.core;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.AirDeskContext;

/**
 * Created by dinis_000 on 30/03/2015.
 */
public class Server {
    public static AirDeskContext context;

    private static boolean hasAccess(String client, WorkspaceCore workspace) {
        //TODO: verify if client belong to clients
        return true;
    }

    public static WorkspaceCore getWorkspace(String client, String workspace) {
        ArrayList<WorkspaceCore> workspaces = (ArrayList) context.getWorkspaces();
        for (WorkspaceCore w : workspaces)
            if (w.getName().equals(workspace)) return hasAccess(client, w) ? w : null;
        return null;
    }

    public static boolean removeFile(String client, String workspace, String file) {
        WorkspaceCore w = getWorkspace(client, workspace);
        if (workspace != null) {
            w.removeFile(file);
            return true;
        }
        return false;
    }
}
