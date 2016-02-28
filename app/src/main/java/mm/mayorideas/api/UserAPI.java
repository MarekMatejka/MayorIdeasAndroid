package mm.mayorideas.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;
import mm.mayorideas.gson.UserStats;

public class UserAPI {

    private static final String USER = "user/";

    public static void getStatsForUser(
            int userID,
            final OnUserStatsReceivedListener listener) {
        String url = ServerAPIHelper.getServer()+USER+"/stats/"+userID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                if (response != null && response.length() > 0 && !response.equals("null")) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<UserStats>() {}.getType();
                    UserStats stats = gson.fromJson(response, type);

                    if(listener != null) {
                        listener.onUserStatsReceivedSuccessfully(stats);
                    }
                } else {
                    if (listener != null) {
                        listener.onUserStatsReceivedUnsuccessfully();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onUserStatsReceivedUnsuccessfully();
                }
            }
        });
    }

    public interface OnUserStatsReceivedListener {
        void onUserStatsReceivedSuccessfully(UserStats stats);
        void onUserStatsReceivedUnsuccessfully();
    }
}
