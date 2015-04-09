package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import pt.ulisboa.tecnico.cmov.airdesk.adapters.WorkspaceAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;

public class WorkspaceList extends ActionBarActivity {

    public void setupNewWorkspace(View view) {
        Intent intent = new Intent(this, WorkspaceSetup.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_list);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String ownerNick = prefs.getString("nick", "");
        String ownerEmail = prefs.getString("email", "");

        if (ownerNick.equals("") || ownerEmail.equals("")) {
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        AirDeskContext context = (AirDeskContext) getApplicationContext();
        context.initContext(ownerEmail);

        populateWorkspaceLists();
    }

    private void populateWorkspaceLists() {

        //owned workspace list
        ListView onwedWorkspaceList = (ListView) findViewById(R.id.owned_workspace_list);
        WorkspaceAdapter onwedWorkspaceAdapter = new WorkspaceAdapter(this,
                R.layout.workspace_list_item, AirDeskContext.getContext().getWorkspaces());

        onwedWorkspaceList.setAdapter(onwedWorkspaceAdapter);
        registerForContextMenu(onwedWorkspaceList);

        onwedWorkspaceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkspaceCore workspace = (WorkspaceCore) parent.getAdapter().getItem(position);
                Util.launchOwnedWorkspace(WorkspaceList.this, OwnedWorkspace.class, workspace);
            }
        });

        //foreign workspace list
        ListView foreignWorkspaceList = (ListView) findViewById(R.id.foreign_workspace_list);
        WorkspaceAdapter foreignWorkspaceAdapter = new WorkspaceAdapter(this,
                R.layout.workspace_list_item, AirDeskContext.getContext().getMountedWorkspaces());

        foreignWorkspaceList.setAdapter(foreignWorkspaceAdapter);
        registerForContextMenu(foreignWorkspaceList);

        foreignWorkspaceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkspaceCore workspace = (WorkspaceCore) parent.getAdapter().getItem(position);
                Intent intent = new Intent(WorkspaceList.this, ForeignWorkspace.class);
                intent.putExtra("workspace", workspace.getName());
                intent.putExtra("owner", workspace.getOwner());
                startActivity(intent);
            }
        });
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
            menu.add(Menu.NONE, 0, 0, getString(R.string.delete));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                AdapterView.AdapterContextMenuInfo info =
                        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                ListView list = (ListView) info.targetView.getParent();
                ArrayAdapter adapter = (ArrayAdapter) list.getAdapter();
                WorkspaceCore workspace = (WorkspaceCore) adapter.getItem(info.position);

                AirDeskContext.getContext().removeWorkspace(workspace.getName());
                adapter.notifyDataSetChanged();
                Util.toast_warning(getApplicationContext(), getString(R.string.workspace_deleted) +
                        ": " + workspace.getName());

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void subscribe(View view) {
        Util.subscribe(this, getApplicationContext());
    }

    public void manage_tags(View view) {
        Intent intent = new Intent(this, SubscribedTags.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}
