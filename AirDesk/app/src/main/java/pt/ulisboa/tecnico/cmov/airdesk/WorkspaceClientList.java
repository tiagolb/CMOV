package pt.ulisboa.tecnico.cmov.airdesk;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

//kgjdfoijdsapifjpoidsa foiçdj fi dsajp
public class WorkspaceClientList extends ActionBarActivity {

    private WorkspaceCore workspace = null;
    private AirDeskContext context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_client_list);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffff4444"))); //FIXME: get color from colors

        Bundle bundle = getIntent().getExtras();
        context = (AirDeskContext) getApplicationContext();
        if (bundle != null) {
            workspace = context.getWorkspace(bundle.getString("workspace"));
        }

        if (workspace != null) {
            bar.setTitle("Clients for " + workspace.getName());

            //populate file list
            ListView fileList = (ListView) findViewById(R.id.workspace_client_list);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.workspace_client_list_item, workspace.getClients());
            fileList.setAdapter(adapter);
            registerForContextMenu(fileList);
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
                workspace.removeClient(client);
                adapter.notifyDataSetChanged();
                Util.toast_warning(getApplicationContext(), "Removed client " + client);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
