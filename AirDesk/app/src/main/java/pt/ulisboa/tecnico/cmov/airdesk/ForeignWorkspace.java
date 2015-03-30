package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import pt.ulisboa.tecnico.cmov.airdesk.adapters.FileListAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.core.Client;
import pt.ulisboa.tecnico.cmov.airdesk.core.Server;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;


public class ForeignWorkspace extends ActionBarActivity {

    private WorkspaceCore workspace = null;
    private AirDeskContext context;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, WorkspaceList.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_workspace);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff0099cc"))); //FIXME: get color from colors

        Bundle bundle = getIntent().getExtras();
        context = (AirDeskContext) getApplicationContext();
        if (bundle != null) {
            workspace = Client.getWorkspace(bundle.getString("owner"), bundle.getString("workspace"));
        }
        if (workspace != null) {
            bar.setTitle(workspace.getName());
            //file click handler
            ListView fileList = (ListView) findViewById(R.id.foreign_workspace_file_list);

            fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(ForeignWorkspace.this, ViewFileForeign.class);
                    intent.putExtra("owner", workspace.getOwner());
                    intent.putExtra("workspace", workspace.getName());
                    intent.putExtra("file", (String) parent.getAdapter().getItem(position));
                    startActivity(intent);
                }
            });

            //populate file list
            FileListAdapter adapter = new FileListAdapter(this, R.layout.workspace_file_list_item, workspace.getFiles());
            fileList.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_foreign_workspace, menu);
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
}
