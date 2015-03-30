package pt.ulisboa.tecnico.cmov.airdesk;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import pt.ulisboa.tecnico.cmov.airdesk.core.OwnedWorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceFileCore;


public class ViewFileOwned extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file_owned);

        //get workspace and file objects
        Bundle bundle = getIntent().getExtras();
        WorkspaceCore workspace = OwnedWorkspaceCore.getWorkspaceById(bundle.getString("workspace"));
        WorkspaceFileCore file = workspace.getFile(bundle.getString("file"));

        //set action-bar's title and background color
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffff4444"))); //FIXME: get color from colors
        bar.setTitle(file.getName());

        //display file content
        ((EditText) findViewById(R.id.view_file_owned_text)).setText(file.getContent(getApplicationContext()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_file, menu);
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
