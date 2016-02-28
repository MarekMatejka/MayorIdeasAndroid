package mm.mayorideas.gson;

public class UserStats {

    private final int userID;
    private final int ideas;
    private final int votes;
    private final int comments;
    private final int follows;

    public UserStats(int userID, int ideas, int votes, int comments, int follows) {
        this.userID = userID;
        this.ideas = ideas;
        this.votes = votes;
        this.comments = comments;
        this.follows = follows;
    }

    public int getUserID() {
        return userID;
    }

    public int getIdeas() {
        return ideas;
    }

    public int getVotes() {
        return votes;
    }

    public int getComments() {
        return comments;
    }

    public int getFollows() {
        return follows;
    }
}
