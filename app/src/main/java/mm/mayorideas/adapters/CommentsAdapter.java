package mm.mayorideas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mm.mayorideas.R;
import mm.mayorideas.objects.Comment;

public class CommentsAdapter extends ArrayAdapter<Comment> {

    private final List<Comment> comments;
    private final Context context;

    public CommentsAdapter(Context context, List<Comment> comments) {
        super(context, R.layout.adapter_item_comment, comments);
        this.comments = comments;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_item_comment, parent, false);

            holder = new Holder();
            holder.personName = (TextView) convertView.findViewById(R.id.person_name);
            holder.commentText = (TextView) convertView.findViewById(R.id.comment_text);
            holder.commentAdded = (TextView) convertView.findViewById(R.id.comment_added);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder)convertView.getTag();
        }

        Comment comment = comments.get(position);
        holder.personName.setText(comment.getAuthorName());
        holder.commentText.setText(comment.getText());
        holder.commentAdded.setText(comment.getDate());

        return convertView;
    }

    private static class Holder {
        TextView personName;
        TextView commentText;
        TextView commentAdded;
    }
}
