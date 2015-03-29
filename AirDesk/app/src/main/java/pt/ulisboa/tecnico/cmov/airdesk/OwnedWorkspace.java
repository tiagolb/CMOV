package pt.ulisboa.tecnico.cmov.airdesk;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class OwnedWorkspace extends ActionBarActivity {

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, WorkspaceList.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_workspace);

        Bundle bundle = getIntent().getExtras();
        String workspaceName = bundle.getString("workspace"); // it must always be defined

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffff4444"))); //FIXME: get color from colors
        if (workspaceName != null) {
            bar.setTitle(workspaceName);
        }
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
                return true;
            case R.id.action_new_file:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
