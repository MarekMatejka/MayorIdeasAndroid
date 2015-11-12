package mm.mayorideas.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mHorizontalSpacing;

    /**
     * @param horizontalSpacing exact value in pixels
     */
    public HorizontalSpaceItemDecoration(int horizontalSpacing) {
        this.mHorizontalSpacing = horizontalSpacing;
    }

    /**
     * @param horizontalSpacingDim id of a dimension to be used as a value of horizontal spacing
     */
    public HorizontalSpaceItemDecoration(Context context, int horizontalSpacingDim) {
        this.mHorizontalSpacing = context.getResources().getDimensionPixelSize(horizontalSpacingDim);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.right = mHorizontalSpacing;
        }
    }
}
