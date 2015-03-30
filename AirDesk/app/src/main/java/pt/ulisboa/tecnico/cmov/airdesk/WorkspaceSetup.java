package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import pt.ulisboa.tecnico.cmov.airdesk.core.OwnedWorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;

public class WorkspaceSetup extends ActionBarActivity {

    public WorkspaceCore workspace;

    public void createNewWorkspace(View view) {
        EditText workspace_name = (EditText) findViewById(R.id.et_workspace_name);
        EditText quota_value = (EditText) findViewById(R.id.et_quota_value);
        EditText tag_value = (EditText) findViewById(R.id.et_tag);
        Spinner privacy_spinner = (Spinner) findViewById(R.id.sp_privacy);

        String name = workspace_name.getText().toString().trim();
        String quota_string = quota_value.getText().toString().trim();
        String tag = tag_value.getText().toString().trim();
        String privacy_setting = privacy_spinner.getSelectedItem().toString();

        if(name.equals("")) {
            Util.toast_warning(getApplicationContext(), "\"Workspace Name\" field is empty");

        } else if(quota_string.equals("")) {
            Util.toast_warning(getApplicationContext(), "\"Quota\" field is empty");

//        } else if (tag.equals("")) {
//                Util.toast_warning(getApplicationContext(), "\"TAG\" field is empty");
        } else {

            int quota;
            try {
                quota = Integer.parseInt(quota_string);
            } catch (Exception e) {
                Util.toast_warning(getApplicationContext(), "\"Quota\" must be a number");
                return;
            }

            boolean isPublic = privacy_setting.equals("Public");

            workspace = new OwnedWorkspaceCore(name, quota, tag, WorkspaceList.OWNER_EMAIL, isPublic);
            OwnedWorkspaceCore.workspaces.add(workspace);
            OwnedWorkspaceCore.saveWorkspaces(getApplicationContext());

            Util.launchOwnedWorkspace(WorkspaceSetup.this, OwnedWorkspace.class, workspace);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_setup);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffff4444"))); //FIXME: get color from colors
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
}
