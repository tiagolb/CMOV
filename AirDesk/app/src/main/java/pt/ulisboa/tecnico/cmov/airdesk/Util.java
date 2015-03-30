package pt.ulisboa.tecnico.cmov.airdesk;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;

/**
 * Created by tiago on 26-03-2015.
 */
public class Util {

    public static void toast_warning(Context context, String warning) {
        Toast.makeText(context, warning, Toast.LENGTH_SHORT).show();
    }

    public static void launchOwnedWorkspace(Context context, Class<?> cls, WorkspaceCore workspace) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("workspaceName", workspace.getName());
        context.startActivity(intent);
    }
}
