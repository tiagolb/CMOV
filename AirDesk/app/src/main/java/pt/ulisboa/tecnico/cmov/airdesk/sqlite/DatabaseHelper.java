package pt.ulisboa.tecnico.cmov.airdesk.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.core.OwnedWorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "workspaceManager";

    // Tables
    private static final String TABLE_WORKSPACE = "workspace";
    //private static final String TABLE_FOREIGN_WORKSPACE = "foreign_workspace";
    private static final String TABLE_FILE = "file";
    private static final String TABLE_TAG = "tag";
    private static final String TABLE_SUBSCRIPTION_TAG = "subscription_tag";
    private static final String TABLE_CLIENT = "client";

    // Columns
    private static final String COLUMN_WORKSPACE = "workspaceName";
    private static final String COLUMN_OWNER = "owner";
    private static final String COLUMN_QUOTA = "quota";
    private static final String COLUMN_PUBLIC = "isPublic";
    private static final String COLUMN_FILE = "fileName";
    private static final String COLUMN_SIZE = "size";
    private static final String COLUMN_TAG = "tagName";
    private static final String COLUMN_CLIENT = "clientName";

    // Statements: Create tables
    // TODO: ADD Foreign keys
    private static final String CREATE_TABLE_WORKSPACE =
            "CREATE TABLE " + TABLE_WORKSPACE + " (" + COLUMN_WORKSPACE + " TEXT PRIMARY KEY," +
                    COLUMN_OWNER + " TEXT," + COLUMN_QUOTA + " INTEGER," + COLUMN_PUBLIC + " INTEGER);";

    /*private static final String CREATE_TABLE_FOREIGN_WORKSPACE =
            "CREATE TABLE " + TABLE_FOREIGN_WORKSPACE + " (" + COLUMN_WORKSPACE + " TEXT PRIMARY KEY," +
                    COLUMN_OWNER + " TEXT," + COLUMN_QUOTA + " INTEGER);";
    */

    private static final String CREATE_TABLE_SUBSCRIPTION_TAG =
            "CREATE TABLE " + TABLE_SUBSCRIPTION_TAG + " (" + COLUMN_TAG + " TEXT);";

    private static final String CREATE_TABLE_FILE =
            "CREATE TABLE " + TABLE_FILE + " (" +
                    COLUMN_WORKSPACE + " TEXT, " +
                    COLUMN_FILE + " TEXT, " +
                    COLUMN_SIZE + " INTEGER, " +
                    "PRIMARY KEY (" +
                    COLUMN_WORKSPACE + ", " + COLUMN_FILE + "));";
    private static final String CREATE_TABLE_TAG =
            "CREATE TABLE " + TABLE_TAG + " (" + COLUMN_WORKSPACE + " TEXT," +
                    COLUMN_TAG + " TEXT, PRIMARY KEY (" + COLUMN_WORKSPACE + "," + COLUMN_TAG + "));";
    private static final String CREATE_TABLE_CLIENT =
            "CREATE TABLE " + TABLE_CLIENT + " (" + COLUMN_WORKSPACE + " TEXT," +
                    COLUMN_CLIENT + " TEXT, PRIMARY KEY (" + COLUMN_WORKSPACE + "," + COLUMN_CLIENT + "));";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WORKSPACE);
        db.execSQL(CREATE_TABLE_FILE);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_CLIENT);

        db.execSQL(CREATE_TABLE_SUBSCRIPTION_TAG);
        // TODO: perguntar ao francis se e assim que se faz
        //db.execSQL(CREATE_TABLE_FOREIGN_WORKSPACE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKSPACE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBSCRIPTION_TAG);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOREIGN_WORKSPACE);
        onCreate(db);
    }

    // Methods to add stuff to workspace
    public void addWorkspace(WorkspaceCore workspace) {
        SQLiteDatabase db = this.getWritableDatabase();
        String workspaceName = workspace.getName();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PUBLIC, (workspace.isPublic()) ? 1 : 0);
        values.put(COLUMN_WORKSPACE, workspaceName);
        values.put(COLUMN_OWNER, workspace.getOwner());
        values.put(COLUMN_QUOTA, workspace.getQuota());


        db.insert(TABLE_WORKSPACE, null, values);

        for (String fileName : workspace.getFiles()) {
            addFileToWorkspace(workspaceName, fileName, db);
        }

        for (String tag : workspace.getTags()) {
            addTagToWorkspace(workspaceName, tag, db);
        }

        for (String client : workspace.getClients()) {
            addTagToWorkspace(workspaceName, client, db);
        }
    }

    public void addFileToWorkspace(String workspace, String file, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORKSPACE, workspace);
        values.put(COLUMN_FILE, file);

        db.insert(TABLE_FILE, null, values);
    }

    public void addFileToWorkspace(String workspace, String file) {
        SQLiteDatabase db = this.getWritableDatabase();

        addFileToWorkspace(workspace, file, db);
    }

    public void addTagToWorkspace(String workspace, String tag, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORKSPACE, workspace);
        values.put(COLUMN_TAG, tag);

        db.insert(TABLE_TAG, null, values);
    }

    public void addTagToWorkspace(String workspace, String tag) {
        SQLiteDatabase db = this.getWritableDatabase();

        addTagToWorkspace(workspace, tag, db);
    }

    public void addTagToSubscribedTags(String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAG, tag);

        db.insert(TABLE_SUBSCRIPTION_TAG, null, values);
    }

    public void addClientToWorkspace(String workspace, String client, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORKSPACE, workspace);
        values.put(COLUMN_CLIENT, client);

        db.insert(TABLE_CLIENT, null, values);
    }

    public void addClientToWorkspace(String workspace, String client) {
        SQLiteDatabase db = this.getWritableDatabase();

        addClientToWorkspace(workspace, client, db);
    }

    // Methods to remove stuff from workspace
    public void removeWorkspace(WorkspaceCore workspace) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = COLUMN_WORKSPACE + " = ?";
        String workspaceName = workspace.getName();

        db.delete(TABLE_WORKSPACE, whereClause, new String[]{workspaceName});

        List<String> files = workspace.getFiles();
        List<String> tags = workspace.getTags();
        List<String> clients = workspace.getClients();

        for (String file : files) {
            removeFileFromWorkspace(workspaceName, file, db);
        }

        for (String tag : tags) {
            removeTagFromWorkspace(workspaceName, tag, db);
        }

        for (String client : clients) {
            removeClientFromWorkspace(workspaceName, client, db);
        }
    }

    private void removeFileFromWorkspace(String workspaceName, String file, SQLiteDatabase db) {
        String whereClause = COLUMN_WORKSPACE + " = ? and " + COLUMN_FILE + " = ?";
        db.delete(TABLE_FILE, whereClause, new String[]{workspaceName, file});
    }

    public void removeFileFromWorkspace(String workspaceName, String file) {
        SQLiteDatabase db = this.getWritableDatabase();

        removeFileFromWorkspace(workspaceName, file, db);
    }

    private void removeTagFromWorkspace(String workspaceName, String tag, SQLiteDatabase db) {
        String whereClause = COLUMN_WORKSPACE + " = ? and " + COLUMN_TAG + " = ?";
        db.delete(TABLE_TAG, whereClause, new String[]{workspaceName, tag});
    }

    public void removeTagFromWorkspace(String workspaceName, String tag) {
        SQLiteDatabase db = this.getWritableDatabase();

        removeTagFromWorkspace(workspaceName, tag, db);
    }

    private void removeClientFromWorkspace(String workspaceName, String client, SQLiteDatabase db) {
        String whereClause = COLUMN_WORKSPACE + " = ? and " + COLUMN_CLIENT + " = ?";
        db.delete(TABLE_CLIENT, whereClause, new String[]{workspaceName, client});
    }

    public void removeClientFromWorkspace(String workspaceName, String client) {
        SQLiteDatabase db = this.getWritableDatabase();

        removeClientFromWorkspace(workspaceName, client, db);
    }

    // Methods to get stuff from workspace
    private WorkspaceCore getWorkspace(String workspaceName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_WORKSPACE;
        String selectFilesQuery = "SELECT * FROM " + TABLE_FILE +
                " WHERE " + COLUMN_WORKSPACE + " = ?";
        String selectTagsQuery = "SELECT * FROM " + TABLE_TAG +
                " WHERE " + COLUMN_WORKSPACE + " = ?";
        String selectClientsQuery = "SELECT * FROM " + TABLE_CLIENT +
                " WHERE " + COLUMN_WORKSPACE + " = ?";

        String[] workspaceArg = new String[]{workspaceName};

        Cursor workspaceCursor = db.rawQuery(selectQuery, null);
        if (workspaceCursor.moveToFirst()) {
            int quota = workspaceCursor.getInt(workspaceCursor.getColumnIndex(COLUMN_QUOTA));
            String owner = workspaceCursor.getString(workspaceCursor.getColumnIndex(COLUMN_OWNER));
            // Add to table isPublic
            boolean isPublic = (workspaceCursor.getInt(workspaceCursor.
                    getColumnIndex(COLUMN_PUBLIC)) == 1);
            workspaceCursor.close();

            ArrayList<String> files = new ArrayList<>();
            Cursor fileCursor = db.rawQuery(selectFilesQuery, workspaceArg);
            if (fileCursor.moveToFirst()) {
                do {
                    files.add(fileCursor.getString(fileCursor.getColumnIndex(COLUMN_FILE)));
                    //workspace.addFile(fileCursor.getString(fileCursor.getColumnIndex(COLUMN_FILE)));
                } while (fileCursor.moveToNext());
            }
            fileCursor.close();

            ArrayList<String> tags = new ArrayList<>();
            Cursor tagCursor = db.rawQuery(selectTagsQuery, workspaceArg);
            if (tagCursor.moveToFirst()) {
                do {
                    tags.add(tagCursor.getString(tagCursor.getColumnIndex(COLUMN_TAG)));
                    //workspace.addTag(tagCursor.getString(tagCursor.getColumnIndex(COLUMN_TAG)));
                } while (tagCursor.moveToNext());
            }
            tagCursor.close();

            ArrayList<String> clients = new ArrayList<>();
            Cursor clientCursor = db.rawQuery(selectClientsQuery, workspaceArg);
            if (clientCursor.moveToFirst()) {
                do {
                    clients.add(clientCursor.getString(clientCursor.getColumnIndex(COLUMN_CLIENT)));
                    // workspace.addClient(clientCursor.getString(clientCursor.getColumnIndex(COLUMN_CLIENT)));
                } while (clientCursor.moveToNext());
            }
            clientCursor.close();

            return new OwnedWorkspaceCore(workspaceName, quota, owner, isPublic, tags, clients, files);
        }
        return null;
    }

    public ArrayList<WorkspaceCore> getAllWorkspaces(String ownerEmail) {
        ArrayList<WorkspaceCore> workspaces = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + COLUMN_WORKSPACE +
                " FROM " + TABLE_WORKSPACE +
                " WHERE " + COLUMN_OWNER + "='" + ownerEmail + "'";
        Log.d("SQL", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                workspaces.add(getWorkspace(c.getString(c.getColumnIndex(COLUMN_WORKSPACE))));
            } while (c.moveToNext());
        }
        c.close();

        return workspaces;
    }

    // ON CREATE
    public List<WorkspaceCore> getAllMountedWorkspaces() {
        List<WorkspaceCore> workspaces = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        //TODO: depois para a segunda entrega tem que se mudar isto mas por agora :public:
        String selectQuery = "SELECT " + "T." + COLUMN_WORKSPACE +
                " FROM " + TABLE_TAG + " AS T , " +
                TABLE_SUBSCRIPTION_TAG + " AS S , " +
                TABLE_WORKSPACE + " AS W " +
                " WHERE " + "T." + COLUMN_TAG + "=" + "S." + COLUMN_TAG +
                " AND W." + COLUMN_WORKSPACE + "=T." + COLUMN_WORKSPACE +
                " AND " + COLUMN_PUBLIC + "=1";
        Log.d("SQL", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                workspaces.add(getWorkspace(c.getString(c.getColumnIndex(COLUMN_WORKSPACE))));
            } while (c.moveToNext());
        }
        c.close();

        return workspaces;
    }

    // SUBSCRIBE
    public List<WorkspaceCore> getAllWorkspacesWithTag(String tag) {
        List<WorkspaceCore> workspaces = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + "W." + COLUMN_WORKSPACE +
                " FROM " + TABLE_WORKSPACE + " AS W , " + TABLE_TAG + " AS T" +
                " WHERE " + "W." + COLUMN_WORKSPACE + "=T." + COLUMN_WORKSPACE +
                " AND " + COLUMN_TAG + "='" + tag + "'" +
                " AND " + COLUMN_PUBLIC + "=1";
        Log.d("SQL", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                workspaces.add(getWorkspace(c.getString(c.getColumnIndex(COLUMN_WORKSPACE))));
            } while (c.moveToNext());
        }
        c.close();

        return workspaces;
    }

    public void setWorkspaceQuota(String name, int quota) {
        SQLiteDatabase db = getWritableDatabase();
        String where = COLUMN_WORKSPACE + " = ?";
        ContentValues newValues = new ContentValues();
        newValues.put(COLUMN_QUOTA, quota);
        String[] args = new String[]{String.valueOf(quota)};
        db.update(TABLE_WORKSPACE, newValues, where, args);
    }

    public List<WorkspaceCore> getAllPushedWorkspaces(String ownerEmail) {
        List<WorkspaceCore> workspaces = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + COLUMN_WORKSPACE +
                " FROM " + TABLE_CLIENT +
                " WHERE " + COLUMN_CLIENT + "=" + "'" + ownerEmail + "'";
        Log.d("SQL", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                workspaces.add(getWorkspace(c.getString(c.getColumnIndex(COLUMN_WORKSPACE))));
            } while (c.moveToNext());
        }
        c.close();

        return workspaces;
    }

    public void setWorkspaceTags(String workspaceName, List<String> tags) {
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = COLUMN_WORKSPACE + " = ?";
        db.delete(TABLE_TAG, whereClause, new String[]{workspaceName});

        for (String tag : tags) {
            addTagToWorkspace(workspaceName, tag, db);
        }
    }

    public List<String> getSubscribedTags() {
        List<String> tags = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + COLUMN_TAG +
                " FROM " + TABLE_SUBSCRIPTION_TAG;
        Log.d("SQL", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                tags.add(c.getString(c.getColumnIndex(COLUMN_TAG)));
            } while (c.moveToNext());
        }
        c.close();

        return tags;
    }

    public void removeSubscribedTag(String tag) {
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = COLUMN_TAG + " = ?";
        db.delete(TABLE_SUBSCRIPTION_TAG, whereClause, new String[]{tag});
    }

    public int getQuotaUsed(String workspace) {
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT SUM(" + COLUMN_SIZE + ") FROM " + TABLE_FILE +
                " WHERE " + COLUMN_WORKSPACE + " = '" + workspace + "'";
        Log.d("SQL", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) result = c.getInt(0);
        c.close();
        return result;
    }

    public void setFileSize(String workspace, String file, int size) {
        SQLiteDatabase db = getWritableDatabase();
        String where = COLUMN_WORKSPACE + " = ? AND " + COLUMN_FILE + " = ?";
        ContentValues newValues = new ContentValues();
        newValues.put(COLUMN_SIZE, size);
        String[] args = new String[]{workspace, file};
        db.update(TABLE_FILE, newValues, where, args);
    }
}
