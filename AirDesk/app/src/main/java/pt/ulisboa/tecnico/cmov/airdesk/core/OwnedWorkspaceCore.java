package pt.ulisboa.tecnico.cmov.airdesk.core;

import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.AirDeskContext;

/**
 * This class represents an owned WorkspaceCore stored in Database.
 */

public class OwnedWorkspaceCore extends WorkspaceCore {

    //create empty owned workspace
    public OwnedWorkspaceCore(String name, int quota, String tags, String owner, boolean isPublic) {
        super(name, quota, tags, owner, isPublic);
    }

    //create owned workspace with data pre-populated (useful when we get it from the database)
    public OwnedWorkspaceCore(String name, int quota, String owner, boolean isPublic,
                              List<String> tags, List<String> clients, List<String> files) {
        super(name, quota, owner, isPublic);
        for (String tag : tags) super.addTag(tag);
        for (String client : clients) super.addClient(client);
        for (String file : files) super.addFile(file);
    }

    public void addClient(String client) {
        super.addClient(client);
        AirDeskContext.getContext().addClientToWorkspace(this, client);
    }

    public void addFile(String file) {
        super.addFile(file);
        AirDeskContext.getContext().addFileToWorkspace(this, file);
    }

    public void setQuota(int quota) {
        super.setQuota(quota);
        AirDeskContext.getContext().setWorkspaceQuota(this, quota);
    }

    public void setTags(String tags) {
        super.setTags(tags);
        AirDeskContext.getContext().setWorkspaceTags(this, tags);
    }

    public void removeFile(String file) {
        super.removeFile(file);
        AirDeskContext.getContext().removeFileFromWorkspace(this, file);
    }

    public void removeClient(String client) {
        super.removeClient(client);
        AirDeskContext.getContext().removeClientFromWorkspace(this, client);
    }

    public WorkspaceFileCore getFile(String fileName) {
        return new OwnedFileCore(getName(), fileName);
    }

}
