package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;


public class WorkspaceList extends ActionBarActivity {

    public static List<WorkspaceCore> WORKSPACE_LIST = new ArrayList<WorkspaceCore>();
    public static String OWNER_NICKNAME;
    public static String OWNER_EMAIL;

    public void setupNewWorkspace(View view) {
        Intent intent = new Intent(this, WorkspaceSetup.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String nick = prefs.getString("nick", "");
        String email = prefs.getString("email", "");

        if (nick.equals("") || email.equals("")) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        /*
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();

            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        */

        // populate the ListView each time we go to this activity
        // should this be done in the onCreate method?
        List<String> workspaceNames = new ArrayList<String>();
        for(WorkspaceCore w : WORKSPACE_LIST) {
            workspaceNames.add(w.getName());
        }
        ListView list = (ListView) findViewById(R.id.owned_workspace_list);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.workspace_list_item, workspaceNames);
        list.setAdapter(listAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workspace_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_change_login:
                startActivity(new Intent(this, Login.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
