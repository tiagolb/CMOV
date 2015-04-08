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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import pt.ulisboa.tecnico.cmov.airdesk.adapters.FileListAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;


public class OwnedWorkspace extends ActionBarActivity {

    private WorkspaceCore workspace = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_workspace);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffff4444"))); //FIXME: get color from colors

        Bundle bundle = getIntent().getExtras();
        AirDeskContext context = (AirDeskContext) getApplicationContext();
        if (bundle != null) {
            workspace = context.getWorkspace(bundle.getString("workspaceName"));
        } else if (savedInstanceState != null) {
            workspace = context.getWorkspace(savedInstanceState.getString("workspaceName"));
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

        savedInstanceState.putString("workspaceName", workspace.getName());
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
                return true;
            case R.id.action_change_quota:
                changeQuota();
                return true;
            case R.id.action_tag_list:
                manageTagList();
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
                    AirDeskContext context = (AirDeskContext) getApplicationContext();
                    context.addFileToWorkspace(workspace, value);
                    //workspace.addFile(value);
                    Util.toast_warning(getApplicationContext(), "File " + value + " created");
                    //OwnedWorkspaceCore.saveWorkspaces(getApplicationContext());
                }
            }
        });
    }

    public void invite() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Invite client");
        builder.setMessage("Please enter the client's e-mail address:");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

        builder.setPositiveButton("Invite",
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
                    Util.toast_warning(getApplicationContext(), "You have to enter an e-mail address.");
                } else if (workspace.hasClient(value)) {
                    Util.toast_warning(getApplicationContext(), "That client already has access.");
                } else {
                    dialog.dismiss();
                    workspace.addClient(value);
                    Util.toast_warning(getApplicationContext(), value + " added to clients list.");
                }
            }
        });
    }

    public void changeQuota() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Change quota");
        builder.setMessage("Enter the new quota value (Bytes):");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(workspace.getQuota() / 1048576));
        builder.setView(input);

        builder.setPositiveButton("Save",
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

                try {
                    int quota = Integer.parseInt(value);//* 1024; //quota is stored in bytes
                    if (quota < workspace.getQuotaUsed()) {
                        Util.toast_warning(getApplicationContext(), "You cannot set a new quota lower than the current used quota: " + workspace.getQuotaUsed() + " bytes");
                    } else {
                        dialog.dismiss();
                        AirDeskContext context = (AirDeskContext) getApplicationContext();
                        context.setWorkspaceQuota(workspace, quota);
                        //workspace.setQuota(quota);
                        Util.toast_warning(getApplicationContext(), "New quota set.");
                    }
                } catch (Exception NumberFormatException) {
                    Util.toast_warning(getApplicationContext(), "You must enter a number.");
                }
            }
        });
    }

    //TODO: we could have an activity for this
    public void manageTagList() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Manage tags");
        alert.setMessage("Enter the workspace's tags:");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setText(workspace.getTagsString());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().trim();
                AirDeskContext context = (AirDeskContext) getApplicationContext();
                context.setWorkspaceTags(workspace, value);
                Util.toast_warning(getApplicationContext(), "Tags saved.");
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
    }
}
