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
public class WorkspaceFile {

    private static final String LINE_SEP = System.getProperty("line.separator");

    private String name;
    private String text;
    private String workspaceName;
    private Context context;

    public WorkspaceFile(String name, String workspaceName, Context context) {
        this.name = name;
        this.workspaceName = workspaceName;
        this.context = context;
        readFile(name, workspaceName);
    }

    public WorkspaceFile(String name, String text, String workspaceName, Context context) {
        this.name = name;
        this. text = text;
        this.workspaceName = workspaceName;
        this.context = context;
    }

    public boolean writeFile(){
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(workspaceName + "_" + name, Context.MODE_PRIVATE);
            fos.write(this.text.getBytes());
        } catch (FileNotFoundException e) {
            Log.e("WorkspaceFile", "File not found");
            return false;
        } catch (IOException e) {
            Log.e("WorkspaceFile", "write problem");
            return false;
        }
        return true;
    }

    public boolean readFile(String name, String workspaceName) {
        FileInputStream fis = null;
        Scanner scanner = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = context.openFileInput(workspaceName + "_" + name);
            scanner = new Scanner(fis);
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine() + LINE_SEP);
            }
        } catch (FileNotFoundException e) {
            Log.e("WorkspaceFile", "File not found");
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e("WorkspaceFile", "close problem");
                    return false;
                }
            }
            if(scanner != null) {
                scanner.close();
            }
            this.text = sb.toString();
        }
        return true;
    }
}
