package mm.mayorideas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import mm.mayorideas.api.LoginAPI;
import mm.mayorideas.gson.LoginDetailsResponse;
import mm.mayorideas.objects.User;
import mm.mayorideas.utils.LoginUtil;

public class NewUserActivity extends AppCompatActivity implements LoginAPI.LoginListener {

    private EditText username;
    private EditText name;
    private EditText password;
    private EditText passwordCheck;

    private String usernameText;
    private String nameText;
    private String passwordText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        username = (EditText)findViewById(R.id.new_user_username);
        name = (EditText)findViewById(R.id.new_user_name);
        password = (EditText)findViewById(R.id.new_user_password);
        passwordCheck = (EditText)findViewById(R.id.new_user_password_check);
    }

    @Override
    public void onResume() {
        super.onResume();
        LoginUtil.readAndSetCurrentUser(this);
    }

    private boolean validateFields() {
        boolean success = true;

        usernameText = username.getText().toString().trim();
        if (usernameText.isEmpty()) {
            username.setError(getString(R.string.username_empty_error));
            success = false;
        }

        nameText = name.getText().toString().trim();
        if (nameText.isEmpty()) {
            name.setError(getString(R.string.name_empty_error));
            success = false;
        }

        passwordText = password.getText().toString().trim();
        if (passwordText.isEmpty()) {
            password.setError(getString(R.string.password_empty_error));
            success = false;
        }

        String passwordTextCheck = passwordCheck.getText().toString().trim();
        if (passwordTextCheck.isEmpty()) {
            passwordCheck.setError(getString(R.string.password_empty_error));
            success = false;
        }

        if (passwordText.length() < 6) {
            Toast.makeText(this, R.string.password_too_short, Toast.LENGTH_LONG).show();
            success = false;
        }

        if (!passwordText.equals(passwordTextCheck)) {
            Toast.makeText(this, R.string.passwords_not_matching, Toast.LENGTH_LONG).show();
            success = false;
        }

        return success;
    }

    public void registerNewUser(View view) {
        if (validateFields()) {
            LoginAPI.registerNewUser(usernameText, passwordText, nameText, this);
        }
    }

    @Override
    public void onLoginSuccess(LoginDetailsResponse response) {
        User newCurrentUser = response.convertToUser();
        User.setCurrentUser(newCurrentUser);
        LoginUtil.saveCurrentUser(this, newCurrentUser);
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailure() {
        Log.e("error", "error registering a user");
    }
}
