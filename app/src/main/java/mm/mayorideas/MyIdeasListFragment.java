package mm.mayorideas;

import android.os.Bundle;

import mm.mayorideas.api.IdeaAPI;

public class MyIdeasListFragment extends IdeaListFragment {

    public static MyIdeasListFragment newInstance() {
        return new MyIdeasListFragment();
    }

    @Override
    protected void handleArguments(Bundle arguments) {}

    @Override
    protected void getIdeasToDisplay(IdeaAPI.GetIdeasListener ideasListener) {
        IdeaAPI.getMyIdeas(ideasListener);
    }
}
