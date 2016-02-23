package mm.mayorideas;

import android.os.Bundle;

import mm.mayorideas.api.IdeaAPI;

public class FollowingIdeasListFragment extends IdeaListFragment {

    public static FollowingIdeasListFragment newInstance() {
        return new FollowingIdeasListFragment();
    }

    @Override
    protected void handleArguments(Bundle arguments) {}

    @Override
    protected void getIdeasToDisplay(IdeaAPI.GetIdeasListener ideasListener) {
        IdeaAPI.getFollowingIdeas(ideasListener);
    }

    @Override
    protected int getEmptyListText() {
        return R.string.empty_list_following;
    }
}
