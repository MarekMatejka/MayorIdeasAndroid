package mm.mayorideas;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;

import java.lang.reflect.Type;
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
            //TODO: DELETE ONCE SERVER IS AVAILABLE REMOTELY
            Log.e("test", "test");
            Gson gson = new Gson();
            String text = "[{\"id\":1,\"title\":\"Test title\",\"categoryID\":1,\"categoryName\":\"Environment\",\"description\":\"test description\",\"location\":\"test location\",\"authorID\":1,\"authorName\":\"Marek\",\"dateCreated\":\"Nov 3, 2015 11:18:58 PM\"},{\"id\":2,\"title\":\"dogdo\",\"categoryID\":1,\"categoryName\":\"Environment\",\"description\":\"oydkpg\",\"location\":\"current location\",\"authorID\":1,\"authorName\":\"Marek\",\"dateCreated\":\"Nov 4, 2015 2:56:49 PM\"},{\"id\":3,\"title\":\"Ahoj \",\"categoryID\":2,\"categoryName\":\"Culture\",\"description\":\"Marek\",\"location\":\"current location\",\"authorID\":1,\"authorName\":\"Marek\",\"dateCreated\":\"Nov 4, 2015 4:21:57 PM\"},{\"id\":5,\"title\":\"Hello \",\"categoryID\":2,\"categoryName\":\"Culture\",\"description\":\"hi\",\"location\":\"current location\",\"authorID\":1,\"authorName\":\"Marek\",\"dateCreated\":\"Nov 4, 2015 5:32:16 PM\"},{\"id\":4,\"title\":\"hello sam\",\"categoryID\":4,\"categoryName\":\"Infrastructure\",\"description\":\"Ho \",\"location\":\"current location\",\"authorID\":1,\"authorName\":\"Marek\",\"dateCreated\":\"Nov 4, 2015 4:28:00 PM\"}]";
            Type type = new TypeToken<List<IdeaGETGson>>() {}.getType();
            List<IdeaGETGson> ideas = gson.fromJson(text, type);
            mAdapter.setData(ideas);
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
