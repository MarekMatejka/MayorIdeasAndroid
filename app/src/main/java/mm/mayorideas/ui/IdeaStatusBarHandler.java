package mm.mayorideas.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsImageView;

import mm.mayorideas.R;
import mm.mayorideas.gson.IdeaGETGson;
import mm.mayorideas.objects.IdeaCategory;

public class IdeaStatusBarHandler {

    private final IconicsImageView mCategoryIcon;
    private final TextView mCommentCountText;
    private final TextView mScoreText;
    private final TextView mVotesCountText;
    private final Activity mActivity;

    private int mCommentCount;
    private int mScore;
    private int mVotesCount;

    public IdeaStatusBarHandler(Activity activity, IdeaGETGson idea) {
        this.mActivity = activity;

        this.mCategoryIcon = (IconicsImageView)activity.findViewById(R.id.idea_status_bar_category);
        this.mCommentCountText = (TextView)activity.findViewById(R.id.idea_status_bar_comments);
        this.mScoreText = (TextView)activity.findViewById(R.id.idea_status_bar_score);
        this.mVotesCountText = (TextView)activity.findViewById(R.id.idea_status_bar_votes);

        this.mCommentCount = 15;
        this.mScore = 0;
        this.mVotesCount = 123;

        setupCategoryIcon(idea.getCategoryID());
        setupCommentCountText();
        setupScoreText();
        setupVotesCountText();
    }

    public IdeaStatusBarHandler(Activity activity, View view, IdeaGETGson idea) {
        this.mActivity = activity;

        this.mCategoryIcon = (IconicsImageView)view.findViewById(R.id.idea_status_bar_category);
        this.mCommentCountText = (TextView)view.findViewById(R.id.idea_status_bar_comments);
        this.mScoreText = (TextView)view.findViewById(R.id.idea_status_bar_score);
        this.mVotesCountText = (TextView)view.findViewById(R.id.idea_status_bar_votes);

        this.mCommentCount = 15;
        this.mScore = 0;
        this.mVotesCount = 123;

        setupCategoryIcon(idea.getCategoryID());
        setupCommentCountText();
        setupScoreText();
        setupVotesCountText();
    }

    private void setupCategoryIcon(int categoryID) {
        IdeaCategory category = IdeaCategory.getIdeaCategory(categoryID);
        mCategoryIcon.setIcon(category.getIcon());
        mCategoryIcon.setColorRes(category.getIconColorRes());
    }

    private void setupCommentCountText() {
        this.mCommentCountText.setText("" + mCommentCount);
    }

    private void setupScoreText() {
        mScoreText.setText("" + mScore);

        Resources resources = mActivity.getResources();
        if (mScore > 0) {
            mScoreText.setTextColor(resources.getColor(android.R.color.holo_green_dark));
        } else if (mScore < 0) {
            mScoreText.setTextColor(resources.getColor(android.R.color.holo_red_dark));
        } else {
            mScoreText.setTextColor(resources.getColor(android.R.color.darker_gray));
        }
    }

    private void setupVotesCountText() {
        this.mVotesCountText.setText("" + mVotesCount);
    }

    public void addLike() {
        mScore++;
        mVotesCount++;

        setupScoreText();
        setupVotesCountText();
    }

    public void addDislike() {
        mScore--;
        mVotesCount++;

        setupScoreText();
        setupVotesCountText();
    }

    public void removeLike() {
        mScore--;
        mVotesCount--;

        setupScoreText();
        setupVotesCountText();
    }

    public void removeDislike() {
        mScore++;
        mVotesCount--;

        setupScoreText();
        setupVotesCountText();
    }
}
