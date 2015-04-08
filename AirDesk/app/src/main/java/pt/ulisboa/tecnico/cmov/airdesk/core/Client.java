package pt.ulisboa.tecnico.cmov.airdesk.core;

import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.exceptions.QuotaExceededException;

/**
 * This class implements all the methods that our application will use to make remote requests.
 * Low level network methods may be implemented somewhere else.
 */

// For now all methods will call our own server directly. When we implement networking, we only need
// change these methods.
public class Client {

    public static WorkspaceCore getWorkspace(String owner, String workspace) {
        WorkspaceCore w = Server.getWorkspace("client", workspace);
        return new ForeignWorkspaceCore(w.getName(), w.getQuota(), w.getTagsString(), w.getOwner(), w.isPublic());
    }

    public static boolean setFileContent(String owner, String workspace, String file, String content)
            throws QuotaExceededException {
        return Server.setFileContent("client", workspace, file, content);
    }

    public static String getFileContent(String owner, String workspace, String file) {
        return Server.getFileContent("client", workspace, file);
    }

    public static void removeFile(String owner, String workspace, String file) {
        Server.removeFile("client", workspace, file);
    }

    public static List<String> getFiles(String owner, String workspace) {
        return Server.getWorkspace("client", workspace).getFiles();
    }

    public static void addFile(String owner, String workspace, String file) {
        Server.addFile("client", workspace, file);
    }
}
