package mm.mayorideas.adapters;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mm.mayorideas.R;
import mm.mayorideas.gson.IdeaGETGson;
import mm.mayorideas.objects.Idea;

public class IdeaListAdapter extends ArrayAdapter<IdeaGETGson> {

    private Context context;
    private List<IdeaGETGson> ideas;

    public IdeaListAdapter(Context context, List<IdeaGETGson> ideas) {
        super(context, R.layout.idea_list_adapter, ideas);
        this.context = context;
        this.ideas = ideas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.idea_list_adapter, parent, false);

            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(R.id.idea_name);
            holder.image = (ImageView) convertView.findViewById(R.id.idea_background_image);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder)convertView.getTag();
        }

        IdeaGETGson idea = ideas.get(position);
        String imageUrl = "http://themestudio.net/wp-content/uploads/2015/06/modern-psd-to-html-online-generator-idea.jpg";

        holder.name.setText(idea.getTitle());
        Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_drawer)
                .resize(getScreenWidth(), (int) context.getResources().getDimension(R.dimen.overview_idea_height))
                .centerCrop()
                .into(holder.image);
        return convertView;
    }

    private static class Holder {
        TextView name;
        ImageView image;
    }

    private int getScreenWidth(){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}
