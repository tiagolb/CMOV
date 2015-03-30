package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceFileCore;


public class ViewFileOwned extends ActionBarActivity {

    private WorkspaceCore workspace;
    private String file;
    private AirDeskContext context;
    WorkspaceFileCore workspaceFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file_owned);

        //get workspace and file objects
        Bundle bundle = getIntent().getExtras();
        //workspace = OwnedWorkspaceCore.getWorkspaceById(bundle.getString("workspace"));
        context = (AirDeskContext) getApplicationContext();
        String workspaceName = bundle.getString("workspace");
        workspace = context.getWorkspace(workspaceName);
        file = bundle.getString("file");
        workspaceFile = new WorkspaceFileCore(file, workspaceName);


        //set action-bar's title and background color
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffff4444"))); //FIXME: get color from colors
        bar.setTitle("View " + file);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //display file content
        ((EditText) findViewById(R.id.view_file_owned_text)).setText(workspaceFile.getContent(getApplicationContext()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_edit_file:
                Intent intent = new Intent(ViewFileOwned.this, EditFileOwned.class);
                intent.putExtra("file", workspaceFile.getName());
                intent.putExtra("workspace", workspace.getName());
                startActivity(intent);
                return true;
            case R.id.action_remove_file:
                //remove file from workspace
                workspace.removeFile(workspaceFile.getName());
                //save changes
                //OwnedWorkspaceCore.saveWorkspaces(getApplicationContext());
                //remove file from disk
                workspaceFile.removeFile(getApplicationContext());

                Util.toast_warning(getApplicationContext(), "File removed");
                finish();
                return true;
            default:
                Log.d("teste", "seta atras");
                return super.onOptionsItemSelected(item);
        }
    }
}
