package mm.mayorideas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.dd.processbutton.iml.ActionProcessButton;

import mm.mayorideas.adapters.CategoryAdapter;
import mm.mayorideas.adapters.IdeaImagesAdapter;
import mm.mayorideas.api.IdeaAPI;
import mm.mayorideas.maps.MapsHelper;
import mm.mayorideas.ui.HorizontalSpaceItemDecoration;

import static mm.mayorideas.adapters.IdeaImagesAdapter.OnIdeaImageItemClickListener;

public class NewIdeaActivity extends ActionBarActivity {

    private static final int FROM_GALLERY = 2;
    private EditText ideaTitle;
    private EditText ideaDescription;
    private Spinner ideaCategory;
    private ActionProcessButton submitIdeaButton;
    private IdeaImagesAdapter imagesAdapter;
    private MapsHelper mapsHelper;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_idea);
        mapsHelper = new MapsHelper(this, R.id.map);

        ideaTitle = (EditText) findViewById(R.id.idea_title);
        ideaDescription = (EditText) findViewById(R.id.idea_description);
        ideaCategory = (Spinner) findViewById(R.id.idea_category);
        CategoryAdapter adapter = new CategoryAdapter(this);
        ideaCategory.setAdapter(adapter);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.idea_image_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(this, R.dimen.images_new_idea_horizontal_spacing));
        imagesAdapter = new IdeaImagesAdapter(this);
        imagesAdapter.setListener(new OnIdeaImageItemClickListener() {
            @Override
            public void onImageItemClicked(IdeaImagesAdapter.ImageEntity entity) {
                Log.e("ImageClicked", "Image = " + entity.getImage());
            }

            @Override
            public void onAddImageItemClicked(IdeaImagesAdapter.ImageEntity entity) {
                fromGallery();
            }
        });
        recyclerView.setAdapter(imagesAdapter);
        imagesAdapter.addEntity(0, new IdeaImagesAdapter.ImageEntity(null));

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

    @Override
    protected void onResume() {
        super.onResume();
        if (mapsHelper == null) {
            mapsHelper = new MapsHelper(this, R.id.map);
        } else {
            mapsHelper.setUpMapIfNeeded(this, R.id.map);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapsHelper.connectMap();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapsHelper.stopLocationUpdates();
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

    public void fromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            imagesAdapter.addEntity(
                    imagesAdapter.getItemCount()-1,
                    new IdeaImagesAdapter.ImageEntity(intent.getData()));

            if (imagesAdapter.getItemCount() == 4) {
                imagesAdapter.deleteEntity(3);
            }
        }
    }
}
