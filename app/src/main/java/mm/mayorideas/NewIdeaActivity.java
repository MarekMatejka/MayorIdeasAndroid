package mm.mayorideas;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.dd.processbutton.iml.ActionProcessButton;

import mm.mayorideas.api.IdeaAPI;

public class NewIdeaActivity extends ActionBarActivity {

    private static final int TAKE_PHOTO = 1;
    private static final int FROM_GALLERY = 2;
    private Bitmap bitmap;
    private ImageView imageView;
    private static Uri imageUri;
    private String imagePath;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_idea);
        imageView = (ImageView) findViewById(R.id.result);

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

    /*
    public void getLocation(View v) {
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Got the location!
                Log.e("latitude", ""+location.getLatitude());
                Log.e("longitude", "" + location.getLongitude());
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);
    }

    public void fromGallery(View View) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FROM_GALLERY);
    }

    public void takePhoto(View v) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "mi.png");
        imageUri = Uri.fromFile(photo);
        imagePath = photo.getAbsolutePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //super.onActivityResult(requestCode, resultCode, intent);
        InputStream stream = null;

        Uri selectedImage = null;
        if (requestCode == TAKE_PHOTO) {
            selectedImage = imageUri;
            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), 256, 256);
            imageView.setImageBitmap(thumbImage);
            return;
        } else if (requestCode == FROM_GALLERY) {
            selectedImage = intent.getData();
        } else {
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            getContentResolver().notifyChange(selectedImage, null);
            try {
                if (bitmap != null) {
                    bitmap.recycle();
                }
                stream = getContentResolver().openInputStream(selectedImage);
//                Bitmap b = MediaStore.Images.Thumbnails.getThumbnail(
//                        getContentResolver(), selectedImage,
//                        MediaStore.Images.Thumbnails.MINI_KIND,
//                        (BitmapFactory.Options) null );
                bitmap = BitmapFactory.decodeStream(stream);

                imageView.setImageBitmap(createResizedBitmap(bitmap, selectedImage, 1920));
                bitmap = null;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private int getOrientation(Uri imageUri) {
        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
        Cursor cur = managedQuery(imageUri, orientationColumn, null, null, null);
        int orientation = 0;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        }
        return orientation;
    }

    private Bitmap createResizedBitmap(Bitmap image, Uri imageUri, int maxSize) {
        Matrix matrix = new Matrix();
        matrix.postRotate(getOrientation(imageUri));

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio < 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        Bitmap scaledImage = Bitmap.createScaledBitmap(image, width, height, true);
        Bitmap b = Bitmap.createBitmap(scaledImage, 0, 0, width, height, matrix, true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
    }
    */
}
