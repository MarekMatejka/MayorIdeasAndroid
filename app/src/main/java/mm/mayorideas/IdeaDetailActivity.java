package mm.mayorideas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import mm.mayorideas.api.CommentAPI;
import mm.mayorideas.api.ImagesAPI;
import mm.mayorideas.api.listeners.SimpleNumberListListener;
import mm.mayorideas.api.listeners.SimpleNumberValueListener;
import mm.mayorideas.objects.Comment;
import mm.mayorideas.objects.Idea;
import mm.mayorideas.ui.IdeaActionBarHandler;
import mm.mayorideas.ui.IdeaStatusBarHandler;
import mm.mayorideas.utils.LoginUtil;

public class IdeaDetailActivity extends AppCompatActivity
        implements CommentAPI.GetCommentsForIdeaListener {

    public static final String IDEA_ID_TAG = "idea_id";

    private SliderLayout mSliderShow;
    private Idea mIdea;
    private IdeaStatusBarHandler mIdeaStatusBarHandler;
    private int mImagesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_detail);

        mImagesCount = 0;
        mIdea = (Idea) getIntent().getSerializableExtra(IDEA_ID_TAG);

        mSliderShow = (SliderLayout) findViewById(R.id.slider);
        addAllImagesToSlider();
        mSliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderShow.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
        mSliderShow.setPresetTransformer(SliderLayout.Transformer.Tablet);
        mSliderShow.setSliderTransformDuration(500, new LinearInterpolator());
        mSliderShow.setDuration(5000);
        mSliderShow.startAutoCycle();

        mIdeaStatusBarHandler = new IdeaStatusBarHandler(this, mIdea);
        IdeaActionBarHandler ideaActionBarHandler = new IdeaActionBarHandler(this, mIdea, mIdeaStatusBarHandler);

        initializeIdea();
    }

    private void initializeIdea() {
        TextView ideaTitle = (TextView)findViewById(R.id.idea_title);
        ideaTitle.setText(mIdea.getTitle());

        TextView authorAndDate = (TextView)findViewById(R.id.idea_author_and_time);
        authorAndDate.setText(formatAuthorAndDate(mIdea.getAuthorName(), mIdea.getDateCreated()));

        TextView ideaDescription = (TextView)findViewById(R.id.idea_description);
        ideaDescription.setText(mIdea.getDescription());

        View ideaState = findViewById(R.id.idea_detail_idea_state);
        ideaState.setBackgroundResource(mIdea.getIdeaState().getColor());
    }

    private String formatAuthorAndDate(String authorName, Timestamp dateCreated) {
        DateFormat dateFormat = new SimpleDateFormat("EEE dd.MM.yyyy", Locale.UK);
        return "by "+authorName+", "+dateFormat.format(dateCreated);
    }

    private void addAllImagesToSlider() {
        ImagesAPI.getImageIdsForIdea(mIdea.getId(), new SimpleNumberListListener() {
            @Override
            public void onSuccess(List<Integer> values) {
                mImagesCount = values.size();
                if (mImagesCount == 1) {
                    mSliderShow.stopAutoCycle();
                }

                for (Integer id : values) {
                    addImageToSlider(mSliderShow, ImagesAPI.getImageUrl(id));
                }
            }

            @Override
            public void onFailure() {
                Log.e("Error", "getting image ids");
            }
        });
    }

    private void setCommentToView(Comment comment, View v) {
        TextView name = (TextView)v.findViewById(R.id.person_name);
        TextView commentText = (TextView)v.findViewById(R.id.comment_text);
        TextView commentAdded = (TextView)v.findViewById(R.id.comment_added);

        name.setText(comment.getUserName());
        name.setTextColor(getNameColor(comment));
        commentText.setText(comment.getText());
        commentAdded.setText(comment.getDateCreated());
    }

    private int getNameColor(Comment comment) {
        int color = comment.isByCitizen() ? android.R.color.darker_gray : R.color.mayorideas_blue;
        return getResources().getColor(color);
    }

    private void addImageToSlider(SliderLayout sliderShow, String url) {
        DefaultSliderView sliderView = new DefaultSliderView(this);
        sliderView.image(url);
        sliderShow.addSlider(sliderView);
    }

    public void openAllComments(View v) {
        Intent i = new Intent(this, CommentsActivity.class);
        i.putExtra(CommentsActivity.IDEA_ID_TAG, mIdea);
        startActivity(i);
        onStop();
    }

    @Override
    public void onStop() {
        mSliderShow.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        LoginUtil.readAndSetCurrentUser(this);

        findViewById(R.id.loading_comments).setVisibility(View.VISIBLE);
        findViewById(R.id.last_2_comments).setVisibility(View.GONE);
        CommentAPI.getLast2CommentsForIdea(mIdea.getId(), this);
        CommentAPI.getNumberOfCommentsForIdea(mIdea.getId(), new SimpleNumberValueListener() {
            @Override
            public void onSuccess(int value) {
                mIdeaStatusBarHandler.setCommentCount(value);
            }

            @Override
            public void onFailure() {
                Log.e("Error", "getting nummber of comments");
            }
        });

        if (mImagesCount > 1) {
            mSliderShow.startAutoCycle();
        }
    }

    @Override
    public void onGetCommentsSuccess(List<Comment> comments) {
        findViewById(R.id.loading_comments).setVisibility(View.GONE);
        findViewById(R.id.last_2_comments).setVisibility(View.VISIBLE);

        if (comments.size() >= 1) {
            setCommentToView(comments.get(0), findViewById(R.id.comment1));
            findViewById(R.id.comment1).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.comment1).setVisibility(View.GONE);
        }

        if (comments.size() == 2) {
            setCommentToView(comments.get(1), findViewById(R.id.comment2));
            findViewById(R.id.comment2).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.comment2).setVisibility(View.GONE);
        }

        Button commentsButton = (Button)findViewById(R.id.see_all_comments);
        if (comments.size() == 0) {
            findViewById(R.id.no_comments).setVisibility(View.VISIBLE);
            commentsButton.setText(R.string.add_first_comment);
        } else {
            findViewById(R.id.no_comments).setVisibility(View.GONE);
            commentsButton.setText(R.string.see_all_comments);
        }
    }

    @Override
    public void onGetCommentsFailure() {
        Log.e("Error", "downloading comments");
    }
}
