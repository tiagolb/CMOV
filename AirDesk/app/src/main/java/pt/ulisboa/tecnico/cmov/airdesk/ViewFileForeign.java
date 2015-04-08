package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
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


public class ViewFileForeign extends ActionBarActivity {

    private WorkspaceCore workspace;
    private WorkspaceFileCore file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file_foreign);

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
            bar.setTitle("View " + file.getName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //display file content
        if (file != null) {
            ((EditText) findViewById(R.id.view_file_foreign_text)).setText(file.getContent(getApplicationContext()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_file_foreign, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_edit_file:
                Intent intent = new Intent(ViewFileForeign.this, EditFileForeign.class);
                intent.putExtra("owner", workspace.getOwner());
                intent.putExtra("workspace", workspace.getName());
                intent.putExtra("file", file.getName());
                startActivity(intent);
                return true;
            case R.id.action_remove_file:
                workspace.removeFile(file.getName());
                file.removeFile(getApplicationContext());
                Util.toast_warning(getApplicationContext(), "File removed");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
