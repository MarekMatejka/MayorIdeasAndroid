package mm.mayorideas.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BlackholeHttpResponseHandler;

public class FollowAPI {

    private static String FOLLOW = "follows/";

    public static void followIdea(
            int userID,
            int ideaID) {
        String url = ServerAPIHelper.getServer()+ FOLLOW;

        url += "f?user_id="+userID+"&idea_id="+ideaID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.put(url, new BlackholeHttpResponseHandler());
    }

    public static void unfollowIdea(
            int userID,
            int ideaID) {
        String url = ServerAPIHelper.getServer()+ FOLLOW;

        url += "u?user_id="+userID+"&idea_id="+ideaID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.put(url, new BlackholeHttpResponseHandler());
    }
}
