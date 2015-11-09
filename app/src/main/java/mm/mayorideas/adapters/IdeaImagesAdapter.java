package mm.mayorideas.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import mm.mayorideas.R;

public class IdeaImagesAdapter
        extends AbstractListAdapter<IdeaImagesAdapter.ImageEntity, IdeaImagesAdapter.ViewHolder> {

    private final Context mContext;
    private OnIdeaImageItemClickListener mListener;

    public IdeaImagesAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(mContext)
                        .inflate(R.layout.adapter_item_idea_image, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bind(mData.get(position));
    }

    public void setListener(OnIdeaImageItemClickListener listener) {
        this.mListener = listener;
    }

    public int getImageItemCount() {
        int count = 0;
        for (ImageEntity imageEntity : mData) {
            if (imageEntity.hasImage()) {
                count++;
            }
        }
        return count;
    }

    public interface OnIdeaImageItemClickListener {
        void onImageItemClicked(ImageEntity entity);
        void onAddImageItemClicked(ImageEntity entity);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View mAddPhotoLayout;
        private final ImageView mImage;
        private final ImageView mImageDelete;
        private final View mIdeaImageLayout;
        private ImageEntity mEntity;

        public ViewHolder(View v) {
            super(v);
            mAddPhotoLayout = v.findViewById(R.id.add_image_layout);
            mImage = (ImageView)v.findViewById(R.id.idea_image);
            mImageDelete = (ImageView)v.findViewById(R.id.idea_image_delete);
            mIdeaImageLayout = v.findViewById(R.id.idea_image_layout);

            mAddPhotoLayout.setVisibility(View.VISIBLE);
            mIdeaImageLayout.setVisibility(View.GONE);

            mAddPhotoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onAddImageItemClicked(mEntity);
                    }
                }
            });

            mIdeaImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onImageItemClicked(mEntity);
                    }
                }
            });

            mImageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteEntity(getPosition());
                    if (getImageItemCount() == 2) {
                        addEntity(2, new ImageEntity(null));
                    }
                }
            });
        }

        public void bind(ImageEntity entity) {
            mEntity = entity;
            if (entity.hasImage()) {
                int imageSize = R.dimen.idea_image_size;
                Picasso.with(mContext)
                       .load(entity.getImage())
                       .centerInside()
                       .resizeDimen(imageSize, imageSize)
                       .into(mImage);
                mAddPhotoLayout.setVisibility(View.GONE);
                mIdeaImageLayout.setVisibility(View.VISIBLE);
            } else {
                mAddPhotoLayout.setVisibility(View.VISIBLE);
                mIdeaImageLayout.setVisibility(View.GONE);
            }
        }
    }

    public static class ImageEntity {

        private final Uri mImage;

        public ImageEntity(Uri image) {
            this.mImage = image;
        }

        public boolean hasImage() {
            return this.mImage != null;
        }

        public Uri getImage() {
            return this.mImage;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ImageEntity that = (ImageEntity) o;

            return !(mImage != null ? !mImage.equals(that.mImage) : that.mImage != null);
        }

        @Override
        public int hashCode() {
            return mImage != null ? mImage.hashCode() : 0;
        }
    }
}
