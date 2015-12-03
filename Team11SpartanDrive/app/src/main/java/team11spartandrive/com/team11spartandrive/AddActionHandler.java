package team11spartandrive.com.team11spartandrive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.api.services.drive.model.File;

import team11spartadrive.com.helper.DriveFiles;

/**
 * Created by Ankit on 12/2/2015.
 */
public class AddActionHandler extends Dialog implements View.OnClickListener {
    final static String newFolder = "Folder";
    final static String newFile = "File";
    Context context;
    LinearLayout linearLayout;
    Button newFolderBtn;
    Button newFileBtn;
    private String newFolderName = "";

    public AddActionHandler(Context context) {
        super(context);
        this.setTitle("New");
        this.context = context;
        // Layouts
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        newFolderBtn = new Button(context);
        newFileBtn = new Button(context);
        // Assign Tags
        newFolderBtn.setText(newFolder);
        newFolderBtn.setTag(newFolder);
        newFileBtn.setText(newFile);
        newFileBtn.setTag(newFile);
        // Attach Listener
        newFolderBtn.setOnClickListener(this);
        newFileBtn.setOnClickListener(this);
        // Add Buttons to dialog
        linearLayout.addView(newFolderBtn);
        linearLayout.addView(newFileBtn);
        // Set View
        this.setContentView(linearLayout);
    }

    private void closeActionHandlerDialog() {
        this.dismiss();
    }

    private void showActionHandlerDialog() {
        this.show();
    }

    @Override
    public void onClick(View view) {
        String selectedAction = view.getTag().toString();
        switch (selectedAction) {
            case "Folder":
                closeActionHandlerDialog();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter folder name");
                // Set up the input
                final EditText input = new EditText(context);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newFolderName = input.getText().toString();
                        if (newFolderName.trim() != "") {
                            new Thread() {
                                public void run() {
                                    File body = new File();
                                    body.setTitle(newFolderName);
                                    body.setMimeType("application/vnd.google-apps.folder");
                                    try {
                                        File file = DriveFiles.getDriveFileInstance().getDrive_files().insert(body).execute();
                                    } catch (Exception e) {
                                        System.out.println("Error while creating new folder");
                                    }
                                }
                            }.start();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "folder name can not be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        newFolderName = "";
                        showActionHandlerDialog();
                    }
                });
                builder.show();
                break;
            case "File":
                this.dismiss();
                break;
        }
    }
}