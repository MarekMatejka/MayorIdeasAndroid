package mm.mayorideas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.LinkedList;
import java.util.List;

import mm.mayorideas.adapters.IdeaListAdapter;
import mm.mayorideas.api.IdeaAPI;
import mm.mayorideas.dummy.DummyContent;
import mm.mayorideas.gson.IdeaGETGson;
import mm.mayorideas.objects.Idea;

public class IdeaListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private IdeaListInteractionListener mListener;

    private AbsListView mListView;
    private ListAdapter mAdapter;

    public static IdeaListFragment newInstance() {
        return new IdeaListFragment();
    }

    public IdeaListFragment() {}

    private final IdeaAPI.Get10IdeasListener get10IdeasListener = new IdeaAPI.Get10IdeasListener() {
        @Override
        public void onSuccess(List<IdeaGETGson> ideas) {
            mAdapter = new IdeaListAdapter(getActivity(), ideas);
            ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
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
        View view = inflater.inflate(R.layout.fragment_idea, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);

        mListView.setOnItemClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_idea_fab);
        fab.attachToListView(mListView);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IdeaListInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public interface IdeaListInteractionListener {
        void onFragmentInteraction(String id);
    }
}
