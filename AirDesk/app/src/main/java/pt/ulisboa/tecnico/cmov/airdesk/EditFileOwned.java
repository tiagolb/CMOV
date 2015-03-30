package pt.ulisboa.tecnico.cmov.airdesk;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceFileCore;


public class EditFileOwned extends ActionBarActivity {

    private WorkspaceCore workspace;
    private WorkspaceFileCore file;
    private AirDeskContext context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file_owned);

        //get workspace and file objects
        Bundle bundle = getIntent().getExtras();
        //workspace = OwnedWorkspaceCore.getWorkspaceById(bundle.getString("workspace"));
        context = (AirDeskContext) getApplicationContext();
        String workspaceName = bundle.getString("workspace");
        workspace = context.getWorkspace(workspaceName);
        String fileName = bundle.getString("file");
        file = new WorkspaceFileCore(fileName, workspaceName);

        //FIXME: retrieve correct lock status
        if (!file.editLock()) {
            Util.toast_warning(getApplicationContext(), "Cannot edit file, another client is already editing it.");
            finish();
        }
        else {
            //set action-bar's title and background color
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffff4444"))); //FIXME: get color from colors
            bar.setTitle("Edit " + file.getName());

            //display file content
            ((EditText) findViewById(R.id.edit_file_owned_text)).setText(file.getContent(getApplicationContext()));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save_file:
                file.setContent(getApplicationContext(), ((EditText) findViewById(R.id.edit_file_owned_text)).getText().toString());
                Util.toast_warning(getApplicationContext(), "File saved");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
