package mm.mayorideas;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import mm.mayorideas.adapters.CommentsAdapter;
import mm.mayorideas.objects.Comment;


public class CommentsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        List<Comment> comments = new LinkedList<>();
        comments.add(new Comment("Marek M.", getString(R.string.lorem_ipsum), new Date()));
        comments.add(new Comment("Marek M.", "Ahoj Marek ako sa mas?", new Date()));
        comments.add(new Comment("Marek M.", getString(R.string.lorem_ipsum), new Date()));
        comments.add(new Comment("Marek M.", "test listu", new Date()));

        ListView listView = (ListView)findViewById(R.id.comments_list);
        CommentsAdapter adapter = new CommentsAdapter(this, comments);
        listView.setAdapter(adapter);
    }
}
