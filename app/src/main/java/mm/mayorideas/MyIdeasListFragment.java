package mm.mayorideas;

import mm.mayorideas.api.IdeaAPI;

public class MyIdeasListFragment extends IdeaListFragment {

    public static MyIdeasListFragment newInstance() {
        return new MyIdeasListFragment();
    }

    @Override
    protected void getIdeasToDisplay(IdeaAPI.GetIdeasListener ideasListener) {
        IdeaAPI.getMyIdeas(ideasListener);
    }
}
