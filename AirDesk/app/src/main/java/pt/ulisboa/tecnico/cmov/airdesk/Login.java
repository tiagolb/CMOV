package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class Login extends ActionBarActivity {

    public void loginToWorkspaceList(View view) {
        EditText ownerNickname = (EditText) findViewById(R.id.owner_nickname);
        String nick = ownerNickname.getText().toString().trim();
        if (nick.equals("")) {
            Util.toast(getApplicationContext(), "Please enter a nickname.");
            return;
        }

        EditText ownerEmail = (EditText) findViewById(R.id.owner_email);
        String email = ownerEmail.getText().toString().trim().toLowerCase();
        if (email.equals("")) {
            Util.toast(getApplicationContext(), "Please enter an e-mail address.");
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        prefs.edit().putString("nick", nick).apply();
        prefs.edit().putString("email", email).apply();

        Intent intent = new Intent(this, WorkspaceList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("newLogin", true);
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
    public void onBackPressed() {
    }
}
