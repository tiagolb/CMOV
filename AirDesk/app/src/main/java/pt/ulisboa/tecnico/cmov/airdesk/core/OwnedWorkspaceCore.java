package pt.ulisboa.tecnico.cmov.airdesk.core;


/**
 * Created by tiago on 26-03-2015.
 */
public class OwnedWorkspaceCore extends WorkspaceCore {

    private boolean isPublic;

    public OwnedWorkspaceCore(String name, int quota, String tag, String owner_email, boolean isPublic) {
        super(name, quota, tag, owner_email);
        this.isPublic = isPublic;
    }
}
