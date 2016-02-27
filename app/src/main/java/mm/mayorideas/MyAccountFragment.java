package mm.mayorideas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mm.mayorideas.objects.User;

public class MyAccountFragment extends Fragment {

    public MyAccountFragment() {
        // Required empty public constructor
    }

    public static MyAccountFragment newInstance() {
        return new MyAccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my_account, container, false);

        User user = User.getCurrentUser();

        TextView name = (TextView)layout.findViewById(R.id.account_name);
        name.setText(user.getName());

        TextView username = (TextView)layout.findViewById(R.id.account_username);
        username.setText(user.getUsername());

        TextView ideas = (TextView)layout.findViewById(R.id.account_ideas_count);
        ideas.setText("4");

        TextView comments = (TextView)layout.findViewById(R.id.account_comments_count);
        comments.setText("42");

        TextView votes = (TextView)layout.findViewById(R.id.account_votes_count);
        votes.setText("126");

        TextView follows = (TextView)layout.findViewById(R.id.account_follows_count);
        follows.setText("15");

        return layout;
    }
}
