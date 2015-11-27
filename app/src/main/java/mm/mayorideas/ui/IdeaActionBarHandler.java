package mm.mayorideas.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.mikepenz.iconics.view.IconicsButton;

import mm.mayorideas.CommentsActivity;
import mm.mayorideas.R;

public class IdeaActionBarHandler {

    private final IconicsButton mCommentAction;
    private final IconicsButton mLikeAction;
    private final IconicsButton mDislikeAction;
    private final IconicsButton mFollowAction;

    public IdeaActionBarHandler(Activity activity) {
        this.mCommentAction = (IconicsButton)activity.findViewById(R.id.idea_action_bar_comment);
        this.mLikeAction = (IconicsButton)activity.findViewById(R.id.idea_action_bar_like);
        this.mDislikeAction = (IconicsButton)activity.findViewById(R.id.idea_action_bar_dislike);
        this.mFollowAction = (IconicsButton)activity.findViewById(R.id.idea_action_bar_follow);

        setupCommentAction(activity);
        setupLikeAction();
        setupDislikeAction();
        setupFollowAction();
    }

    private void setupCommentAction(final Activity activity) {
        mCommentAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open the comment section for this idea
                Intent i = new Intent(activity, CommentsActivity.class);
                activity.startActivity(i);
            }
        });
    }

    private void setupLikeAction() {
        mLikeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDislikeAction.isSelected()) {
                    removeDislike();
                    mDislikeAction.setSelected(false);
                }

                if (!mLikeAction.isSelected()) {
                    addLike();
                } else {
                    removeLike();
                }
                mLikeAction.setSelected(!mLikeAction.isSelected());
            }
        });
    }

    private void setupDislikeAction() {
        mDislikeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLikeAction.isSelected()) {
                    removeLike();
                    mLikeAction.setSelected(false);
                }

                if (!mDislikeAction.isSelected()) {
                    addDislike();
                } else {
                    removeDislike();
                }
                mDislikeAction.setSelected(!mDislikeAction.isSelected());
            }
        });
    }

    private void setupFollowAction() {
        mFollowAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFollowAction.isSelected()) {
                    addFollowing();
                    mFollowAction.setText("{faw-heart}");
                } else {
                    removeFollowing();
                    mFollowAction.setText("{faw-heart-o}");
                }
                mFollowAction.setSelected(!mFollowAction.isSelected());
            }
        });
    }

    private void addLike() {
        //call HTTP method to add a like to the idea
    }

    private void removeLike() {
        //call HTTP method to remove a like from the idea
    }

    private void addDislike() {
        //call HTTP method to add a dislike to the idea
    }

    private void removeDislike() {
        //call HTTP method to remove a dislike from the idea
    }

    private void addFollowing() {
        //add the idea to the list of ideas the user is following
    }

    private void removeFollowing() {
        //remove the idea from the list of ideas the user is following
    }
}
