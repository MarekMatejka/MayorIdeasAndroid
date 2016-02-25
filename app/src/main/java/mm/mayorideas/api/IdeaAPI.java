package mm.mayorideas.api;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import mm.mayorideas.gson.NewIdeaPOSTGson;
import mm.mayorideas.objects.Idea;
import mm.mayorideas.objects.User;

public class IdeaAPI {

    private static String IDEA = "idea/";

    public interface AddNewIdeaListener {
        void onSuccess(int ideaID);
        void onError();
    }

    public static void addNewIdea(
            String title,
            String description,
            int categoryID,
            LatLng position,
            final AddNewIdeaListener listener) {

        String url = ServerAPIHelper.getServer()+IDEA+"add";

        NewIdeaPOSTGson postGson = new NewIdeaPOSTGson(title, description, categoryID, position);

        StringEntity entity = null;
        try {
            Gson gson = new Gson();
            entity = new StringEntity(gson.toJson(postGson));
        }catch (UnsupportedEncodingException e) {e.printStackTrace();}

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(null, url, entity, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "" + responseString);
                if (listener != null)
                    listener.onError();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (listener != null && !responseString.equals("-1"))
                    listener.onSuccess(Integer.parseInt(responseString));
            }
        });
    }

    public interface GetIdeasListener {
        void onSuccess(List<Idea> ideas);
        void onFailure();
    }

    public static void getTop10Ideas(final GetIdeasListener listener) {
        String url = ServerAPIHelper.getServer()+IDEA+"/top10";

        url += "?user_id="+User.getCurrentUser().getID();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                handleIdeaGETResponse(response, listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

    public static void get10ClosestIdeas(
            double latitude,
            double longitude,
            final GetIdeasListener listener) {
        String url = ServerAPIHelper.getServer()+IDEA+"/closest";

        url += "?user_id="+User.getCurrentUser().getID();
        url += "&latitude="+latitude;
        url += "&longitude="+longitude;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                handleIdeaGETResponse(response, listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

    public static void getMyIdeas(final GetIdeasListener listener) {
        String url = ServerAPIHelper.getServer()+IDEA+"/my";

        url += "?user_id="+User.getCurrentUser().getID();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                handleIdeaGETResponse(response, listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

    public static void getFollowingIdeas(final GetIdeasListener listener) {
        String url = ServerAPIHelper.getServer()+IDEA+"/following";

        url += "?user_id="+User.getCurrentUser().getID();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                handleIdeaGETResponse(response, listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

    public static void getTrendingIdeas(final GetIdeasListener listener) {
        String url = ServerAPIHelper.getServer()+IDEA+"/trending";

        url += "?user_id="+User.getCurrentUser().getID();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                handleIdeaGETResponse(response, listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

    public static void getIdeasByCategory(final int categoryID, final GetIdeasListener listener) {
        String url = ServerAPIHelper.getServer()+IDEA+"/category";

        url += "/"+categoryID;
        url += "?user_id="+User.getCurrentUser().getID();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                handleIdeaGETResponse(response, listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

    private static void handleIdeaGETResponse(String response, GetIdeasListener listener) {
        if (response != null && response.length() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Idea>>() {}.getType();
            List<Idea> ideas = gson.fromJson(response, type);

            if(listener != null) {
                listener.onSuccess(ideas);
            }
        } else {
            if (listener != null) {
                listener.onFailure();
            }
        }
    }
}
