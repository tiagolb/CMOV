package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.sqlite.DatabaseHelper;

public class AirDeskContext extends Application {
    private ArrayList<WorkspaceCore> workspaces = null;
    private List<WorkspaceCore> mountedWorkspaces = null;
    private List<String> subscribedTags = null;
    private DatabaseHelper dbHelper = null;
    static private AirDeskContext context;

    static public AirDeskContext getContext() {
        return context;
    }

    public void initContext(String ownerEmail, boolean newLogin) {
        if (workspaces == null || newLogin) {
            dbHelper = new DatabaseHelper(this);
            workspaces = dbHelper.getAllWorkspaces(ownerEmail);
            AirDeskContext.context = this;
        }

        mountedWorkspaces = dbHelper.getAllMountedWorkspaces(ownerEmail);
        mountedWorkspaces.addAll(getAllPushedWorkspaces(ownerEmail));
        subscribedTags = dbHelper.getSubscribedTags(ownerEmail);
    }

    private List<WorkspaceCore> getAllPushedWorkspaces(String ownerEmail) {
        List<WorkspaceCore> pushed = dbHelper.getAllPushedWorkspaces(ownerEmail);
        List<WorkspaceCore> cleanedPushed = new ArrayList<WorkspaceCore>();
        for(WorkspaceCore workspace : pushed) {
            if(isWorkspaceNotMounted(workspace.getName())) {
                cleanedPushed.add(workspace);
            }
        }
        return cleanedPushed;
    }

    public void addTagToSubscribedTags(String tag, String ownerEmail) {
        subscribedTags.add(tag);
        dbHelper.addTagToSubscribedTags(tag, ownerEmail);
        List<WorkspaceCore> workspacesWithTag = context.getWorkspacesWithTag(tag);
        for (WorkspaceCore workspace : workspacesWithTag) {
            if(context.isWorkspaceNotMounted(workspace.getName())) {
                context.addMountedWorkspace(workspace);
            }
        }
    }

    public void setWorkspaceTags(WorkspaceCore workspace, String tags) {
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
    }

    public void addClientToWorkspace(WorkspaceCore workspace, String client) {
        dbHelper.addClientToWorkspace(workspace.getName(), client);
    }

    public void removeClientFromWorkspace(WorkspaceCore workspace, String client) {
        dbHelper.removeClientFromWorkspace(workspace.getName(), client);
    }

    public void addFileToWorkspace(WorkspaceCore workspace, String file) {
        dbHelper.addFileToWorkspace(workspace.getName(), file);
    }

    public void removeFileFromWorkspace(WorkspaceCore workspace, String file) {
        dbHelper.removeFileFromWorkspace(workspace.getName(), file);
    }

    public void setWorkspaceQuota(WorkspaceCore workspace, int quota) {
        dbHelper.setWorkspaceQuota(workspace.getName(), quota);
    }


    public ArrayList<WorkspaceCore> getWorkspaces() {
        return workspaces;
    }

    public List<WorkspaceCore> getMountedWorkspaces() {
        return mountedWorkspaces;
    }

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

    public boolean isWorkspaceNotMounted(String name) {
        for (WorkspaceCore workspace : mountedWorkspaces) {
            if (name.equals(workspace.getName())) {
                return false;
            }
        }
        return true;
    }

    public void removeWorkspace(String name) {
        WorkspaceCore workspace;
        for (int i = 0; i < workspaces.size(); i++) {
            if (name.equals(workspaces.get(i).getName())) {
                workspace = workspaces.get(i);
                workspaces.remove(i);
                dbHelper.removeWorkspace(workspace);
                return;
            }
        }
    }

    public List<String> getSubscribedTags() {
        return subscribedTags;
    }

    public void removeSubscribedTag(String tag) {
        for (int i = 0; i < subscribedTags.size(); i++) {
            if (tag.equals(subscribedTags.get(i))) {
                subscribedTags.remove(i);
                dbHelper.removeSubscribedTag(tag);
            }
        }
    }

    public int getQuotaUsed(String workspace) {
        return dbHelper.getQuotaUsed(workspace);
    }

    public void setFileSize(String workspace, String file, int size) {
        dbHelper.setFileSize(workspace, file, size);
    }

    public void setPrivacy(int privacy, WorkspaceCore workspace) {
        dbHelper.setPrivacy(privacy, workspace.getName());
    }
}
