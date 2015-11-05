package mm.mayorideas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.dd.processbutton.iml.ActionProcessButton;
import com.squareup.picasso.Picasso;

import java.io.File;

import mm.mayorideas.api.IdeaAPI;

public class NewIdeaActivity extends ActionBarActivity {

    private static final int TAKE_PHOTO = 1;
    private static final int FROM_GALLERY = 2;
    private ImageView imageView;
    private EditText ideaTitle;
    private EditText ideaDescription;
    private Spinner ideaCategory;
    private ActionProcessButton submitIdeaButton;

    private final IdeaAPI.AddNewIdeaListener addNewIdeaListener = new IdeaAPI.AddNewIdeaListener() {
        @Override
        public void onSuccess(int ideaID) {
            submitIdeaButton.setProgress(100);
            finish();
        }

        @Override
        public void onError() {
            submitIdeaButton.setProgress(-1);
            setFieldsEnabled(true);
        }
    };
    private File photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_idea);
        imageView = (ImageView) findViewById(R.id.imageViewTest);

        ideaTitle = (EditText) findViewById(R.id.idea_title);
        ideaDescription = (EditText) findViewById(R.id.idea_description);
        ideaCategory = (Spinner) findViewById(R.id.idea_category);

        submitIdeaButton = (ActionProcessButton) findViewById(R.id.btnSignIn);
        submitIdeaButton.setMode(ActionProcessButton.Mode.ENDLESS);
        submitIdeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    setFieldsEnabled(false);
                    submitIdeaButton.setProgress(1);
                    IdeaAPI.addNewIdea(
                            ideaTitle.getText().toString(),
                            ideaDescription.getText().toString(),
                            ideaCategory.getSelectedItemPosition() + 1,
                            addNewIdeaListener);
                }
            }
        });
    }

    private void setFieldsEnabled(boolean enabled) {
        submitIdeaButton.setEnabled(enabled);
        ideaTitle.setEnabled(enabled);
        ideaDescription.setEnabled(enabled);
        ideaCategory.setEnabled(enabled);
    }

    private boolean validateFields() {
        boolean success = true;

        String title = ideaTitle.getText().toString().trim();
        if (title.equals("")) {
            ideaTitle.setError("Title must not be empty!");
            success = false;
        }

        String description = ideaDescription.getText().toString().trim();
        if (description.equals("")) {
            ideaDescription.setError("Description must not be empty!");
            success = false;
        }

        return success;
    }


//    public void getLocation(View v) {
//        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
//            @Override
//            public void gotLocation(Location location){
//                //Got the location!
//                Log.e("latitude", ""+location.getLatitude());
//                Log.e("longitude", "" + location.getLongitude());
//            }
//        };
//        MyLocation myLocation = new MyLocation();
//        myLocation.getLocation(this, locationResult);
//    }

    public void fromGallery(View View) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FROM_GALLERY);
    }

    public void takePhoto(View v) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            Picasso.with(this).load(intent.getData()).resize(150, 150).into(imageView);
        }
    }
}
