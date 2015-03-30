package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;

/**
 * Created by Francisco on 29-03-2015.
 */
public class AirDeskContext extends Application {
    private List<WorkspaceCore> workspaces;

    public void initContext() {
        if(workspaces == null) {
            workspaces = new ArrayList<WorkspaceCore>();
        }
    }

    public void addWorkspace(WorkspaceCore workspace) {
        workspaces.add(workspace);
    }

    public void removeWorkspace(String name) {
        for(int i = 0; i < workspaces.size(); i++) {
            if(name.equals(workspaces.get(i).getName())) {
                workspaces.remove(i);
                return;
            }
        }
    }
}
