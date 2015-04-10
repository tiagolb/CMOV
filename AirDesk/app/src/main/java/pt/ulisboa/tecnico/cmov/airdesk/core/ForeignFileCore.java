package pt.ulisboa.tecnico.cmov.airdesk.core;

import android.content.Context;

import pt.ulisboa.tecnico.cmov.airdesk.exceptions.QuotaExceededException;

public class ForeignFileCore extends WorkspaceFileCore {

    private String owner;

    public ForeignFileCore(String owner, String workspace, String name) {
        super(workspace, name);
        this.owner = owner;
    }

    public boolean setContent(Context context, String data) throws QuotaExceededException {
        return Client.setFileContent(owner, getWorkspace(), getName(), data);
    }

    public String getContent(Context context) {
        return Client.getFileContent(owner, getWorkspace(), getName());
    }

    public void removeFile(Context context) {
        Client.removeFile(owner, getWorkspace(), getName());
    }

    public String getOwner() {
        return owner;
    }
}
