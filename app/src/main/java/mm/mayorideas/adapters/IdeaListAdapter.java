package mm.mayorideas.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mm.mayorideas.IdeaDetailActivity;
import mm.mayorideas.R;
import mm.mayorideas.gson.IdeaGETGson;
import mm.mayorideas.ui.IdeaActionBarHandler;
import mm.mayorideas.ui.IdeaStatusBarHandler;

public class IdeaListAdapter extends AbstractListAdapter<IdeaGETGson, IdeaListAdapter.ViewHolder> {

    private Activity mContext;

    public IdeaListAdapter(Activity context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(mContext)
                        .inflate(R.layout.adapter_item_idea_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.bind(mContext, mData.get(position));
        viewHolder.ideaCardBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, IdeaDetailActivity.class);
                i.putExtra(IdeaDetailActivity.IDEA_ID_TAG, mData.get(position));
                mContext.startActivity(i);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        private final View view;
        private TextView name;
        private ImageView image;
        private TextView description;
        View ideaCardBody;

        public ViewHolder(View v) {
            super(v);
            view = v;

            name = (TextView) v.findViewById(R.id.idea_card_title);
            image = (ImageView) v.findViewById(R.id.idea_card_image);
            description = (TextView) v.findViewById(R.id.idea_card_description);

            ideaCardBody = v.findViewById(R.id.idea_card_body);
        }

        public void bind(Activity context, IdeaGETGson idea) {
            name.setText(idea.getTitle());
            description.setText(idea.getDescription());
            Picasso.with(context)
                    .load(idea.getCoverImageUrl())
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_drawer)
                    .fit()
                    .centerCrop()
                    .into(image);

            IdeaStatusBarHandler statusBarHandler = new IdeaStatusBarHandler(context, view, idea);
            IdeaActionBarHandler actionBarHandler = new IdeaActionBarHandler(context, idea, view, statusBarHandler);
        }
    }
}
