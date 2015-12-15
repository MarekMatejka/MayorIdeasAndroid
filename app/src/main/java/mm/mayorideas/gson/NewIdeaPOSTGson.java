package mm.mayorideas.gson;

public class NewIdeaPOSTGson {

    private final String title;
    private final String description;
    private final int categoryID;
    private final String location;
    private final int authorID;

    public NewIdeaPOSTGson(String title, String description, int categoryID, String location, int authorID) {
        this.title = title;
        this.description = description;
        this.categoryID = categoryID;
        this.location = location;
        this.authorID = authorID;
    }

    public NewIdeaPOSTGson(String title, String description, int categoryID) {
        this.title = title;
        this.description = description;
        this.categoryID = categoryID;
        this.location = "current location";
        this.authorID = 1;
    }
}
