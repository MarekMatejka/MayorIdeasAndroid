package mm.mayorideas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;

import mm.mayorideas.adapters.CategoryAdapter;
import mm.mayorideas.adapters.IdeaImagesAdapter;
import mm.mayorideas.api.IdeaAPI;
import mm.mayorideas.api.ImagesAPI;
import mm.mayorideas.api.listeners.SimpleNumberValueListener;
import mm.mayorideas.maps.MapsHelper;
import mm.mayorideas.ui.HorizontalSpaceItemDecoration;
import mm.mayorideas.utils.LoginUtil;

import static mm.mayorideas.adapters.IdeaImagesAdapter.OnIdeaImageItemClickListener;

public class NewIdeaActivity extends AppCompatActivity {

    private static final int FROM_GALLERY = 2;
    private EditText ideaTitle;
    private EditText ideaDescription;
    private Spinner ideaCategory;
    private ActionProcessButton submitIdeaButton;
    private IdeaImagesAdapter imagesAdapter;
    private MapsHelper mapsHelper;
    private int mUploadingImages;

    private final IdeaAPI.AddNewIdeaListener addNewIdeaListener = new IdeaAPI.AddNewIdeaListener() {
        @Override
        public void onSuccess(int ideaID) {
            sendImages(ideaID);
        }

        @Override
        public void onError() {
            submitIdeaButton.setProgress(-1);
            setFieldsEnabled(true);
        }
    };

    private final SimpleNumberValueListener addImageListener = new SimpleNumberValueListener() {
        @Override
        public void onSuccess(int id) {
            mUploadingImages--;
            if (mUploadingImages == 0) {
                submitIdeaButton.setProgress(100);
                finish();
            }
        }

        @Override
        public void onFailure() {
            submitIdeaButton.setProgress(-1);
            setFieldsEnabled(true);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_idea);
        mUploadingImages = 0;
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
                            mapsHelper.getScreenCenterPosition(),
                            addNewIdeaListener);
                }
            }
        });
    }

    private void sendImages(int ideaID) {
        for (IdeaImagesAdapter.ImageEntity image : imagesAdapter.getData()) {
            if (image.hasImage()) {
                mUploadingImages++;
                ImagesAPI.sendImage(ideaID, image.getImage(), this, addImageListener);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoginUtil.readAndSetCurrentUser(this);

        if (mapsHelper == null) {
            mapsHelper = new MapsHelper(this, R.id.map);
        } else {
            mapsHelper.setUpMapIfNeeded(this, R.id.map);
        }
        mapsHelper.setAnimateCameraOnLocationChange(false);
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
            ideaTitle.setError(getString(R.string.empty_title_error));
            success = false;
        }

        String description = ideaDescription.getText().toString().trim();
        if (description.equals("")) {
            ideaDescription.setError(getString(R.string.empty_description_error));
            success = false;
        }

        if (imagesAdapter.getImageItemCount() == 0) {
            Toast.makeText(this, R.string.no_picture_error, Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_idea, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.open_camera) {
            Log.e("camera", "clicked");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(intent);
            onPause();
            //startActivityForResult(intent, 1);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
