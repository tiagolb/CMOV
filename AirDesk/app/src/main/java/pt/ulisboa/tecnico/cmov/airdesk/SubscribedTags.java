package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class SubscribedTags extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_tags);

        //populate tag list
        ListView fileList = (ListView) findViewById(R.id.subscribed_tags_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.subscribed_tag_list_item, AirDeskContext.getContext().getSubscribedTags());
        fileList.setAdapter(adapter);
        registerForContextMenu(fileList);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //update view
        ListView fileList = (ListView) findViewById(R.id.subscribed_tags_list);
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) fileList.getAdapter();
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subscribed_tags, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add_tag:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String ownerEmail = prefs.getString("email", "");

                Util.subscribe(this, getApplicationContext(), ownerEmail);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.subscribed_tags_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            ListView list = (ListView) v;
            String tag = (String) list.getAdapter().getItem(info.position);

            menu.setHeaderTitle(tag);
            menu.add(Menu.NONE, 0, 0, getString(R.string.remove_tag));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                ListView list = (ListView) info.targetView.getParent();
                ArrayAdapter adapter = (ArrayAdapter) list.getAdapter();

                String tag = (String) adapter.getItem(info.position);
                AirDeskContext.getContext().removeSubscribedTag(tag);

                adapter.notifyDataSetChanged();
                Util.toast(getApplicationContext(), getString(R.string.tag_removed) + ": " + tag);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
