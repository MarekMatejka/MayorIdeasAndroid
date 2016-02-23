package mm.mayorideas.api;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import mm.mayorideas.gson.LoginDetailsPOSTGson;
import mm.mayorideas.gson.LoginDetailsResponse;
import mm.mayorideas.gson.RegisterUserPOSTGson;

public class LoginAPI {

    private static String LOGIN = "login/";

    public static void login(String username, String password, final LoginListener listener) {
        String url = ServerAPIHelper.getServer() + LOGIN;

        LoginDetailsPOSTGson loginDetails = new LoginDetailsPOSTGson(username, password);
        StringEntity entity = null;
        try {
            Gson gson = new Gson();
            entity = new StringEntity(gson.toJson(loginDetails));
        }catch (UnsupportedEncodingException e) {e.printStackTrace();}

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(null, url, entity, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "" + responseString);
                if (listener != null) {
                    listener.onLoginFailure();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (listener != null) {
                    if (!responseString.equals("{}") && !responseString.isEmpty()) {
                        Gson gson = new Gson();
                        listener.onLoginSuccess(gson.fromJson(responseString, LoginDetailsResponse.class));
                    } else {
                        listener.onLoginFailure();
                    }
                }
            }
        });
    }

    public static void registerNewUser(
            String username,
            String password,
            String name,
            final LoginListener listener) {
        String url = ServerAPIHelper.getServer() + LOGIN + "register";

        RegisterUserPOSTGson registerUser = new RegisterUserPOSTGson(username, password, name);
        StringEntity entity = null;
        try {
            Gson gson = new Gson();
            entity = new StringEntity(gson.toJson(registerUser));
        }catch (UnsupportedEncodingException e) {e.printStackTrace();}

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(null, url, entity, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "" + responseString);
                if (listener != null) {
                    listener.onLoginFailure();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (listener != null) {
                    if (!responseString.equals("{}") && !responseString.isEmpty()) {
                        Gson gson = new Gson();
                        listener.onLoginSuccess(gson.fromJson(responseString, LoginDetailsResponse.class));
                    } else {
                        listener.onLoginFailure();
                    }
                }
            }
        });
    }

    public interface LoginListener {
        void onLoginSuccess(LoginDetailsResponse response);
        void onLoginFailure();
    }
}
