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
import pt.ulisboa.tecnico.cmov.airdesk.core.WorkspaceFileCore;

public class Util {

    public static void toast_warning(Context context, String warning) {
        Toast.makeText(context, warning, Toast.LENGTH_SHORT).show();
    }

    public static void toast(String text) {
        Toast.makeText(AirDeskContext.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void launchOwnedWorkspace(Context context, Class<?> cls, WorkspaceCore workspace) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("workspaceName", workspace.getName());
        context.startActivity(intent);
    }

    public static void launchEditFileOwned(Context context, WorkspaceCore workspace,
                                           WorkspaceFileCore file) {
        Intent intent = new Intent(context, EditFileOwned.class);
        intent.putExtra("file", file.getName());
        intent.putExtra("workspace", workspace.getName());
        context.startActivity(intent);
    }

    public static void launchEditFileForeign(Context context, WorkspaceCore workspace,
                                             WorkspaceFileCore file) {
        Intent intent = new Intent(context, EditFileForeign.class);
        intent.putExtra("owner", workspace.getOwner());
        intent.putExtra("file", file.getName());
        intent.putExtra("workspace", workspace.getName());
        context.startActivity(intent);
    }

    public static AlertDialog createDialog(Activity activity, String title, String message,
                                           String positiveButtonLabel,
                                           String negativeButtonLabel,
                                           EditText editText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title);
        builder.setMessage(message);
        if (editText != null) builder.setView(editText);

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
        AlertDialog dialog = builder.create();
        if (editText != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        dialog.show();
        return dialog;
    }

    public static void subscribe(final Activity activity, final Context context) {
        final EditText editText = new EditText(activity);
        final AlertDialog dialog = Util.createDialog(activity,
                activity.getString(R.string.subscribe_to_workspaces),
                activity.getString(R.string.enter_tag) + ":", activity.getString(R.string.subscribe),
                activity.getString(R.string.cancel), editText);

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AirDeskContext airDesk = AirDeskContext.getContext();
                String tag = editText.getText().toString().trim();
                if (tag.equals("")) {
                    Util.toast_warning(context, activity.getString(R.string.must_enter_tag));
                } else if (airDesk.getSubscribedTags().contains(tag)) {
                    Util.toast_warning(context, activity.getString(R.string.tag_already_subscribed));
                } else {
                    dialog.dismiss();
                    airDesk.addTagToSubscriptionTags(tag);
                    List<WorkspaceCore> workspacesWithTag = airDesk.getWorkspacesWithTag(tag);
                    for (WorkspaceCore workspace : workspacesWithTag)
                        airDesk.addMountedWorkspace(workspace);
                    Util.toast_warning(context, context.getString(R.string.tag_subscribed_success) +
                            ": " + tag);
                }
            }
        });
    }

    public static void inviteClient(final Activity activity, final Context context,
                                    final WorkspaceCore workspace) {
        final EditText editText = new EditText(activity);
        final AlertDialog dialog = Util.createDialog(activity,
                activity.getString(R.string.invite_client),
                activity.getString(R.string.enter_client_email) + ":",
                activity.getString(R.string.invite),
                activity.getString(R.string.cancel), editText);

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editText.getText().toString().trim();

                if (value.equals("")) {
                    Util.toast_warning(context,
                            activity.getString(R.string.must_enter_email));
                } else if (workspace.hasClient(value)) {
                    Util.toast_warning(context,
                            activity.getString(R.string.client_already_in_workspace));
                } else {
                    dialog.dismiss();
                    workspace.addClient(value);
                    Util.toast_warning(context,
                            activity.getString(R.string.client_added) + ": " + value);
                }
            }
        });
    }

    public static void removeFile(Context context, WorkspaceCore workspace, WorkspaceFileCore file) {
        workspace.removeFile(file.getName());
        file.removeFile(context);
        Util.toast_warning(context, context.getString(R.string.file_removed));
    }


}
