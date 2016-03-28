package mm.mayorideas.gson;

import com.google.android.gms.maps.model.LatLng;

import mm.mayorideas.objects.User;

public class NewIdeaPOSTGson {

    private final String title;
    private final String description;
    private final int categoryID;
    private final double latitude;
    private final double longitude;
    private final int authorID;

    public NewIdeaPOSTGson(String title, String description, int categoryID, LatLng position, int authorID) {
        this.title = title;
        this.description = description;
        this.categoryID = categoryID;
        this.latitude = position.latitude;
        this.longitude = position.longitude;
        this.authorID = authorID;
    }

    public NewIdeaPOSTGson(String title, String description, int categoryID, LatLng position) {
        this.title = title;
        this.description = description;
        this.categoryID = categoryID;
        this.latitude = position.latitude;
        this.longitude = position.longitude;
        this.authorID = User.getCurrentUser().getID();
    }
}
