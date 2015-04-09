package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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

import pt.ulisboa.tecnico.cmov.airdesk.core.Client;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;


public class ForeignWorkspace extends ActionBarActivity {

    private WorkspaceCore workspace = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_workspace);

        ActionBar bar = getSupportActionBar();

        Bundle bundle = getIntent().getExtras();
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
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.workspace_file_list_item, workspace.getFiles());
            fileList.setAdapter(adapter);
            registerForContextMenu(fileList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //update view
        ListView fileList = (ListView) findViewById(R.id.foreign_workspace_file_list);
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) fileList.getAdapter();
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_foreign_workspace, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_new_file:
                newFile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void newFile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Create new file");
        builder.setMessage("Please enter the file name:");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Create",
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
                String value = input.getText().toString().trim();

                if (value.equals("")) {
                    Util.toast_warning(getApplicationContext(), "You have to enter a filename.");
                } else if (workspace.hasFile(value)) {
                    Util.toast_warning(getApplicationContext(), "That file already exists.");
                } else {
                    dialog.dismiss();
                    //AirDeskContext context = (AirDeskContext) getApplicationContext();
                    //context.addFileToWorkspace(workspace, value);
                    workspace.addFile(value);
                    Util.toast_warning(getApplicationContext(), "File " + value + " created");
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.foreign_workspace_file_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            ListView list = (ListView) v;
            String item = (String) list.getAdapter().getItem(info.position);

            menu.setHeaderTitle(item);
            menu.add(Menu.NONE, 0, 0, getString(R.string.remove_file));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                ListView list = (ListView) info.targetView.getParent();
                ArrayAdapter adapter = (ArrayAdapter) list.getAdapter();
                String file = (String) adapter.getItem(info.position);
                Util.removeFile(getApplicationContext(), workspace, workspace.getFile(file));
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
