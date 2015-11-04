package mm.mayorideas.gson;

import java.sql.Timestamp;

public class NewIdeaPOSTGson {

    private final String title;
    private final String description;
    private final int categoryID;
    private final Timestamp dateCreated;
    private final String location;
    private final int authorID;

    public NewIdeaPOSTGson(String title, String description, int categoryID, Timestamp dateCreated, String location, int authorID) {
        this.title = title;
        this.description = description;
        this.categoryID = categoryID;
        this.dateCreated = dateCreated;
        this.location = location;
        this.authorID = authorID;
    }

    public NewIdeaPOSTGson(String title, String description, int categoryID) {
        this.title = title;
        this.description = description;
        this.categoryID = categoryID;
        this.dateCreated = new Timestamp(System.currentTimeMillis());
        this.location = "current location";
        this.authorID = 1;
    }
}
