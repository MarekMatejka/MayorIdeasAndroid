package mm.mayorideas.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BlackholeHttpResponseHandler;

import mm.mayorideas.objects.Vote;

public class VoteAPI {

    private static String VOTE = "vote/";

    public static void castVote(
            int userID,
            int ideaID,
            Vote vote) {
        String url = ServerAPIHelper.getServer()+VOTE;

        url += "cast?user_id="+userID+"&idea_id="+ideaID+"&vote="+vote.getVote();

        AsyncHttpClient client = new AsyncHttpClient();
        client.put(url, new BlackholeHttpResponseHandler());
    }

    public static void deleteVote(
            int userID,
            int ideaID) {
        String url = ServerAPIHelper.getServer()+VOTE;

        url += "delete?user_id="+userID+"&idea_id="+ideaID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.put(url, new BlackholeHttpResponseHandler());
    }
}
