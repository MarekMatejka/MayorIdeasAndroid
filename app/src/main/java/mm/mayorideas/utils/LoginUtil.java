package mm.mayorideas.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mm.mayorideas.NewUserActivity;
import mm.mayorideas.R;
import mm.mayorideas.api.LoginAPI;
import mm.mayorideas.gson.LoginDetailsResponse;
import mm.mayorideas.objects.User;

public class LoginUtil {

    private enum TextType {INFO, WARNING}

    public static void showLoginDialog(Activity context, int text, @Nullable OnDismissListener listener) {
        Holder holder = new ViewHolder(R.layout.dialog_login);
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setCancelable(true)
                .setOnDismissListener(listener)
                .create();

        View view = holder.getInflatedView();
        setupButtons(context, view, dialog);
        setText(context, view, TextType.INFO, text);

        dialog.show();
    }

    private static void setText(Activity context, View view, TextType textType, int textResource) {
        TextView text = (TextView)view.findViewById(R.id.login_dialog_text);
        text.setText(textResource);
        if (textType == TextType.INFO) {
            text.setTextColor(context.getResources().getColor(R.color.mayorideas_blue));
        } else {
            text.setTextColor(context.getResources().getColor(R.color.holo_orange_light));
        }
    }

    private static void setupButtons(
            final Activity context,
            final View view,
            final DialogPlus dialog) {
        Button login = (Button)view.findViewById(R.id.login_dialog_login);
        Button signUp = (Button)view.findViewById(R.id.login_dialog_signup);
        final EditText username = (EditText)view.findViewById(R.id.login_dialog_username);
        final EditText password = (EditText)view.findViewById(R.id.login_dialog_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAPI.login(
                        username.getText().toString(),
                        password.getText().toString(),
                        new LoginAPI.LoginListener() {
                            @Override
                            public void onLoginSuccess(LoginDetailsResponse response) {
                                User newCurrentUser = response.convertToUser();
                                User.setCurrentUser(newCurrentUser);
                                saveCurrentUser(context, newCurrentUser);
                                dialog.dismiss();
                                Toast.makeText(
                                        context,
                                        context.getString(
                                                R.string.login_welcome)+
                                                newCurrentUser.getName()+
                                                "!",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }

                            @Override
                            public void onLoginFailure() {
                                setText(context, view, TextType.WARNING, R.string.incorrect_login);
                            }
                        });
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewUserActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        });
    }

    private static final String FILENAME = "user.mm";

    public static void readAndSetCurrentUser(Context context) {
        String userText = readFile(context);
        if (userText != null) {
            User user = User.parse(userText);
            User.setCurrentUser(user);
        } else {
            User.setCurrentUser(null);
        }
    }

    public static void saveCurrentUser(Context context, User currentUser) {
        if (currentUser != null) {
            writeFile(context, User.toRecord(currentUser));
        }
    }

    public static void deleteCurrentUser(Context context) {
        context.deleteFile(FILENAME);
    }

    private static @Nullable String readFile(Context context) {
        if (!fileExists(context)) {
            return null;
        }

        try {
            DataInputStream in = new DataInputStream(context.openFileInput(FILENAME));
            String result = in.readUTF();
            in.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean fileExists(Context context) {
        return context.getFileStreamPath(FILENAME).exists();
    }

    private static boolean writeFile(Context context, String content) {
        try {
            context.deleteFile(FILENAME);
            DataOutputStream out = new DataOutputStream(
                    context.openFileOutput(
                            FILENAME,
                            Context.MODE_PRIVATE));
            out.writeUTF(content);
            out.close();
            return true;
        } catch (IOException e) {
            createEmptyFile(context, FILENAME);
            writeFile(context, content);
            e.printStackTrace();
        }
        return false;
    }

    private static void createEmptyFile(Context context, String filename) {
        try {
            DataOutputStream out = new DataOutputStream(
                    context.openFileOutput(
                            filename,
                            Context.MODE_PRIVATE));
            out.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
