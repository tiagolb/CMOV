package pt.ulisboa.tecnico.cmov.airdesk.core;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import pt.ulisboa.tecnico.cmov.airdesk.AirDeskContext;

public abstract class WorkspaceCore {

    private String name;
    private int quota; //bytes
    private List<String> tags = new ArrayList<>();
    private String owner;
    private List<String> files = new ArrayList<>();
    private List<String> clients = new ArrayList<>();
    private boolean isPublic;

    public WorkspaceCore(String name, int quota, String tags, String owner, boolean isPublic) {
        this(name, quota, owner, isPublic);
        setTags(tags);
    }

    public WorkspaceCore(String name, int quota, String owner, boolean isPublic) {
        this.name = name;
        this.quota = quota;
        this.owner = owner;
        this.isPublic = isPublic;
    }

    public int getQuota() {
        return quota;
    }

    public int getQuotaUsed() {
        return AirDeskContext.getContext().getQuotaUsed(getName());
    }

    public int getQuotaAvailable() {
        return getQuota() - getQuotaUsed();
    }

    public List<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public String getOwner() {
        return owner;
    }

    public List<String> getFiles() {
        return files;
    }

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

    public abstract WorkspaceFileCore getFile(String fileName);

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

    public void setPublic() {
        this.isPublic = true;
    }

    public void setPrivate() {
        this.isPublic = false;
    }
}
