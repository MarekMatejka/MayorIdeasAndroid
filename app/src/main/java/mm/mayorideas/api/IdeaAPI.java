package mm.mayorideas.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import mm.mayorideas.gson.IdeaGETGson;
import mm.mayorideas.gson.NewIdeaPOSTGson;

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
            final AddNewIdeaListener listener) {
        String url = ServerAPIHelper.getServer()+IDEA+"add";

        NewIdeaPOSTGson postGson = new NewIdeaPOSTGson(title, description, categoryID);

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

    public interface Get10IdeasListener {
        void onSuccess(List<IdeaGETGson> ideas);
        void onFailure();
    }

    public static void get10Ideas(final Get10IdeasListener listener) {
        String url = ServerAPIHelper.getServer()+IDEA+"/top10";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Log.e("response", response);
                if (response != null && response.length() > 0) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<IdeaGETGson>>() {}.getType();
                    List<IdeaGETGson> ideas = gson.fromJson(response, type);

                    if(listener != null) {
                        listener.onSuccess(ideas);
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
}
