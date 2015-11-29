package mm.mayorideas;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import mm.mayorideas.adapters.IdeaListAdapter;
import mm.mayorideas.api.IdeaAPI;
import mm.mayorideas.gson.IdeaGETGson;

public class IdeaListFragment extends Fragment {

    private IdeaListAdapter mAdapter;

    public static IdeaListFragment newInstance() {
        return new IdeaListFragment();
    }

    public IdeaListFragment() {}

    private final IdeaAPI.Get10IdeasListener get10IdeasListener = new IdeaAPI.Get10IdeasListener() {
        @Override
        public void onSuccess(List<IdeaGETGson> ideas) {
            mAdapter.setData(ideas);
        }

        @Override
        public void onFailure() {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IdeaAPI.get10Ideas(get10IdeasListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Top 10 ideas");

        View view = inflater.inflate(R.layout.fragment_idea, container, false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.idea_card_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new IdeaListAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_idea_fab);
        fab.attachToRecyclerView(recyclerView);

        return view;
    }
}
