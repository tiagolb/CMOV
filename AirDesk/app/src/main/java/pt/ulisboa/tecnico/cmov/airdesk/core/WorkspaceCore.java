package pt.ulisboa.tecnico.cmov.airdesk.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 26-03-2015.
 */
public class WorkspaceCore {

    //private String id;
    private String name;
    private int quota;
    private List<String> tags;
    private String owner;
    private List<String> files;
    private List<String> clients;

    /*public static abstract class WorkspaceCoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "workspace";
        public static final String COLUMN_NAME_WORKSPACE_NAME = "workspace_name";
        public static final String COLUMN_NAME_QUOTA = "quota";
        public static final String COLUMN_NAME_TAG = "tag";
        public static final String COLUMN_NAME_OWNER_EMAIL = "owner";
        public static final String COLUMN_NAME_FILE = "file_name";
    }*/

    // TODO: Remove tag or receive list
    public WorkspaceCore(String name, int quota, String tag, String owner) {
        //this.id = UUID.randomUUID().toString();
        this.name = name;
        this.quota = quota; //bytes
        this.tags = new ArrayList<>();
        this.owner = owner;
        this.files = new ArrayList<>();
        this.clients = new ArrayList<>();
    }

    public int getQuota() {
        return quota;
    }

    public int getQuotaUsed() {
        return 0; //FIXME: query db for the sum of the files size
    }

    public int getQuotaAvailable() {
        return getQuota() - getQuotaUsed();
    }

    // TODO: Return unmodifiable Collection
    public List<String> getTags() {
        return tags;
    }

    public String getOwner() {
        return owner;
    }

    // TODO: Return unmodifiable Collection
    public List<String> getFiles() {
        return files;
    }

    // TODO: Return unmodifiable Collection
    public List<String> getClients() {
        return clients;
    }

    public Boolean isClient(String client) {
        return this.clients.contains(client);
    }

    public void addClient(String client) {
        this.clients.add(client);
    }

    public void removeClient(String client) {
        this.clients.remove(client);
    }

    public String getName() {
        return name;
    }

    //public String getId() { return id; }

    public void addFile(String fileName) {
        //this.files.put(fileName, new WorkspaceFileCore(fileName, this.getId()));
        this.files.add(fileName);
    }

    public Boolean hasFile(String fileName) {
        //return this.files.containsKey(fileName);
        return this.files.contains(fileName);
    }

    public Boolean hasClient(String client) {
        return this.clients.contains(client);
    }

    public WorkspaceFileCore getFile(String fileName) {
        //return this.files.get(fileName);
        return new WorkspaceFileCore(fileName, getName());
    }

    public void removeFile(String fileName) {
        this.files.remove(fileName);
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

}
