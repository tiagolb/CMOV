package pt.ulisboa.tecnico.cmov.airdesk;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import pt.ulisboa.tecnico.cmov.airdesk.core.Client;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceFileCore;
import pt.ulisboa.tecnico.cmov.airdesk.exceptions.QuotaExceededException;


public class EditFileForeign extends ActionBarActivity {

    private WorkspaceCore workspace;
    private WorkspaceFileCore file;
    private AirDeskContext context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file_foreign);

        //set action-bar's title and background color
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff0099cc"))); //FIXME: get color from colors

        //get workspace and file objects
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            workspace = Client.getWorkspace(bundle.getString("owner"), bundle.getString("workspace"));
            file = workspace.getFile(bundle.getString("file"));
        }
        if (file != null) {
            if (!file.editLock()) {
                Util.toast_warning(getApplicationContext(), "Cannot edit file, another client is already editing it.");
                finish();
            } else {
                bar.setTitle("Edit " + file.getName());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //display file content
        if (file != null) {
            ((EditText) findViewById(R.id.edit_file_foreign_text)).setText(file.getContent(getApplicationContext()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_file_foreign, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save_file:
                try {
                    String newContent = ((EditText) findViewById(R.id.edit_file_foreign_text)).getText().toString();
                    file.setContent(getApplicationContext(), newContent);
                    Util.toast_warning(getApplicationContext(), "File saved");
                } catch (QuotaExceededException e) {
                    Util.toast_warning(getApplicationContext(), "Cannot save file, quota exceeded.");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
