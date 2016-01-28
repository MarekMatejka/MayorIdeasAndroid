package mm.mayorideas;

import mm.mayorideas.api.IdeaAPI;

public class Top10IdeasListFragment extends IdeaListFragment {

    public static Top10IdeasListFragment newInstance() {
        return new Top10IdeasListFragment();
    }

    @Override
    protected void getIdeasToDisplay(IdeaAPI.GetIdeasListener ideasListener) {
        IdeaAPI.getTop10Ideas(ideasListener);
    }
}
