package pt.ulisboa.tecnico.cmov.airdesk.core;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.AirDeskContext;

/**
 * Created by dinis_000 on 30/03/2015.
 */
public class Server {
    public static AirDeskContext context;

    public static WorkspaceCore getWorkspace(String client, String workspace) {
        //TODO: verify if client belong to clients
        ArrayList<WorkspaceCore> workspaces = (ArrayList) context.getWorkspaces();
        for (WorkspaceCore w : workspaces) if (w.getName().equals(workspace)) return w;
        return null;
    }
}
