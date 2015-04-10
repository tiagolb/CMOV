package pt.ulisboa.tecnico.cmov.airdesk;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import pt.ulisboa.tecnico.cmov.airdesk.core.Client;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceFileCore;


public class ViewFileForeign extends ActionBarActivity {

    private WorkspaceCore workspace;
    private WorkspaceFileCore file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file_foreign);

        //get workspace and file objects
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            workspace = Client.getWorkspace(bundle.getString("owner"),
                    bundle.getString("workspace"));
            file = workspace.getFile(bundle.getString("file"));
        } else if (savedInstanceState != null) {
            workspace = Client.getWorkspace(savedInstanceState.getString("owner"),
                    savedInstanceState.getString("workspace"));
            file = workspace.getFile(savedInstanceState.getString("file"));
        }
        if (file != null) {
            //set action-bar's title
            ActionBar bar = getSupportActionBar();
            bar.setTitle("View " + file.getName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //display file content
        if (file != null) {
            String content = file.getContent(getApplicationContext());
            if (content.length() == 0) content = "Empty File...";
            ((EditText) findViewById(R.id.view_file_foreign_text)).setText(content);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("workspace", workspace.getName());
        savedInstanceState.putString("file", file.getName());
        savedInstanceState.putString("owner", workspace.getOwner());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_file_foreign, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_edit_file:
                Util.launchEditFileForeign(ViewFileForeign.this, workspace, file);
                return true;
            case R.id.action_remove_file:
                Util.removeFile(getApplicationContext(), workspace, file);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
