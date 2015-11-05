package mm.mayorideas.gson;

import java.sql.Timestamp;

public class IdeaGETGson {

    private final int id;
    private final String title;
    private final int categoryID;
    private final String categoryName;
    private final String description;
    private final String location;
    private final int authorID;
    private final String authorName;
    private final Timestamp dateCreated;

    public IdeaGETGson(
            int id,
            String title,
            int categoryID,
            String categoryName,
            String description,
            String location,
            int authorID,
            String authorName,
            Timestamp dateCreated) {
        this.id = id;
        this.title = title;
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.description = description;
        this.location = location;
        this.authorID = authorID;
        this.authorName = authorName;
        this.dateCreated = dateCreated;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public int getAuthorID() {
        return authorID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }


}
