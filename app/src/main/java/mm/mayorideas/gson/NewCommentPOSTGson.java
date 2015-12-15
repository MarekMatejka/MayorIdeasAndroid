package mm.mayorideas.gson;

public class NewCommentPOSTGson {

    private final  int userID;
    private final int ideaID;
    private final String commentText;

    public NewCommentPOSTGson(int userID, int ideaID, String commentText) {
        this.userID = userID;
        this.ideaID = ideaID;
        this.commentText = commentText;
    }

    public int getUserID() {
        return userID;
    }

    public int getIdeaID() {
        return ideaID;
    }

    public String getCommentText() {
        return commentText;
    }
}
