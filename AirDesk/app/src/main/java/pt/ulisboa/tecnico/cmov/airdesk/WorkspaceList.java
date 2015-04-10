package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import pt.ulisboa.tecnico.cmov.airdesk.adapters.WorkspaceAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.core.OwnedWorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;

public class WorkspaceList extends ActionBarActivity {

    public void setupNewWorkspace(View view) {
        Intent intent = new Intent(this, WorkspaceSetup.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();

    }

    private void updateView() {
        //update view
        ListView ownedList = (ListView) findViewById(R.id.owned_workspace_list);
        ArrayAdapter<String> ownedAdapter = (ArrayAdapter<String>) ownedList.getAdapter();
        ownedAdapter.notifyDataSetChanged();

        ListView foreignList = (ListView) findViewById(R.id.foreign_workspace_list);
        ArrayAdapter<String> foreignAdapter = (ArrayAdapter<String>) foreignList.getAdapter();
        foreignAdapter.notifyDataSetChanged();
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


        boolean newLogin = getIntent().hasExtra("newLogin");
        AirDeskContext context = (AirDeskContext) getApplicationContext();
        context.initContext(ownerEmail, newLogin);

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
            case R.id.action_manage_subscriptions:
                manageTags(null);
                return true;
            case R.id.action_populate:
                populateDB();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateDB() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // This is to allow only one populate
        if(prefs.getBoolean("populateAlreadyCalled", false)) {
            return;
        }
        String ownerEmail = prefs.getString("email", "");

        AirDeskContext airDesk = AirDeskContext.getContext();

        String workspace1_name = "Populated Workspace 1";
        int workspace1_quota = 1024;
        String workspace1_tags = "workspace1_tag";
        boolean workspace1_privacy = true;
        WorkspaceCore workspace1 = new OwnedWorkspaceCore(workspace1_name,
                workspace1_quota, workspace1_tags, ownerEmail, workspace1_privacy);
        airDesk.addWorkspace(workspace1);

        String workspace2_name = "Populated Workspace 2";
        int workspace2_quota = 32;
        String workspace2_tags = "workspace2_tag";
        boolean workspace2_privacy = true;
        WorkspaceCore workspace2 = new OwnedWorkspaceCore(workspace2_name,
                workspace2_quota, workspace2_tags, ownerEmail, workspace2_privacy);
        airDesk.addWorkspace(workspace2);

        String workspace3_name = "Populated Workspace 3";
        int workspace3_quota = 1024;
        String workspace3_tags = "workspace2_tag";
        boolean workspace3_privacy = false;
        WorkspaceCore workspace3 = new OwnedWorkspaceCore(workspace3_name,
                workspace3_quota, workspace3_tags, ownerEmail, workspace3_privacy);
        airDesk.addWorkspace(workspace3);

        String workspace4_name = "Populated Workspace 4";
        int workspace4_quota = 1024;
        String workspace4_tags = "workspace4_tag";
        boolean workspace4_privacy = true;
        WorkspaceCore workspace4 = new OwnedWorkspaceCore(workspace4_name,
                workspace4_quota, workspace4_tags, ownerEmail, workspace4_privacy);
        airDesk.addWorkspace(workspace4);

        workspace1.addClient(ownerEmail);
        airDesk.addTagToSubscribedTags(workspace2_tags, ownerEmail);

        String filename1 = "file1.txt";
        workspace1.addFile(filename1);

        String filename2 = "file2.txt";
        workspace1.addFile(filename2);

        String filename3 = "file3.txt";
        workspace2.addFile(filename3);

        String filename4 = "file4.txt";
        workspace3.addFile(filename4);

        String filename5 = "file5.txt";
        workspace4.addFile(filename5);

        // This is to allow only one populate
        prefs.edit().putBoolean("populateAlreadyCalled", true).apply();

        airDesk.initContext(ownerEmail, false);
        populateWorkspaceLists();
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
                Util.toast(getApplicationContext(), getString(R.string.workspace_deleted) +
                        ": " + workspace.getName());

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void subscribe(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String ownerEmail = prefs.getString("email", "");

        Util.subscribe(this, getApplicationContext(), ownerEmail);
    }

    public void manageTags(View view) {
        Intent intent = new Intent(this, SubscribedTags.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}
