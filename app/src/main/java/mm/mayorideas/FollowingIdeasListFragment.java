package mm.mayorideas;

import mm.mayorideas.api.IdeaAPI;

public class FollowingIdeasListFragment extends IdeaListFragment {

    public static FollowingIdeasListFragment newInstance() {
        return new FollowingIdeasListFragment();
    }

    @Override
    protected void getIdeasToDisplay(IdeaAPI.GetIdeasListener ideasListener) {
        IdeaAPI.getFollowingIdeas(ideasListener);
    }
}
