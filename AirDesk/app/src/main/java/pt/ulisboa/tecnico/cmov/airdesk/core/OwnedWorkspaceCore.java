package pt.ulisboa.tecnico.cmov.airdesk.core;


import java.util.ArrayList;

public class OwnedWorkspaceCore extends WorkspaceCore {

    public static ArrayList<WorkspaceCore> workspaces = null;

    public OwnedWorkspaceCore(String name, int quota, String tags, String owner, boolean isPublic) {
        super(name, quota, tags, owner, isPublic);
    }

    public OwnedWorkspaceCore(String name, int quota, String owner, boolean isPublic) {
        super(name, quota, owner, isPublic);
    }

}
