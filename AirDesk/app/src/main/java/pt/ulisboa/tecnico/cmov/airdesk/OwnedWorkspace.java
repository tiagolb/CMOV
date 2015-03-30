package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import pt.ulisboa.tecnico.cmov.airdesk.adapters.FileListAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.core.OwnedWorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;


public class OwnedWorkspace extends ActionBarActivity {

    private WorkspaceCore workspace = null;
    private AirDeskContext context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_workspace);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffff4444"))); //FIXME: get color from colors

        Bundle bundle = getIntent().getExtras();
        context = (AirDeskContext) getApplicationContext();
        if (bundle != null) {
            //workspace = OwnedWorkspaceCore.workspaces.get(bundle.getInt("workspaceIndex"));
            workspace = context.getWorkspace(bundle.getString("workspaceName"));
        }
        /*else if (savedInstanceState != null) { //FIXME: it's always null
            workspace = OwnedWorkspaceCore.getWorkspaceById(bundle.getString("workspace"));
        }*/
        if (workspace != null) {
            bar.setTitle(workspace.getName());

            //file click handler
            ListView fileList = (ListView) findViewById(R.id.owned_workspace_file_list);
            fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(OwnedWorkspace.this, ViewFileOwned.class);
                    //intent.putExtra("file", (String) parent.getAdapter().getItem(position));
                    intent.putExtra("file", (String) parent.getAdapter().getItem(position));
                    intent.putExtra("workspace", workspace.getName());
                    startActivity(intent);
                }
            });

            //populate file list
            FileListAdapter adapter = new FileListAdapter(this, R.layout.workspace_file_list_item, workspace.getFiles());
            fileList.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //update view
        ListView fileList = (ListView) findViewById(R.id.owned_workspace_file_list);
        FileListAdapter adapter = (FileListAdapter) fileList.getAdapter();
        adapter.notifyDataSetChanged();
    }

    // TODO
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //savedInstanceState.putString("workspace", workspace.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owned_workspace, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_invite:
                invite();
                return true;
            case R.id.action_new_file:
                newFile();
                return true;
            case R.id.action_client_list:
                Intent intent = new Intent(OwnedWorkspace.this, WorkspaceClientList.class);
                intent.putExtra("workspace", workspace.getName());
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void newFile() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Create new file");
        alert.setMessage("Please enter the file name:");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                if (value.equals("")) {
                    Util.toast_warning(getApplicationContext(), "You have to enter a filename.");
                }
                else if (workspace.hasFile(value)) {
                    Util.toast_warning(getApplicationContext(), "That file already exists.");
                }
                else {
                    Util.toast_warning(getApplicationContext(), "Creating file " + value + "...");
                    workspace.addFile(value);
                    OwnedWorkspaceCore.saveWorkspaces(getApplicationContext());
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public void invite() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Invite client");
        alert.setMessage("Please enter the client's e-mail address:");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                workspace.addClient(value);
                OwnedWorkspaceCore.saveWorkspaces(getApplicationContext());
                Util.toast_warning(getApplicationContext(), value + " added to clients list.");
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}
