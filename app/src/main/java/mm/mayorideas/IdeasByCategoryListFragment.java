package mm.mayorideas;

import android.os.Bundle;

import mm.mayorideas.api.IdeaAPI;

public class IdeasByCategoryListFragment extends IdeaListFragment {

    private static final String CATEGORY_ID = "categoryID";

    private int categoryID;

    public static IdeasByCategoryListFragment newInstance(int categoryID) {
        Bundle args = new Bundle();
        IdeasByCategoryListFragment fragment = new IdeasByCategoryListFragment();
        args.putInt(CATEGORY_ID, categoryID);
        fragment.setArguments(args);
        return fragment;
    }

    public IdeasByCategoryListFragment() {}

    @Override
    protected void handleArguments(Bundle arguments) {
        if (arguments != null) {
            this.categoryID = arguments.getInt(CATEGORY_ID);
        }
    }

    @Override
    protected void getIdeasToDisplay(IdeaAPI.GetIdeasListener ideasListener) {
        IdeaAPI.getIdeasByCategory(categoryID, ideasListener);
    }
}
