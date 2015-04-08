package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.sqlite.DatabaseHelper;

public class AirDeskContext extends Application {
    private List<WorkspaceCore> workspaces = null;
    private List<WorkspaceCore> mountedWorkspaces = null;
    private List<String> subscribedTags = null;
    private DatabaseHelper dbHelper = null;
    static public AirDeskContext context;

    static public AirDeskContext getContext() {
        return context;
    }

    public void initContext(String ownerEmail) {
        if (workspaces == null) {
            dbHelper = new DatabaseHelper(this);
            workspaces = dbHelper.getAllWorkspaces(ownerEmail);
            AirDeskContext.context = this;
        }

        mountedWorkspaces = dbHelper.getAllMountedWorkspaces();
        mountedWorkspaces.addAll(dbHelper.getAllPushedWorkspaces(ownerEmail));

        /*if(workspaces.isEmpty()) {
            OwnedWorkspaceCore workspace = new OwnedWorkspaceCore("Example Workspace", 16, "", "self", true);
            addClientToWorkspace(workspace, "joao@tecnico.ulisboa.pt");
            addClientToWorkspace(workspace, "luis@tecnico.ulisboa.pt");
            addClientToWorkspace(workspace, "ana@tecnico.ulisboa.pt");
            addFileToWorkspace(workspace, "Notas.txt");
            addFileToWorkspace(workspace, "Exemplo.txt");
            //workspaces.add(workspace);
            addWorkspace(workspace);
        }*/
    }

    public void addTagToSubscriptionTags(String tag) {
        dbHelper.addTagToSubscribedTags(tag);
    }

    public void setWorkspaceTags(WorkspaceCore workspace, String tags) {
        workspace.setTags(tags);
        dbHelper.setWorkspaceTags(workspace.getName(), workspace.getTags());

    }

    public void addWorkspace(WorkspaceCore workspace) {
        if (workspaces == null) {
            workspaces = new ArrayList<>();
        }
        workspaces.add(workspace);
        dbHelper.addWorkspace(workspace);
    }

    public void addMountedWorkspace(WorkspaceCore workspace) {
        if (workspaces == null) {
            workspaces = new ArrayList<>();
        }
        mountedWorkspaces.add(workspace);
        //FIXME: para a primeira entrega isto esta comentado mas depois deixa de estar
        //dbHelper.addWorkspace(workspace);
    }

    public void addClientToWorkspace(WorkspaceCore workspace, String client) {
        //workspace.addClient(client);
        dbHelper.addClientToWorkspace(workspace.getName(), client);
    }

    public void removeClientFromWorkspace(WorkspaceCore workspace, String client) {
        workspace.removeClient(client);
        dbHelper.removeClientFromWorkspace(workspace.getName(), client);
    }

    public void addFileToWorkspace(WorkspaceCore workspace, String file) {
        workspace.addFile(file);
        dbHelper.addFileToWorkspace(workspace.getName(), file);
    }

    public void removeFileFromWorkspace(WorkspaceCore workspace, String file) {
        workspace.removeFile(file);
        dbHelper.removeFileFromWorkspace(workspace.getName(), file);
    }

    public void setWorkspaceQuota(WorkspaceCore workspace, int quota) {
        workspace.setQuota(quota);
        dbHelper.setWorkspaceQuota(workspace.getName(), quota);
    }


    public List<WorkspaceCore> getWorkspaces() {
        /*if (workspaces == null) {
            workspaces = new ArrayList<>();
            //first time, let's populate with an example
            OwnedWorkspaceCore workspace = new OwnedWorkspaceCore("Example Workspace", 16, "", "self", true);
            workspace.addClient("joao@tecnico.ulisboa.pt");
            workspace.addClient("luis@tecnico.ulisboa.pt");
            workspace.addClient("ana@tecnico.ulisboa.pt");
            workspace.addFile("Notas.txt");
            workspace.addFile("Exemplo.txt");
            //workspaces.add(workspace);
            addWorkspace(workspace);
        }*/
        return workspaces;
    }

    public List<WorkspaceCore> getMountedWorkspaces() {
        return mountedWorkspaces;
    }

    //FIXME: para a primeira entrega fica assim
    public List<WorkspaceCore> getWorkspacesWithTag(String tag) {
        return dbHelper.getAllWorkspacesWithTag(tag);
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
        WorkspaceCore workspace = null;
        for (int i = 0; i < workspaces.size(); i++) {
            if (name.equals(workspaces.get(i).getName())) {
                workspace = workspaces.get(i);
                workspaces.remove(i);
                dbHelper.removeWorkspace(workspace);
                return;
            }
        }
    }
}
