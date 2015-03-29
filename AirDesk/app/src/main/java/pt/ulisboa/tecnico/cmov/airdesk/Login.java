package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class Login extends ActionBarActivity {

    public void loginToWorkspaceList(View view) {
        EditText ownerNickname = (EditText) findViewById(R.id.owner_nickname);
        String nick = ownerNickname.getText().toString().trim();
        if (nick.equals("")) {
            Util.toast_warning(getApplicationContext(), "Please enter a nickname.");
            return;
        }

        EditText ownerEmail = (EditText) findViewById(R.id.owner_email);
        String email = ownerEmail.getText().toString().trim();
        if (email.equals("")) {
            Util.toast_warning(getApplicationContext(), "Please enter an e-mail address.");
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        prefs.edit().putString("nick", nick).apply();
        prefs.edit().putString("email", email).apply();

        Intent intent = new Intent(this, WorkspaceList.class);
        //intent.putExtra("ownerNickname", ownerNickname.toString().trim());
        //intent.putExtra("ownerEmail", ownerEmail.toString().trim());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        ((EditText) findViewById(R.id.owner_nickname)).setText(prefs.getString("nick", ""));
        ((EditText) findViewById(R.id.owner_email)).setText(prefs.getString("email", ""));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
