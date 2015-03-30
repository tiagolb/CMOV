package pt.ulisboa.tecnico.cmov.airdesk.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 26-03-2015.
 */
public class WorkspaceCore {

    private String name;
    private int quota;
    private String tag;

    private String owner_email;
    private List<String> files; // This should be a list of files but i am not going to implement it yet

    /*public static abstract class WorkspaceCoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "workspace";
        public static final String COLUMN_NAME_WORKSPACE_NAME = "workspace_name";
        public static final String COLUMN_NAME_QUOTA = "quota";
        public static final String COLUMN_NAME_TAG = "tag";
        public static final String COLUMN_NAME_OWNER_EMAIL = "owner_email";
        public static final String COLUMN_NAME_FILE = "file_name";
    }*/

    public WorkspaceCore(String name, int quota, String tag, String owner_email) {
        this.name = name;
        this.quota = quota;
        this.tag = tag;
        this.owner_email = owner_email;
        this.files = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }
}
