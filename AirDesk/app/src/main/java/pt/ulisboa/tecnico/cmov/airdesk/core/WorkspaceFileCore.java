package pt.ulisboa.tecnico.cmov.airdesk.core;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import pt.ulisboa.tecnico.cmov.airdesk.AirDeskContext;
import pt.ulisboa.tecnico.cmov.airdesk.exceptions.QuotaExceededException;

public class WorkspaceFileCore {

    private static final String LINE_SEP = System.getProperty("line.separator");

    private String name;
    private String workspace;
    private boolean editLock = false;
    private int size = 0;

    public WorkspaceFileCore(String workspace, String name) {
        this.name = name;
        this.workspace = workspace;
    }

    //sets the content of the file in disk
    public boolean setContent(Context context, String data) throws QuotaExceededException {
        int quotaIncrease = data.length() - this.getSize();
        ArrayList<WorkspaceCore> workspaces = ((AirDeskContext) context).getWorkspaces();

        //TODO: a method to obtain a workspace by name would be nice, or, have a reference for it in the file
        for (WorkspaceCore w : workspaces)
            if (w.getName().equals(workspace) && quotaIncrease > w.getQuotaAvailable())
                throw new QuotaExceededException(quotaIncrease - w.getQuotaAvailable());

        FileOutputStream fos;
        try {
            fos = context.openFileOutput(toString(), Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            setSize(data.length());
            return true;
        } catch (FileNotFoundException e) {
            Log.e("WorkspaceFileCore", "File not found");
        } catch (IOException e) {
            Log.e("WorkspaceFileCore", "write problem");
        }
        return false;
    }

    //retrieves the content of the file from disk
    public String getContent(Context context) {
        FileInputStream fis = null;
        Scanner scanner = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = context.openFileInput(toString());
            scanner = new Scanner(fis);
            while (scanner.hasNextLine()) {
                String lineToAppend = scanner.nextLine() + LINE_SEP;
                sb.append(lineToAppend);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return "";
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e("WorkspaceFileCore", "close problem");
                }
            }
            if (scanner != null) scanner.close();
        }
    }

    public String getName() {
        return this.name;
    }

    //TODO: must be synchronized
    public boolean editLock() {
        return !editLock && (editLock = true);
    }

    public void editUnlock() {
        editLock = false;
    }

    public void removeFile(Context context) {
        context.deleteFile(toString());
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getWorkspace() {
        return workspace;
    }

    @Override
    public String toString() {
        return this.workspace + "_" + this.name;
    }
}
