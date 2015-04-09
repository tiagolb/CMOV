package pt.ulisboa.tecnico.cmov.airdesk;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceCore;

public class Util {

    public static void toast_warning(Context context, String warning) {
        Toast.makeText(context, warning, Toast.LENGTH_SHORT).show();
    }

    public static void launchOwnedWorkspace(Context context, Class<?> cls, WorkspaceCore workspace) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("workspaceName", workspace.getName());
        context.startActivity(intent);
    }

    public static AlertDialog createEditTextDialog(Activity activity, String title, String message, String positiveButtonLabel, String negativeButtonLabel, EditText editText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setView(editText);

        builder.setPositiveButton(positiveButtonLabel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.setNegativeButton(negativeButtonLabel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        return builder.create();
    }

    public static void subscribe(final Activity activity, final Context context, View view) {
        final EditText editText = new EditText(activity);
        final AlertDialog dialog = Util.createEditTextDialog(activity,
                activity.getString(R.string.subscribe_to_workspaces),
                activity.getString(R.string.enter_tag) + ":", activity.getString(R.string.subscribe),
                activity.getString(R.string.cancel), editText);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = editText.getText().toString().trim();
                if (tag.equals("")) {
                    Util.toast_warning(context, activity.getString(R.string.must_enter_tag));
                } else {
                    dialog.dismiss();
                    AirDeskContext context = AirDeskContext.getContext();
                    context.addTagToSubscriptionTags(tag);
                    List<WorkspaceCore> workspacesWithTag = context.getWorkspacesWithTag(tag);
                    if (workspacesWithTag.isEmpty()) {
                        //Util.toast_warning(context, "No Workspace with such tag exists");
                    } else for (WorkspaceCore workspace : workspacesWithTag)
                        context.addMountedWorkspace(workspace);
                    Util.toast_warning(context, context.getString(R.string.tag_subscribed_success) + ": " + tag);
                }
            }
        });
    }


}
