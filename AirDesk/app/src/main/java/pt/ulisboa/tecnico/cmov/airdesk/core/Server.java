package pt.ulisboa.tecnico.cmov.airdesk.core;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.AirDeskContext;

/**
 * This class implements all the methods that our application will use to handle remote requests.
 * Low level network methods may be implemented somewhere else.
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
