package mm.mayorideas;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

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
    }

    private void addImageToSlider(SliderLayout sliderShow, String url) {
        DefaultSliderView sliderView = new DefaultSliderView(this);
        sliderView.image(url);
        sliderShow.addSlider(sliderView);
    }

    @Override
    public void onStop() {
        mSliderShow.stopAutoCycle();
        super.onStop();
    }
}
