package pt.ulisboa.tecnico.cmov.airdesk;


import android.content.Context;
import android.widget.Toast;

/**
 * Created by tiago on 26-03-2015.
 */
public class Util {

    public static void toast_warning(Context context, String warning) {
        Toast.makeText(context, warning, Toast.LENGTH_SHORT).show();
    }
}
