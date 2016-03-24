package mm.mayorideas.objects;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Comment {

    private final int ID;
    private final int userID;
    private final int ideaID;
    private String userName;
    private final String text;
    private final Timestamp dateCreated;
    private final boolean isByCitizen;

    public Comment(int ID, int userID, int ideaID, String userName, String text, Timestamp dateCreated, boolean isByCitizen) {
        this.ID = ID;
        this.userID = userID;
        this.ideaID = ideaID;
        this.userName = userName;
        this.text = text;
        this.dateCreated = dateCreated;
        this.isByCitizen = isByCitizen;
    }

    public int getID() {
        return ID;
    }

    public int getUserID() {
        return userID;
    }

    public int getIdeaID() {
        return ideaID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public String getDateCreated() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm EEE, d MMM yyyy", Locale.getDefault());
        return formatter.format(dateCreated);
    }

    public boolean isByCitizen() {
        return isByCitizen;
    }
}
