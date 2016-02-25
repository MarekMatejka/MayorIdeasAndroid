package mm.mayorideas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import mm.mayorideas.adapters.IdeaListAdapter;
import mm.mayorideas.api.IdeaAPI;
import mm.mayorideas.objects.Idea;

public abstract class IdeaListFragment extends Fragment implements IdeaAPI.GetIdeasListener {

    private IdeaListAdapter mAdapter;
    protected TextView emptyListText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleArguments(getArguments());
        getIdeasToDisplay(this);
    }

    protected abstract void handleArguments(Bundle arguments);
    protected abstract void getIdeasToDisplay(IdeaAPI.GetIdeasListener ideasListener);
    protected abstract int getEmptyListText();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_idea, container, false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.idea_card_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new IdeaListAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        emptyListText = (TextView)view.findViewById(R.id.empty_recyclerview);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_idea_fab);
        fab.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onSuccess(List<Idea> ideas) {
        showEmptyListText(ideas.isEmpty());
        mAdapter.setData(ideas);
    }

    protected void showEmptyListText(boolean show) {
        if (show) {
            emptyListText.setText(getEmptyListText());
            emptyListText.setVisibility(View.VISIBLE);
        } else {
            emptyListText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure() {
    }
}
