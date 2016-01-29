package mm.mayorideas;

import android.os.Bundle;

import mm.mayorideas.api.IdeaAPI;

public class TrendingIdeasListFragment extends IdeaListFragment {

    public static TrendingIdeasListFragment newInstance() {
        return new TrendingIdeasListFragment();
    }

    @Override
    protected void handleArguments(Bundle arguments) {}

    @Override
    protected void getIdeasToDisplay(IdeaAPI.GetIdeasListener ideasListener) {
        IdeaAPI.getTrendingIdeas(ideasListener);
    }
}
