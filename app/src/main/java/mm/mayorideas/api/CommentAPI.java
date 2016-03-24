package mm.mayorideas.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import mm.mayorideas.api.listeners.SimpleNumberValueListener;
import mm.mayorideas.gson.NewCommentPOSTGson;
import mm.mayorideas.objects.Comment;
import mm.mayorideas.security.AESEncryptor;

public class CommentAPI {

    private static String COMMENT = "comment/";

    public static void addComment(
            int userID,
            int ideaID,
            String commentText) {
        String url = ServerAPIHelper.getServer() + COMMENT + "add";

        NewCommentPOSTGson comment = new NewCommentPOSTGson(userID, ideaID, commentText);
        StringEntity entity = null;
        try {
            Gson gson = new Gson();
            entity = new StringEntity(gson.toJson(comment));
        }catch (UnsupportedEncodingException e) {e.printStackTrace();}

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(null, url, entity, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "" + responseString);
//                if (listener != null)
//                    listener.onGetCommentsFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                if (listener != null && !responseString.equals("-1"))
//                    listener.onGetCommentsSuccess(Integer.parseInt(responseString));
            }
        });
    }

    public interface GetCommentsForIdeaListener {
        void onGetCommentsSuccess(List<Comment> comments);
        void onGetCommentsFailure();
    }

    public static void getAllCommentsForIdea(
            int ideaID,
            final GetCommentsForIdeaListener listener) {
        String url = ServerAPIHelper.getServer()+COMMENT+"/idea/"+ideaID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Log.e("all comments", response);
                handleResponse(response, listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onGetCommentsFailure();
                }
            }
        });
    }

    public static void getLast2CommentsForIdea(
            int ideaID,
            final GetCommentsForIdeaListener listener) {
        String url = ServerAPIHelper.getServer()+COMMENT+"/idea/last/"+ideaID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Log.e("last 2 comments", response);
                handleResponse(response, listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onGetCommentsFailure();
                }
            }
        });
    }

    private static void handleResponse(String response, GetCommentsForIdeaListener listener) {
        if (response != null && response.length() > 0 && !response.equals("null")) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Comment>>() {}.getType();
            List<Comment> comments = gson.fromJson(response, type);
            comments = decryptNames(comments);

            if(listener != null) {
                listener.onGetCommentsSuccess(comments);
            }
        } else {
            if (listener != null) {
                listener.onGetCommentsFailure();
            }
        }
    }

    public static void getNumberOfCommentsForIdea(
            int ideaID,
            final SimpleNumberValueListener listener) {
        String url = ServerAPIHelper.getServer()+COMMENT+"/idea/count/"+ideaID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                if (response != null && response.length() > 0 && !response.equals("null")) {
                    if(listener != null) {
                        listener.onSuccess(Integer.parseInt(response));
                    }
                } else {
                    if (listener != null) {
                        listener.onFailure();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

    private static List<Comment> decryptNames(List<Comment> comments) {
        AESEncryptor aes = new AESEncryptor();
        for (Comment comment : comments) {
            comment.setUserName(aes.decrypt(comment.getUserName()));
        }
        return comments;
    }
}
