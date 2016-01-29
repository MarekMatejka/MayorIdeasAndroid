package mm.mayorideas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import mm.mayorideas.adapters.CategoryAdapter;
import mm.mayorideas.objects.IdeaCategory;

public class CategoriesListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private CategoryAdapter mAdapter;
    private CategoryClickListener mListener;

    public static CategoriesListFragment newInstance() {
        return new CategoriesListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories_list, container, false);

        ListView listView = (ListView)view.findViewById(R.id.categories_list);
        mAdapter = new CategoryAdapter(getActivity(), R.layout.adapter_item_category);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        return view;
    }

    public void setCategoriesListener(CategoryClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null) {
            IdeaCategory category = mAdapter.getItem(position);
            mListener.onCategoryClicked(category.getID(), category.getNameRes());
        }
    }

    public interface CategoryClickListener {
        void onCategoryClicked(int categoryID, int categoryName);
    }
}
