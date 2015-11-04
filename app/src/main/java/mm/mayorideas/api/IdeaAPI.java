package mm.mayorideas.api;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

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

}
