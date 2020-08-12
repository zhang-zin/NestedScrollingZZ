package com.zj.nestedscrollingdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

public class TopBarLayout extends ConstraintLayout {
    public TopBarLayout(Context context) {
        super(context);
    }

    public TopBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() + getStatusBarHeight(), View.MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }

    /**
     * 获取状态栏高度
     */
    private int getStatusBarHeight() {
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getContext().getResources().getDimensionPixelSize(resourceId);
    }
}
