package mm.mayorideas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsImageView;

import mm.mayorideas.R;
import mm.mayorideas.objects.IdeaCategory;

public class CategoryAdapter extends ArrayAdapter<IdeaCategory> {

    private final Context mContext;
    private final static IdeaCategory[] categories = IdeaCategory.values();

    public CategoryAdapter(Context context) {
        super(context, R.layout.adapter_item_category, categories);
        this.mContext = context;
    }

    public CategoryAdapter(Context context, int layout) {
        super(context, layout, categories);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_item_category, parent, false);

            holder = new Holder();
            holder.categoryIcon = (IconicsImageView) convertView.findViewById(R.id.category_icon);
            holder.categoryName = (TextView) convertView.findViewById(R.id.category_name);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder)convertView.getTag();
        }

        IdeaCategory category = categories[position];
        holder.categoryIcon.setIcon(category.getIcon());
        holder.categoryIcon.setColorRes(category.getIconColorRes());
        holder.categoryName.setText(mContext.getString(category.getNameRes()));

        return convertView;
    }

    private static class Holder {
        IconicsImageView categoryIcon;
        TextView categoryName;
    }
}
