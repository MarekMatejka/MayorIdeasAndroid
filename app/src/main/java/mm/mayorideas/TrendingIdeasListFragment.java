package mm.mayorideas;

import mm.mayorideas.api.IdeaAPI;

public class TrendingIdeasListFragment extends IdeaListFragment {

    public static TrendingIdeasListFragment newInstance() {
        return new TrendingIdeasListFragment();
    }

    @Override
    protected void getIdeasToDisplay(IdeaAPI.GetIdeasListener ideasListener) {
        IdeaAPI.getTrendingIdeas(ideasListener);
    }
}
