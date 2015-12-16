package mm.mayorideas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.List;

import mm.mayorideas.api.CommentAPI;
import mm.mayorideas.api.listeners.SimpleNumberValueListener;
import mm.mayorideas.gson.IdeaGETGson;
import mm.mayorideas.objects.Comment;
import mm.mayorideas.ui.IdeaActionBarHandler;
import mm.mayorideas.ui.IdeaStatusBarHandler;

public class IdeaDetailActivity extends AppCompatActivity
        implements CommentAPI.GetCommentsForIdeaListener {

    public static final String IDEA_ID_TAG = "idea_id";

    private SliderLayout mSliderShow;
    private IdeaGETGson mIdea;
    private IdeaStatusBarHandler mIdeaStatusBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_detail);

        mIdea = (IdeaGETGson) getIntent().getSerializableExtra(IDEA_ID_TAG);

        mSliderShow = (SliderLayout) findViewById(R.id.slider);
        addImageToSlider(mSliderShow, "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
        addImageToSlider(mSliderShow, "http://4.bp.blogspot.com/-zMD8rPGL3PU/VEP2Dis9tyI/AAAAAAABiIA/7cx90zLWVlw/s1600/TBBT%2Bheader%2B1.jpg");
        addImageToSlider(mSliderShow, "http://cdn.traileraddict.com/content/20th-century-fox/martian2015-2.jpg");

        mSliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderShow.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
        mSliderShow.setPresetTransformer(SliderLayout.Transformer.Tablet);
        mSliderShow.setSliderTransformDuration(500, new LinearInterpolator());
        mSliderShow.setDuration(5000);
        mSliderShow.startAutoCycle();

        mIdeaStatusBarHandler = new IdeaStatusBarHandler(this, mIdea);
        IdeaActionBarHandler ideaActionBarHandler = new IdeaActionBarHandler(this, mIdea, mIdeaStatusBarHandler);
    }

    private void setCommentToView(Comment comment, View v) {
        TextView name = (TextView)v.findViewById(R.id.person_name);
        TextView commentText = (TextView)v.findViewById(R.id.comment_text);
        TextView commentAdded = (TextView)v.findViewById(R.id.comment_added);

        name.setText(comment.getUserName());
        commentText.setText(comment.getText());
        commentAdded.setText(comment.getDateCreated());
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
    }

    @Override
    public void onSuccess(List<Comment> comments) {
        if (comments.size() >= 1) {
            setCommentToView(comments.get(0), findViewById(R.id.comment1));
        } else {
            findViewById(R.id.comment1).setVisibility(View.GONE);
        }

        if (comments.size() == 2) {
            setCommentToView(comments.get(1), findViewById(R.id.comment2));
        } else {
            findViewById(R.id.comment2).setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure() {
        Log.e("Error", "downloading comments");
    }
}
