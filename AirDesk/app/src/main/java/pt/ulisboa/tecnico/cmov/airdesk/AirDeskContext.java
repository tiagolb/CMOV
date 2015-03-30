package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.core.OwnedWorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;

/**
 * Created by Francisco on 29-03-2015.
 */
public class AirDeskContext extends Application {
    private List<WorkspaceCore> workspaces = null;

    public void initContext() {
        if (workspaces == null) {
            workspaces = new ArrayList<WorkspaceCore>();
        }
    }

    public void addWorkspace(WorkspaceCore workspace) {
        if (workspaces == null) {
            workspaces = new ArrayList<WorkspaceCore>();
        }
        workspaces.add(workspace);
    }

    public List<WorkspaceCore> getWorkspaces() {
        if (workspaces == null) {
            workspaces = new ArrayList<WorkspaceCore>();
            //first time, let's populate with an example
            OwnedWorkspaceCore workspace = new OwnedWorkspaceCore("Example Workspace", 1, "", "self", true);
            workspace.addClient("joao@tecnico.ulisboa.pt");
            workspace.addClient("luis@tecnico.ulisboa.pt");
            workspace.addClient("ana@tecnico.ulisboa.pt");
            workspace.addFile("Notas.txt");
            workspace.addFile("Exemplo.txt");
            workspaces.add(workspace);
        }
        return workspaces;
    }

    public WorkspaceCore getWorkspace(String name) {
        for (WorkspaceCore workspace : workspaces) {
            if (name.equals(workspace.getName())) {
                return workspace;
            }
        }
        return null;
    }

    public void removeWorkspace(String name) {
        for (int i = 0; i < workspaces.size(); i++) {
            if (name.equals(workspaces.get(i).getName())) {
                workspaces.remove(i);
                return;
            }
        }
    }
}
