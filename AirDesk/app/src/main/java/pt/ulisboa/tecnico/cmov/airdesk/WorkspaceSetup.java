package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import pt.ulisboa.tecnico.cmov.airdesk.core.OwnedWorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;

public class WorkspaceSetup extends ActionBarActivity {

    private WorkspaceCore workspace;

    public void createNewWorkspace(View view) {
        String workspaceName = ((EditText) findViewById(R.id.et_workspace_name)).getText().toString().trim();
        String quotaString = ((EditText) findViewById(R.id.et_quota_value)).getText().toString().trim();
        String tags = ((EditText) findViewById(R.id.et_tag)).getText().toString().trim();
        String privacy = ((Spinner) findViewById(R.id.sp_privacy)).getSelectedItem().toString();

        if (workspaceName.equals("")) {
            Util.toast_warning(getApplicationContext(), "Enter a workspace name.");
        } else if (quotaString.equals("")) {
            Util.toast_warning(getApplicationContext(), "Enter the quota value.");
        } else {
            int quota;
            try {
                quota = Integer.parseInt(quotaString) * 1048576; //quota is stored in bytes
            } catch (Exception NumberFormatException) {
                Util.toast_warning(getApplicationContext(), "Quota must be a number");
                return;
            }

            boolean isPublic = privacy.equals("Public");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String ownerEmail = prefs.getString("email", "");
            Log.d("TAG", ownerEmail);

            workspace = new OwnedWorkspaceCore(workspaceName, quota, tags, ownerEmail, isPublic);
            AirDeskContext context = (AirDeskContext) getApplicationContext();
            context.addWorkspace(workspace);

            Util.launchOwnedWorkspace(WorkspaceSetup.this, OwnedWorkspace.class, workspace);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_setup);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workspace_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }
}
