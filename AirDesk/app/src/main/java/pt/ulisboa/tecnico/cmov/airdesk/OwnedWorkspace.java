package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;


public class OwnedWorkspace extends ActionBarActivity {

    private WorkspaceCore workspace = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_workspace);

        Bundle bundle = getIntent().getExtras();
        AirDeskContext context = (AirDeskContext) getApplicationContext();
        if (bundle != null) {
            workspace = context.getWorkspace(bundle.getString("workspaceName"));
        } else if (savedInstanceState != null) {
            workspace = context.getWorkspace(savedInstanceState.getString("workspaceName"));
        }
        if (workspace != null) {
            ActionBar bar = getSupportActionBar();
            bar.setTitle(workspace.getName());

            //file click handler
            ListView fileList = (ListView) findViewById(R.id.owned_workspace_file_list);
            fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(OwnedWorkspace.this, ViewFileOwned.class);
                    intent.putExtra("file", (String) parent.getAdapter().getItem(position));
                    intent.putExtra("workspace", workspace.getName());
                    startActivity(intent);
                }
            });

            //populate file list
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.workspace_file_list_item,
                    workspace.getFiles());
            fileList.setAdapter(adapter);
            registerForContextMenu(fileList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //update view
        ListView fileList = (ListView) findViewById(R.id.owned_workspace_file_list);
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) fileList.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("workspaceName", workspace.getName());
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
                Util.inviteClient(this, getApplicationContext(), workspace);
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
            case R.id.action_change_privacy:
                changePrivacy();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changePrivacy() {
        final AlertDialog dialog = Util.createDialog(this,
                getString(R.string.change_privacy),
                getString(R.string.this_workspace_is) + ": " +
                        (workspace.isPublic() ?
                                getString(R.string.privacy_public) :
                                getString(R.string.privacy_private)) + "\n\n" +
                        getString(R.string.update_it_to) + ":",
                getString(R.string.privacy_public),
                getString(R.string.privacy_private), null);

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workspace.setPublic();
                Util.toast(getString(R.string.workspace_pricacy_set_public));
                dialog.dismiss();
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workspace.setPrivate();
                Util.toast(getString(R.string.workspace_pricacy_set_private));
                dialog.dismiss();
            }
        });
    }

    public void newFile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.create_new_file));
        builder.setMessage(getString(R.string.enter_file_name) + ":");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.create),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
                    Util.toast(getApplicationContext(),
                            getString(R.string.must_enter_filename));
                } else if (workspace.hasFile(value)) {
                    Util.toast(getApplicationContext(),
                            getString(R.string.file_already_exists));
                } else {
                    dialog.dismiss();
                    workspace.addFile(value);
                    Util.toast(getApplicationContext(), getString(R.string.file) + " " +
                            value + " " + getString(R.string.created));
                }
            }
        });
    }

    public void changeQuota() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.change_quota));
        builder.setMessage(getString(R.string.enter_new_quota));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(workspace.getQuota()));
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.save),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
                    int quota = Integer.parseInt(value); //quota is stored in bytes
                    if (quota < workspace.getQuotaUsed()) {
                        Util.toast(getApplicationContext(),
                                getString(R.string.cannot_set_lower_quota) +
                                        workspace.getQuotaUsed() + " " + getString(R.string.bytes));
                    } else {
                        dialog.dismiss();
                        workspace.setQuota(quota);
                        Util.toast(getApplicationContext(),
                                getString(R.string.new_quota_set));
                    }
                } catch (NumberFormatException e) {
                    Util.toast(getApplicationContext(),
                            getString(R.string.must_enter_a_number));
                }
            }
        });
    }

    //TODO: we could have an activity for this
    public void manageTagList() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(getString(R.string.manage_tags));
        alert.setMessage(getString(R.string.enter_tags));

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setText(workspace.getTagsString());
        alert.setView(input);

        alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().trim();
                workspace.setTags(value);
                Util.toast(getApplicationContext(), getString(R.string.tags_saved));
            }
        });

        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.owned_workspace_file_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            ListView list = (ListView) v;
            String item = (String) list.getAdapter().getItem(info.position);

            menu.setHeaderTitle(item);
            menu.add(Menu.NONE, 0, 0, getString(R.string.edit_file));
            menu.add(Menu.NONE, 0, 1, getString(R.string.remove_file));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ListView list = (ListView) info.targetView.getParent();
        ArrayAdapter adapter = (ArrayAdapter) list.getAdapter();
        String file = (String) adapter.getItem(info.position);

        switch (item.getItemId()) {
            case 0:
                Util.launchEditFileOwned(this, workspace, workspace.getFile(file));
                return true;
            case 1:
                Util.removeFile(getApplicationContext(), workspace, workspace.getFile(file));
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
