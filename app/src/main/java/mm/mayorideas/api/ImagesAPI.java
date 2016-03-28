package mm.mayorideas.api;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.FileEntity;
import mm.mayorideas.api.listeners.SimpleNumberListListener;
import mm.mayorideas.api.listeners.SimpleNumberValueListener;

public class ImagesAPI {

    private static final int LONG_TIMEOUT = 30000;
    private static final String IMAGE = "image/";

    public static void sendImage(
            int ideaID,
            Uri imageUri,
            Activity activity,
            final SimpleNumberValueListener listener) {
        String url = ServerAPIHelper.getServer()+IMAGE+ideaID;
        FileEntity file = new FileEntity(
                                new File(getPath(imageUri, activity)),
                                ContentType.APPLICATION_OCTET_STREAM);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(LONG_TIMEOUT);
        client.post(null, url, file, "application/octet-stream", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "" + responseString);
                if (listener != null) {
                    listener.onFailure();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (responseString != null && !responseString.equals("-1") && !responseString.isEmpty()) {
                    if (listener != null) {
                        listener.onSuccess(Integer.parseInt(responseString));
                    }
                } else {
                    if (listener != null) {
                        listener.onFailure();
                    }
                }
            }
        });
    }

    private static String getPath(Uri uri, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.managedQuery(uri, projection, null, null, null);
        context.startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String getImageUrl(int imageID) {
        return ServerAPIHelper.getServer()+IMAGE+"get/"+imageID;
    }

    public static void getImageIdsForIdea(int ideaID, final SimpleNumberListListener listener) {
        String url = ServerAPIHelper.getServer()+IMAGE+"/get/idea/"+ideaID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                if (response != null && response.length() > 0 && !response.equals("null")) {
                    if(listener != null) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Integer>>() {}.getType();
                        List<Integer> ids = gson.fromJson(response, type);
                        listener.onSuccess(ids);
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
