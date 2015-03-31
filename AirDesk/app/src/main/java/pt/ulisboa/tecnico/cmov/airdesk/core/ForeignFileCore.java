package pt.ulisboa.tecnico.cmov.airdesk.core;

import android.content.Context;

import pt.ulisboa.tecnico.cmov.airdesk.exceptions.QuotaExceededException;

/**
 * Created by Francisco on 29-03-2015.
 */
public class ForeignFileCore extends WorkspaceFileCore {

    private static final String LINE_SEP = System.getProperty("line.separator");

    private String name;
    private String workspace;
    private String owner;

    public ForeignFileCore(String name, String workspace) {
        super(name, workspace);
        this.name = name;
        this.workspace = workspace;
    }

    public boolean setContent(Context context, String data) throws QuotaExceededException {
        return Client.setFileContent(owner, workspace, name, data);
    }

    public String getContent(Context context) {
        return Client.getFileContent(owner, workspace, name);
    }

    public void removeFile(Context context) {
        Client.removeFile(owner, workspace, name);
    }
}
