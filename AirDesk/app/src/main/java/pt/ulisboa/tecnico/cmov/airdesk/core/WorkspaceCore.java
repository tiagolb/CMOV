package pt.ulisboa.tecnico.cmov.airdesk.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Created by tiago on 26-03-2015.
 */
public class WorkspaceCore {

    private String id;
    private String name;
    private int quota;
    private String tag;

    private String owner;
    public TreeMap<String, WorkspaceFileCore> files;

    /*public static abstract class WorkspaceCoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "workspace";
        public static final String COLUMN_NAME_WORKSPACE_NAME = "workspace_name";
        public static final String COLUMN_NAME_QUOTA = "quota";
        public static final String COLUMN_NAME_TAG = "tag";
        public static final String COLUMN_NAME_OWNER_EMAIL = "owner";
        public static final String COLUMN_NAME_FILE = "file_name";
    }*/

    public WorkspaceCore(String name, int quota, String tag, String owner) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.quota = quota;
        this.tag = tag;
        this.owner = owner;
        this.files = new TreeMap<>();
    }

    public String getName() {
        return name;
    }

    public String getId() { return id; }

    public void addFile(String fileName) {
        this.files.put(fileName, new WorkspaceFileCore(fileName, this.getId()));
    }
    public Boolean hasFile(String fileName) {
        return this.files.containsKey(fileName);
    }
    public WorkspaceFileCore getFile(String fileName) { return this.files.get(fileName); }
    public void removeFile(String fileName) {
        this.files.remove(fileName);
    }

}
