package pt.ulisboa.tecnico.cmov.airdesk.core;

import pt.ulisboa.tecnico.cmov.airdesk.AirDeskContext;

public class OwnedFileCore extends WorkspaceFileCore {
    public OwnedFileCore(String workspace, String name) {
        super(workspace, name);
        //TODO: retrieve size and editLock from database
    }

    public void setSize(int size) {
        super.setSize(size);
        AirDeskContext.getContext().setFileSize(getWorkspace(), getName(), size);
    }
}
