package pt.ulisboa.tecnico.cmov.airdesk.core;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.AirDeskContext;
import pt.ulisboa.tecnico.cmov.airdesk.exceptions.QuotaExceededException;

/**
 * This class implements all the methods that our application will use to make remote requests.
 * Low level network methods may be implemented somewhere else.
 */
public class Client {
    private static List<WorkspaceCore> mountedWorkspaces;

    public static List<WorkspaceCore> getMountedWorkspaces(Context context) {
        // FIXME: Client is needed? I added this here just for now
        return ((AirDeskContext) context).getMountedWorkspaces();
    }

    public static WorkspaceCore getWorkspace(String owner, String workspace) {
        //for now we get it locally
        WorkspaceCore w = Server.getWorkspace("", workspace);
        return w;
        //return new ForeignWorkspaceCore(w.getName(), w.getQuota(), w.getTagsString(), w.getOwner(), w.isPublic());
    }

    public static boolean setFileContent(String owner, String workspace, String file, String data)
            throws QuotaExceededException {
        return Server.getWorkspace("", workspace).getFile(file).setContent(Server.context, data);
    }

    public static String getFileContent(String owner, String workspace, String file) {
        return Server.getWorkspace("", workspace).getFile(file).getContent(Server.context);
    }

    public static void removeFile(String owner, String workspace, String file) {
        Server.getWorkspace("", workspace).removeFile(file);
        Server.removeFile("", workspace, file);
    }

    public static ArrayList<String> getFiles(String owner, String workspace) {
        return new ArrayList<>(Server.getWorkspace("", workspace).getFiles());
    }
}
