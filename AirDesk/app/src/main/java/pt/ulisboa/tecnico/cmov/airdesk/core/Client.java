package pt.ulisboa.tecnico.cmov.airdesk.core;

import android.content.Context;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.AirDeskContext;
import pt.ulisboa.tecnico.cmov.airdesk.exceptions.QuotaExceededException;

/**
 * Created by dinis_000 on 30/03/2015.
 */
//TODO: we should use ForeignWorkspaceCore, so we can override some methods to use remote retrievals instead
public class Client {
    public static ArrayList<WorkspaceCore> mountedWorkspaces;

    public static ArrayList<WorkspaceCore> getMountedWorkspaces(Context context) {
        //for now we return local workspaces
        ArrayList<WorkspaceCore> workspaces = (ArrayList) ((AirDeskContext) context).getWorkspaces();
        return mountedWorkspaces = workspaces;
    }

    public static WorkspaceCore getWorkspace(String owner, String workspace) {
        //for now we get it locally
        return Server.getWorkspace("", workspace);
    }

    public static boolean setFileContent(String owner, String workspace, String file, String data)
            throws QuotaExceededException {
        return getWorkspace(owner, workspace).getFile(file).setContent(Server.context, data);
    }

    public static String getFileContent(String owner, String workspace, String file) {
        return getWorkspace(owner, workspace).getFile(file).getContent(Server.context);
    }

    public static void removeFile(String owner, String workspace, String file) {
        getWorkspace(owner, workspace).removeFile(file);
        Server.removeFile("", workspace, file);
    }
}
