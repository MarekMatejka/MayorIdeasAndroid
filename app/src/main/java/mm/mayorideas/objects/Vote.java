package mm.mayorideas.objects;

public enum Vote {
    LIKE(1), DISLIKE(-1);

    private int vote;
    Vote(int i) {
        vote = i;
    }

    public int getVote() {
        return vote;
    }
}
