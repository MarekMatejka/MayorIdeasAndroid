package mm.mayorideas.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ViewHolder;

import mm.mayorideas.NewUserActivity;
import mm.mayorideas.R;
import mm.mayorideas.api.LoginAPI;
import mm.mayorideas.gson.LoginDetailsResponse;
import mm.mayorideas.objects.User;

public class LoginUtil {

    private enum TextType {INFO, WARNING}

    public static void showLoginDialog(Activity context, int text) {
        Holder holder = new ViewHolder(R.layout.dialog_login);
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setCancelable(true)
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
                Log.e("login","login");
                LoginAPI.login(
                        username.getText().toString(),
                        password.getText().toString(),
                        new LoginAPI.LoginListener() {
                            @Override
                            public void onLoginSuccess(LoginDetailsResponse response) {
                                Log.e("login", response.toString());
                                User.setCurrentUser(response.convertToUser());
                                dialog.dismiss();
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
}
