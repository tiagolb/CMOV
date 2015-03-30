package pt.ulisboa.tecnico.cmov.airdesk.core;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Francisco on 29-03-2015.
 */
public class WorkspaceFileCore {

    private static final String LINE_SEP = System.getProperty("line.separator");

    private String name;
    private String workspace;

    public WorkspaceFileCore(String name, String workspace) {
        this.name = name;
        this.workspace = workspace;
    }

    //sets the content of the file in disk
    public boolean setContent(String data, Context context) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(workspace + "_" + name, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
        } catch (FileNotFoundException e) {
            Log.e("WorkspaceFileCore", "File not found");
            return false;
        } catch (IOException e) {
            Log.e("WorkspaceFileCore", "write problem");
            return false;
        }
        return true;
    }

    //retrieves the content of the file from disk
    public String getContent(Context context) {
        FileInputStream fis = null;
        Scanner scanner = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = context.openFileInput(this.workspace + "_" + this.name);
            scanner = new Scanner(fis);
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine() + LINE_SEP);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            Log.e("WorkspaceFileCore", "File not found");
            return "Empty File...";
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e("WorkspaceFileCore", "close problem");
                    return null;
                }
            }
            if(scanner != null) {
                scanner.close();
            }
        }
    }

    public String getName() {
        return this.name;
    }

    //TODO: remove file
}
