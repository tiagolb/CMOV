package pt.ulisboa.tecnico.cmov.airdesk.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pt.ulisboa.tecnico.cmov.airdesk.core.OwnedWorkspaceCore;

/**
 * Created by Francisco on 30-03-2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "workspaceManager";

    // Tables
    private static final String TABLE_WORKSPACE = "workspace";
    private static final String TABLE_FILE = "file";
    private static final String TABLE_TAG = "tag";
    private static final String TABLE_CLIENT = "client";

    // Columns
    private static final String COLUMN_WORKSPACE = "workspaceName";
    private static final String COLUMN_OWNER = "owner";
    private static final String COLUMN_QUOTA = "quota";
    private static final String COLUMN_FILE = "fileName";
    private static final String COLUMN_TAG = "tagName";
    private static final String COLUMN_CLIENT = "clientName";

    // Statements: Create tables
    // TODO: ADD Foreign keys
    private static final String CREATE_TABLE_WORKSPACE =
            "CREATE TABLE " + TABLE_WORKSPACE + "(" + COLUMN_WORKSPACE + " TEXT PRIMARY KEY," +
                    COLUMN_OWNER + " TEXT," + COLUMN_QUOTA + " INTEGER)";
    private static final String CREATE_TABLE_FILE =
            "CREATE TABLE " + TABLE_FILE + "(" + COLUMN_WORKSPACE + " TEXT," +
                    COLUMN_FILE + " TEXT, PRIMARY KEY (" + COLUMN_WORKSPACE + COLUMN_FILE + "))";
    private static final String CREATE_TABLE_TAG =
            "CREATE TABLE " + TABLE_TAG + "(" + COLUMN_WORKSPACE + " TEXT," +
                    COLUMN_TAG + " TEXT, PRIMARY KEY (" + COLUMN_WORKSPACE + COLUMN_TAG + "))";
    private static final String CREATE_TABLE_CLIENT =
            "CREATE TABLE " + TABLE_FILE + "(" + COLUMN_WORKSPACE + " TEXT," +
                    COLUMN_CLIENT + " TEXT, PRIMARY KEY (" + COLUMN_WORKSPACE + COLUMN_CLIENT + "))";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WORKSPACE);
        db.execSQL(CREATE_TABLE_FILE);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_CLIENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKSPACE);
        onCreate(db);
    }

    public void addWorkspace(OwnedWorkspaceCore workspace) {
        SQLiteDatabase db = this.getWritableDatabase();
        String workspaceName = workspace.getName();

        ContentValues values = new ContentValues();
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
}
