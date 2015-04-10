package pt.ulisboa.tecnico.cmov.airdesk;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceFileCore;
import pt.ulisboa.tecnico.cmov.airdesk.exceptions.QuotaExceededException;


public class EditFileOwned extends ActionBarActivity {

    private WorkspaceFileCore file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file_owned);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            AirDeskContext context = (AirDeskContext) getApplicationContext();
            WorkspaceCore workspace = context.getWorkspace(bundle.getString("workspace"));
            file = workspace.getFile(bundle.getString("file"));

            if (!file.editLock()) {
                Util.toast(getApplicationContext(), "Cannot edit file, another client is already editing it.");
                finish();
            } else {
                //set action-bar's title
                ActionBar bar = getSupportActionBar();
                bar.setTitle("Edit " + file.getName());

                //display file content
                ((EditText) findViewById(R.id.edit_file_owned_text)).setText(file.getContent(getApplicationContext()));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save_file:
                try {
                    String newContent = ((EditText) findViewById(R.id.edit_file_owned_text)).getText().toString();
                    file.setContent(getApplicationContext(), newContent);
                    Util.toast(getApplicationContext(), "File saved");
                } catch (QuotaExceededException e) {
                    Util.toast(getApplicationContext(), "Cannot save file, quota exceeded.");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
