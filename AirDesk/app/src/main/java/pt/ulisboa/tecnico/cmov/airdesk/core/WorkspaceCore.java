package pt.ulisboa.tecnico.cmov.airdesk.core;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//FIXME: most methods implemented here should be in fact implemented in OwnedWorkspaceCore (with db access)
// We should also start using ForeignWorkspaceCore, that should implement methods using calls to Client class
// We're using simple WorkspaceCore almost everywhere, we need to change them to Owned/Foreign.
// In fact, this class could be Abstract.

public class WorkspaceCore {

    private String name;
    private int quota;
    private List<String> tags;
    private String owner;
    private List<String> files;
    private List<String> clients;
    private boolean isPublic;

    // TODO: Remove tag or receive list
    public WorkspaceCore(String name, int quota, String tags, String owner, boolean isPublic) {
        this.name = name;
        this.quota = quota; //bytes
        this.owner = owner;
        this.files = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.isPublic = isPublic;
        setTags(tags);
    }

    public WorkspaceCore(String name, int quota, String owner, boolean isPublic) {
        this.name = name;
        this.quota = quota;
        this.owner = owner;
        this.isPublic = isPublic;
        this.tags = new ArrayList<>();
        this.files = new ArrayList<>();
        this.clients = new ArrayList<>();
    }

    public int getQuota() {
        return quota;
    }

    public int getQuotaUsed() {
        int quotaUsed = 0;
        for (String filename : files) {
            WorkspaceFileCore file = this.getFile(filename);
            quotaUsed += file.getSize();
        }
        return quotaUsed;
    }

    public int getQuotaAvailable() {
        return getQuota() - getQuotaUsed();
    }

    // TODO: Return unmodifiable Collection
    public List<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
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

    public void addClient(String client) {
        this.clients.add(client);
    }

    public void removeClient(String client) {
        this.clients.remove(client);
    }

    public String getName() {
        return name;
    }

    public void addFile(String fileName) {
        this.files.add(fileName);
    }

    public Boolean hasFile(String fileName) {
        return this.files.contains(fileName);
    }

    public Boolean hasClient(String client) {
        return this.clients.contains(client);
    }

    public WorkspaceFileCore getFile(String fileName) {
        return new WorkspaceFileCore(fileName, getName());
    }

    public void removeFile(String fileName) {
        this.files.remove(fileName);
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public void setTags(String tags) {
        this.tags = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(tags);
        while (st.hasMoreTokens()) this.tags.add(st.nextToken());
    }

    public String getTagsString() {
        StringBuilder sb = new StringBuilder();
        for (String tag : this.tags)
            sb.append(tag).append(" ");
        return sb.toString().trim();
    }

    public boolean isPublic() {
        return this.isPublic;
    }

}
