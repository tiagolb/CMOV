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
import pt.ulisboa.tecnico.cmov.airdesk.core.ForeignWorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.OwnedWorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;


public class WorkspaceList extends ActionBarActivity {

    public static String OWNER_NICKNAME;
    public static String OWNER_EMAIL;

    public void setupNewWorkspace(View view) {
        Intent intent = new Intent(this, WorkspaceSetup.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String nick = prefs.getString("nick", "");
        String email = prefs.getString("email", "");

        if (nick.equals("") || email.equals("")) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        /*
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();

            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        */


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_list);

        ListView ownedWorkspacesList = (ListView) findViewById(R.id.owned_workspace_list);
        ownedWorkspacesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                WorkspaceCore workspace = (WorkspaceCore) parent.getAdapter().getItem(position);
                Util.launchOwnedWorkspace(WorkspaceList.this, OwnedWorkspace.class, workspace);
            }
        });


        // populate the ListView
        OwnedWorkspaceCore.loadWorkspaces(getApplicationContext());
        ListView onwedWorkspaceList = (ListView) findViewById(R.id.owned_workspace_list);
        WorkspaceAdapter onwedWorkspaceAdapter = new WorkspaceAdapter(this, R.layout.workspace_list_item, OwnedWorkspaceCore.workspaces);
        onwedWorkspaceList.setAdapter(onwedWorkspaceAdapter);
        registerForContextMenu(onwedWorkspaceList);

        ForeignWorkspaceCore.loadWorkspaces(getApplicationContext());
        ListView foreignWorkspaceList = (ListView) findViewById(R.id.foreign_workspace_list);
        WorkspaceAdapter foreignWorkspaceAdapter = new WorkspaceAdapter(this, R.layout.workspace_list_item, ForeignWorkspaceCore.workspaces);
        foreignWorkspaceList.setAdapter(foreignWorkspaceAdapter);
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

            ListView list = (ListView)v;
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

                OwnedWorkspaceCore.workspaces.remove(workspace);
                adapter.notifyDataSetChanged();
                OwnedWorkspaceCore.saveWorkspaces(getApplicationContext());
                Util.toast_warning(getApplicationContext(), "Deleted workspace " + workspace.getName());

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
