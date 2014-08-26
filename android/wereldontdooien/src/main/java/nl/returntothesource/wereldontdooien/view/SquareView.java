package nl.returntothesource.wereldontdooien.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareView extends ImageView {
    public SquareView(Context context) {
        super(context);
    }

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec > heightMeasureSpec) {
            widthMeasureSpec = heightMeasureSpec;
        } else {
            heightMeasureSpec = widthMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
