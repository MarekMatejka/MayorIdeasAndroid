package mm.mayorideas.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.mikepenz.iconics.view.IconicsButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnDismissListener;

import mm.mayorideas.CommentsActivity;
import mm.mayorideas.OverviewActivity;
import mm.mayorideas.R;
import mm.mayorideas.api.FollowAPI;
import mm.mayorideas.api.VoteAPI;
import mm.mayorideas.objects.Idea;
import mm.mayorideas.objects.User;
import mm.mayorideas.objects.Vote;
import mm.mayorideas.utils.LoginUtil;

public class IdeaActionBarHandler {

    private final IconicsButton mCommentAction;
    private final IconicsButton mLikeAction;
    private final IconicsButton mDislikeAction;
    private final IconicsButton mFollowAction;

    @Nullable private final IdeaStatusBarHandler mStatusBarHandler;

    private final Activity context;
    private Idea mIdea;

    /**
     * Called when the login dialog is dismissed.
     */
    private OnDismissListener onLoginDialogDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogPlus dialog) {
            if (context instanceof OverviewActivity) {
                ((OverviewActivity)context).onResume();
            }
        }
    };

    public IdeaActionBarHandler(Activity activity, Idea idea, @Nullable IdeaStatusBarHandler statusBarHandler) {
        this.mCommentAction = (IconicsButton)activity.findViewById(R.id.idea_action_bar_comment);
        this.mLikeAction = (IconicsButton)activity.findViewById(R.id.idea_action_bar_like);
        this.mDislikeAction = (IconicsButton)activity.findViewById(R.id.idea_action_bar_dislike);
        this.mFollowAction = (IconicsButton)activity.findViewById(R.id.idea_action_bar_follow);

        this.context = activity;
        this.mIdea = idea;
        this.mStatusBarHandler = statusBarHandler;

        setupActionBar(activity);
    }

    public IdeaActionBarHandler(Activity activity, Idea idea, View v, @Nullable IdeaStatusBarHandler statusBarHandler) {
        this.mCommentAction = (IconicsButton)v.findViewById(R.id.idea_action_bar_comment);
        this.mLikeAction = (IconicsButton)v.findViewById(R.id.idea_action_bar_like);
        this.mDislikeAction = (IconicsButton)v.findViewById(R.id.idea_action_bar_dislike);
        this.mFollowAction = (IconicsButton)v.findViewById(R.id.idea_action_bar_follow);

        this.context = activity;
        this.mIdea = idea;
        this.mStatusBarHandler = statusBarHandler;

        setupActionBar(activity);
    }

    private void setupActionBar(Activity activity) {
        setupCommentAction(activity);
        setupLikeAction();
        setupDislikeAction();
        setupFollowAction();
        setupUserValues();
    }

    private void setupUserValues() {
        int userVote = mIdea.getUserVote();
        mLikeAction.setSelected(userVote == 1);
        mDislikeAction.setSelected(userVote == -1);

        if(mIdea.isUserFollowing()) {
            mFollowAction.setSelected(true);
            mFollowAction.setText("{faw-heart}");
        } else {
            mFollowAction.setSelected(false);
            mFollowAction.setText("{faw-heart-o}");
        }
    }

    private void setupCommentAction(final Activity activity) {
        mCommentAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!User.isUserLoggedIn()) {
                    LoginUtil.showLoginDialog(context, R.string.login_necessary_comments, onLoginDialogDismissListener);
                    return;
                }

                //open the comment section for this idea
                Intent i = new Intent(activity, CommentsActivity.class);
                i.putExtra(CommentsActivity.IDEA_ID_TAG, mIdea);
                activity.startActivity(i);
            }
        });
    }

    private void setupLikeAction() {
        mLikeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!User.isUserLoggedIn()) {
                    LoginUtil.showLoginDialog(context, R.string.login_necessary_voting, onLoginDialogDismissListener);
                    return;
                }

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
                if (!User.isUserLoggedIn()) {
                    LoginUtil.showLoginDialog(context, R.string.login_necessary_voting, onLoginDialogDismissListener);
                    return;
                }

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
                if (!User.isUserLoggedIn()) {
                    LoginUtil.showLoginDialog(context, R.string.login_necessary_following, onLoginDialogDismissListener);
                    return;
                }

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
        if (mStatusBarHandler != null) {
            mStatusBarHandler.addLike();
        }
        //call HTTP method to add a like to the idea
        VoteAPI.castVote(User.getCurrentUser().getID(), mIdea.getId(), Vote.LIKE);
    }

    private void removeLike() {
        if (mStatusBarHandler != null) {
            mStatusBarHandler.removeLike();
        }
        //call HTTP method to remove a like from the idea
        VoteAPI.deleteVote(User.getCurrentUser().getID(), mIdea.getId());
    }

    private void addDislike() {
        if (mStatusBarHandler != null) {
            mStatusBarHandler.addDislike();
        }
        //call HTTP method to add a dislike to the idea
        VoteAPI.castVote(User.getCurrentUser().getID(), mIdea.getId(), Vote.DISLIKE);
    }

    private void removeDislike() {
        if (mStatusBarHandler != null) {
            mStatusBarHandler.removeDislike();
        }
        //call HTTP method to remove a dislike from the idea
        VoteAPI.deleteVote(User.getCurrentUser().getID(), mIdea.getId());
    }

    private void addFollowing() {
        //add the idea to the list of ideas the user is following
        FollowAPI.followIdea(User.getCurrentUser().getID(), mIdea.getId());
    }

    private void removeFollowing() {
        //remove the idea from the list of ideas the user is following
        FollowAPI.unfollowIdea(User.getCurrentUser().getID(), mIdea.getId());
    }
}
