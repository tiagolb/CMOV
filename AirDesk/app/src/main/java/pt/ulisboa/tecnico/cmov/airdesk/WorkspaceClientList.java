package pt.ulisboa.tecnico.cmov.airdesk;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;

public class WorkspaceClientList extends ActionBarActivity {

    private WorkspaceCore workspace = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_client_list);

        ActionBar bar = getSupportActionBar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            AirDeskContext context = (AirDeskContext) getApplicationContext();
            workspace = context.getWorkspace(bundle.getString("workspace"));
        }

        if (workspace != null) {
            bar.setTitle("Clients for " + workspace.getName());

            //populate file list
            ListView clientList = (ListView) findViewById(R.id.workspace_client_list);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.workspace_client_list_item, workspace.getClients());
            clientList.setAdapter(adapter);
            registerForContextMenu(clientList);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workspace_client_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_invite:
                Util.inviteClient(this, getApplicationContext(), workspace);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.workspace_client_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            ListView list = (ListView) v;
            String client = (String) list.getAdapter().getItem(info.position);

            menu.setHeaderTitle(client);
            menu.add(Menu.NONE, 0, 0, "Remove access");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                ListView list = (ListView) info.targetView.getParent();
                ArrayAdapter adapter = (ArrayAdapter) list.getAdapter();
                String client = (String) adapter.getItem(info.position);
                //AirDeskContext context = (AirDeskContext) getApplicationContext();
                //context.removeClientFromWorkspace(workspace, client);
                workspace.removeClient(client);
                adapter.notifyDataSetChanged();
                Util.toast_warning(getApplicationContext(), "Removed client " + client);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
