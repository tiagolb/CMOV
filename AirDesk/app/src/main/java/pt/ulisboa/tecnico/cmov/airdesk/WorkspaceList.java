package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.adapters.WorkspaceAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.core.Client;
import pt.ulisboa.tecnico.cmov.airdesk.core.Server;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;

public class WorkspaceList extends ActionBarActivity {
    private AirDeskContext context;
    private String ownerNick;
    private String ownerEmail;

    public void setupNewWorkspace(View view) {
        Intent intent = new Intent(this, WorkspaceSetup.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        ownerNick = prefs.getString("nick", "");
        ownerEmail = prefs.getString("email", "");

        if (ownerNick.equals("") || ownerEmail.equals("")) {
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            // FIXME: Aqui deve haver uma maneira melhor de fazer isto
            ownerNick = prefs.getString("nick", "");
            ownerEmail = prefs.getString("email", "");
            populateWorkspaceLists();
        }
    }

    private void populateWorkspaceLists() {
        Server.context = (AirDeskContext) getApplicationContext();
        Server.context.initContext(ownerEmail);

        ListView ownedWorkspacesList = (ListView) findViewById(R.id.owned_workspace_list);
        ownedWorkspacesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // Does this work? sim, é quando clicas num workspace, ele abre o workspace
                WorkspaceCore workspace = (WorkspaceCore) parent.getAdapter().getItem(position);
                Util.launchOwnedWorkspace(WorkspaceList.this, OwnedWorkspace.class, workspace);
            }
        });


        // populate the ListView
        //OwnedWorkspaceCore.loadWorkspaces(getApplicationContext());
        context = (AirDeskContext) getApplicationContext();
        ListView onwedWorkspaceList = (ListView) findViewById(R.id.owned_workspace_list);
        //WorkspaceAdapter onwedWorkspaceAdapter = new WorkspaceAdapter(this, R.layout.workspace_list_item, OwnedWorkspaceCore.workspaces);
        WorkspaceAdapter onwedWorkspaceAdapter = new WorkspaceAdapter(this, R.layout.workspace_list_item, context.getWorkspaces());

        onwedWorkspaceList.setAdapter(onwedWorkspaceAdapter);
        registerForContextMenu(onwedWorkspaceList);

        //foreign workspace list
        ListView foreignWorkspaceList = (ListView) findViewById(R.id.foreign_workspace_list);

        //when user clicks foreign workspace
        foreignWorkspaceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                WorkspaceCore workspace = (WorkspaceCore) parent.getAdapter().getItem(position);
                Intent intent = new Intent(WorkspaceList.this, ForeignWorkspace.class);
                intent.putExtra("workspace", workspace.getName());
                intent.putExtra("owner", workspace.getOwner());
                startActivity(intent);
            }
        });

        //populate foreign workspace list
        WorkspaceAdapter foreignWorkspaceAdapter = new WorkspaceAdapter(this,
                R.layout.workspace_list_item,
                context.getMountedWorkspaces());
        foreignWorkspaceList.setAdapter(foreignWorkspaceAdapter);

        registerForContextMenu(foreignWorkspaceList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_list);

        // Aqui acho que nao e preciso isto
        //populateWorkspaceLists();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workspace_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_change_login:
                startActivity(new Intent(this, Login.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.owned_workspace_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            ListView list = (ListView) v;
            WorkspaceCore workspace = (WorkspaceCore) list.getAdapter().getItem(info.position);

            menu.setHeaderTitle(workspace.getName());
            String[] menuItems = {"Delete"};
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                ListView list = (ListView) info.targetView.getParent();
                ArrayAdapter adapter = (ArrayAdapter) list.getAdapter();
                WorkspaceCore workspace = (WorkspaceCore) adapter.getItem(info.position);

                //OwnedWorkspaceCore.workspaces.remove(workspace);
                context.removeWorkspace(workspace.getName());
                adapter.notifyDataSetChanged();
                //OwnedWorkspaceCore.saveWorkspaces(getApplicationContext());
                Util.toast_warning(getApplicationContext(), "Deleted workspace " + workspace.getName());

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void subscribe(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Subscribe to a Workspace");
        builder.setMessage("Please enter a tag:");

        final EditText tagInput = new EditText(this);
        builder.setView(tagInput);

        builder.setPositiveButton("Subscribe",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = tagInput.getText().toString().trim();
                if (tag.equals("")) {
                    Util.toast_warning(getApplicationContext(), "You have to enter a tag.");
                } else {
                    dialog.dismiss();
                    List<WorkspaceCore> workspacesWithTag = context.getWorkspacesWithTag(tag);
                    if (workspacesWithTag.isEmpty()) {
                        Util.toast_warning(getApplicationContext(), "No Workspace with such tag exists");
                    } else {
                        for (WorkspaceCore workspace : workspacesWithTag) {
                            context.addMountedWorkspace(workspace);
                        }
                        Util.toast_warning(getApplicationContext(), "SUBSCRIBE");
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {}
}
