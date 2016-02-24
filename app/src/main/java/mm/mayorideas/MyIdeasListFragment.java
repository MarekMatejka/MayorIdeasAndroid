package mm.mayorideas;

import android.os.Bundle;

import mm.mayorideas.api.IdeaAPI;
import mm.mayorideas.objects.User;
import mm.mayorideas.utils.LoginUtil;

public class MyIdeasListFragment extends IdeaListFragment {

    public static MyIdeasListFragment newInstance() {
        return new MyIdeasListFragment();
    }

    @Override
    protected void handleArguments(Bundle arguments) {}

    @Override
    protected void getIdeasToDisplay(IdeaAPI.GetIdeasListener ideasListener) {
        if (!User.isUserLoggedIn()) {
            LoginUtil.showLoginDialog(getActivity(), R.string.login_necessary_my_ideas, null);
        }
        IdeaAPI.getMyIdeas(ideasListener);
    }

    @Override
    protected int getEmptyListText() {
        return R.string.empty_list_my_ideas;
    }
}
