package mm.mayorideas;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mm.mayorideas.adapters.CommentsAdapter;
import mm.mayorideas.objects.Comment;
import mm.mayorideas.ui.IdeaActionBarHandler;


public class IdeaDetailActivity extends ActionBarActivity {

    private SliderLayout mSliderShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_detail);

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

        IdeaActionBarHandler ideaActionBarHandler = new IdeaActionBarHandler(this);

        setCommentToView(new Comment("Marek M.", "Ahoj", new Date()), findViewById(R.id.comment1));
        setCommentToView(new Comment("Matus M.", getString(R.string.lorem_ipsum), new Date()), findViewById(R.id.comment2));
    }

    private void setCommentToView(Comment comment, View v) {
        TextView name = (TextView)v.findViewById(R.id.person_name);
        TextView commentText = (TextView)v.findViewById(R.id.comment_text);
        TextView commentAdded = (TextView)v.findViewById(R.id.comment_added);

        name.setText(comment.getAuthorName());
        commentText.setText(comment.getText());
        commentAdded.setText(comment.getDate());
    }

    private void addImageToSlider(SliderLayout sliderShow, String url) {
        DefaultSliderView sliderView = new DefaultSliderView(this);
        sliderView.image(url);
        sliderShow.addSlider(sliderView);
    }

    public void openAllComments(View v) {
        Intent i = new Intent(this, CommentsActivity.class);
        startActivity(i);
        onStop();
    }

    @Override
    public void onStop() {
        mSliderShow.stopAutoCycle();
        super.onStop();
    }
}
