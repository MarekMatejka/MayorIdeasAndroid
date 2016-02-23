package mm.mayorideas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import mm.mayorideas.adapters.CommentsAdapter;
import mm.mayorideas.api.CommentAPI;
import mm.mayorideas.gson.IdeaGETGson;
import mm.mayorideas.objects.Comment;
import mm.mayorideas.objects.User;
import mm.mayorideas.utils.LoginUtil;

public class CommentsActivity extends AppCompatActivity implements
        CommentAPI.GetCommentsForIdeaListener {

    public static final String IDEA_ID_TAG = "idea_id";

    private CommentsAdapter adapter;
    private IdeaGETGson mIdea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        mIdea = (IdeaGETGson) getIntent().getSerializableExtra(IDEA_ID_TAG);

        ListView listView = (ListView)findViewById(R.id.comments_list);
        adapter = new CommentsAdapter(this, new LinkedList<Comment>());
        listView.setAdapter(adapter);

        showOrHideComments(false, false);
        CommentAPI.getAllCommentsForIdea(mIdea.getId(), this);
    }

    private void showOrHideComments(boolean show, boolean hasComments) {
        findViewById(R.id.loading_comments_list).setVisibility(show ? View.GONE : View.VISIBLE);
        findViewById(R.id.comments_list).setVisibility((show && hasComments) ? View.VISIBLE : View.GONE);
        findViewById(R.id.no_comments_list).setVisibility((show && !hasComments) ? View.VISIBLE : View.GONE);
    }

    public void addComment(View v) {
        if (!User.isUserLoggedIn()) {
            LoginUtil.showLoginDialog(this, R.string.login_necessary_comments);
            return;
        }

        EditText commentBox = (EditText) findViewById(R.id.comment_text_box);
        String commentText = commentBox.getText().toString();
        if (commentText.length() > 0) {
            CommentAPI.addComment(User.getCurrentUser().getID(), mIdea.getId(), commentText);
            adapter.insert(
                    new Comment(
                            -1,
                            User.getCurrentUser().getID(),
                            mIdea.getId(),
                            User.getCurrentUser().getName(),
                            commentText,
                            new Timestamp(System.currentTimeMillis())),
                    0);
            commentBox.setText("");
            showOrHideComments(true, true);
        }
    }

    @Override
    public void onGetCommentsSuccess(List<Comment> comments) {
        adapter.addAll(comments);
        showOrHideComments(true, comments.size() > 0);
    }

    @Override
    public void onGetCommentsFailure() {
        Log.e("Error", "downloading all comments");
    }
}
