package mm.mayorideas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsButton;

import mm.mayorideas.objects.User;
import mm.mayorideas.utils.LoginUtil;

public class MyAccountFragment extends Fragment {

    private OnUserStatClickedListener mListener;

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
        IconicsButton login = (IconicsButton)layout.findViewById(R.id.user_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOrLogout();
            }
        });

        User user = User.getCurrentUser();

        if (user.getID() != -1) {
            layout.findViewById(R.id.user_details).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.user_not_logged_in).setVisibility(View.GONE);
            login.setText(R.string.logout);

            TextView name = (TextView) layout.findViewById(R.id.account_name);
            name.setText(user.getName());

            TextView username = (TextView) layout.findViewById(R.id.account_username);
            username.setText(user.getUsername());

            createUserStat(layout.findViewById(R.id.user_ideas), UserStat.IDEA, 25);
            createUserStat(layout.findViewById(R.id.user_comments), UserStat.COMMENT, 25);
            createUserStat(layout.findViewById(R.id.user_votes), UserStat.VOTES, 25);
            createUserStat(layout.findViewById(R.id.user_follows), UserStat.FOLLOWS, 25);
        } else {
            layout.findViewById(R.id.user_details).setVisibility(View.GONE);
            layout.findViewById(R.id.user_not_logged_in).setVisibility(View.VISIBLE);
            login.setText(R.string.login_or_signup);
        }

        return layout;
    }

    private void createUserStat(View layout, final UserStat stat, int statValue) {
        TextView name = (TextView)layout.findViewById(R.id.count_text);
        TextView value = (TextView)layout.findViewById(R.id.count);
        IconicsButton goTo = (IconicsButton)layout.findViewById(R.id.go_to);

        if (stat.hasGoToFragment()) {
            goTo.setVisibility(View.VISIBLE);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onUserStatClicked(stat.getGoToFragment());
                    }
                }
            });
        } else {
            goTo.setVisibility(View.INVISIBLE);
        }

        name.setText(getText(stat.getName()));
        value.setText(String.valueOf(statValue));
        value.setTextColor(getResources().getColor(stat.getColor()));
    }

    public void setOnUserStatClickedListener(OnUserStatClickedListener listener) {
        this.mListener = listener;
    }

    private void loginOrLogout() {
        if (User.isUserLoggedIn()) {
            LoginUtil.logout(getContext());
        } else {
            LoginUtil.showLoginDialog(getActivity(), R.string.login_to_start, null);
        }
    }

    private enum UserStat {
        IDEA(R.string.ideas_title, R.color.mayorideas_blue, MyIdeasListFragment.newInstance()),
        COMMENT(R.string.comments_title, android.R.color.holo_blue_light, null),
        VOTES(R.string.votes_title, android.R.color.holo_green_light, null),
        FOLLOWS(R.string.following_title, android.R.color.holo_red_dark, FollowingIdeasListFragment.newInstance());

        private final int name, color;
        private final IdeaListFragment fragment;

        UserStat(int name, int color, IdeaListFragment fragment) {
            this.name = name;
            this.color = color;
            this.fragment = fragment;
        }

        public int getName() {
            return name;
        }

        public int getColor() {
            return color;
        }

        public boolean hasGoToFragment() {
            return fragment != null;
        }

        public IdeaListFragment getGoToFragment() {
            return fragment;
        }
    }

    public interface OnUserStatClickedListener {
        void onUserStatClicked(IdeaListFragment goToFragment);
    }
}
