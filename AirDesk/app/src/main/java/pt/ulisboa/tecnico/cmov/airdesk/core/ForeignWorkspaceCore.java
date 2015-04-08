package pt.ulisboa.tecnico.cmov.airdesk.core;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an foreign WorkspaceCore stored remotely.
 */

public class ForeignWorkspaceCore extends WorkspaceCore {

    public static ArrayList<WorkspaceCore> workspaces = null;

    public ForeignWorkspaceCore(String name, int quota, String tags, String owner, boolean isPublic) {
        super(name, quota, tags, owner, isPublic);
    }

    public WorkspaceFileCore getFile(String fileName) {
        return new ForeignFileCore(fileName, getName());
    }

    public List<String> getFiles() {
        return Client.getFiles(getOwner(), getName());
    }

    public void addFile(String file) {
        super.addFile(file);
        Client.addFile(getOwner(), getName(), file);
    }

    public void removeFile(String file) {
        super.removeFile(file);
        Client.removeFile(getOwner(), getName(), file);
    }

}
