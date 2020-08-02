package com.zj.nestedscrollingdemo.widget;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

/**
 * @author zhangj
 * 嵌套滑动的父布局，用来消费NestedScrollingChild2的滑动值
 */
public class ParenNestedScrollLayout extends FrameLayout implements NestedScrollingParent2 {

    private static final long ANIM_DURATION_FRACTION = 200L;

    //Header部分
    private View mIvLogo;
    private View mVCollect;

    //Collaps Content部分
    private View mClCollapsedHeader;
    private View mCollapsedContent;
    private View mRvCollapsed;

    //TopBar部分
    private View mIvSearch;
    private View mIvShare;
    private View mTvSearch;
    private View mTopBar;
    private ImageView mIvBack;
    private ImageView mIvAssemble;

    //Content部分
    private View mLlContent;
    private View mIvClose;
    private View mViewPager;
    private View mStl;

    //ShopBar部分
    private View mShopBar;

    private float topBarHeight;//topBar高度
    private float shopBarHeight;//shopBar高度
    private float contentTransY;//滑动内容初始化TransY
    private float upAlphaScaleY;//上滑时logo，收藏icon缩放、搜索icon、分享icon透明度临界值
    private float upAlphaGradientY;//上滑时搜索框、topBar背景，返回icon、拼团icon颜色渐变临界值
    private float downFlingCutOffY;//从折叠状态下滑产生fling时回弹到初始状态的临界值
    private float downCollapsedAlphaY;//下滑时折叠内容透明度临界值
    private float downShopBarTransY;//下滑时购物内容位移临界值
    private float downContentAlphaY;//下滑时收起按钮和滑动内容透明度临界值
    private float downEndY;//下滑时终点值

    private NestedScrollingParentHelper mPatentHelper;
    // 颜色渐变
    private ArgbEvaluator iconArgbEvaluator;
    private ArgbEvaluator topBarArgbEvaluator;
    // 动画
    private ValueAnimator restoreOrExpandAnimator;//收起或展开折叠内容时执行的动画
    private ValueAnimator reboundedAnim;//回弹动画

    public ParenNestedScrollLayout(@NonNull Context context) {
        this(context, null);
    }

    public ParenNestedScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParenNestedScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPatentHelper = new NestedScrollingParentHelper(this);

        iconArgbEvaluator = new ArgbEvaluator();
        topBarArgbEvaluator = new ArgbEvaluator();

        reboundedAnim = new ValueAnimator();
        reboundedAnim.setInterpolator(new DecelerateInterpolator());
        reboundedAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

            }
        });
        reboundedAnim.setDuration(ANIM_DURATION_FRACTION);

        restoreOrExpandAnimator = new ValueAnimator();
        restoreOrExpandAnimator.setInterpolator(new AccelerateInterpolator());
        restoreOrExpandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置Content部分高度
        topBarHeight = mTopBar.getMeasuredHeight();
        ViewGroup.LayoutParams layoutParams = mLlContent.getLayoutParams();
        layoutParams.height = (int) (getMeasuredHeight() - topBarHeight);
        // 重新测量
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        shopBarHeight = getResources().getDimension(R.dimen.shop_bar_height);
//        contentTransY = getResources().getDimension(R.dimen.content_trans_y);
        downShopBarTransY = contentTransY + shopBarHeight;
        upAlphaScaleY = contentTransY - dp2px(32);
        upAlphaGradientY = contentTransY - dp2px(64);
        downFlingCutOffY = contentTransY + dp2px(28);
        downCollapsedAlphaY = contentTransY + dp2px(32);
//        downContentAlphaY = getResources().getDimension(R.dimen.donw_content_alpha_y);
//        downEndY = getHeight() - getResources().getDimension(R.dimen.iv_close_height);
    }

    /**
     * 释放资源
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (restoreOrExpandAnimator.isStarted()) {
            restoreOrExpandAnimator.cancel();
            restoreOrExpandAnimator.removeAllUpdateListeners();
            restoreOrExpandAnimator = null;
        }

        if (reboundedAnim.isStarted()) {
            reboundedAnim.cancel();
            reboundedAnim.removeAllUpdateListeners();
            reboundedAnim = null;
        }
    }

    /**
     * 对NestedScrollingChild发起嵌套滑动作出应答
     *
     * @param child  布局中包含下面target的直接父View
     * @param target 发起嵌套滑动的NestedScrollingChild的View
     * @param axes   滑动方向
     * @return 返回NestedScrollingParent是否配合处理嵌套滑动
     */
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes) {
        return onStartNestedScroll(child, target, axes, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        // 只接受mLlContent内容和垂直滑动
        return child.getId() == mLlContent.getId() && axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    /**
     * NestedScrollingParent配合处理嵌套滑动回调此方法
     *
     * @param child  同上
     * @param target 同上
     * @param axes   同上
     */
    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        onNestedScrollAccepted(child, target, axes, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mPatentHelper.onNestedScrollAccepted(child, target, axes, type);

        if (restoreOrExpandAnimator.isStarted()) {
            restoreOrExpandAnimator.cancel();
        }

        checkIvClockVisibility();
    }

    /**
     * 嵌套滑动结束
     *
     * @param target 同上
     */
    @Override
    public void onStopNestedScroll(@NonNull View target) {
        onStopNestedScroll(target, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mPatentHelper.onStopNestedScroll(target, type);

        // 如果不是从初始状态转换到展开状态过程触发，return
        if (mLlContent.getTranslationY() <= contentTransY) {
            return;
        }
        // TODO: 2020/7/30 处理滑动结束

    }

    /**
     * NestedScrollingChild滑动完成后将滑动值分发给NestedScrollingParent回调此方法
     *
     * @param target       同上
     * @param dxConsumed   水平方向消费的距离
     * @param dyConsumed   垂直方向消费的距离
     * @param dxUnconsumed 水平方向剩余的距离
     * @param dyUnconsumed 垂直方向剩余的距离
     */
    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
    }

    /**
     * NestedScrollingChild滑动完之前将滑动值分发给NestedScrollingParent回调此方法
     *
     * @param target   同上
     * @param dx       水平方向的距离
     * @param dy       水平方向的距离
     * @param consumed 返回NestedScrollingParent是否消费部分或全部滑动值
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        onNestedPreScroll(target, dx, dy, consumed, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
    }

    /**
     * NestedScrollingChild在惯性滑动之前，将惯性滑动的速度和NestedScrollingChild自身是否需要消费此惯性滑动分
     * 发给NestedScrollingParent回调此方法
     *
     * @param target    同上
     * @param velocityX 水平方向的速度
     * @param velocityY 垂直方向的速度
     * @param consumed  NestedScrollingChild自身是否需要消费此惯性滑动
     * @return 返回NestedScrollingParent是否消费全部惯性滑动
     */
    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    /**
     * NestedScrollingChild在惯性滑动之前,将惯性滑动的速度分发给NestedScrollingParent
     *
     * @param target    同上
     * @param velocityX 同上
     * @param velocityY 同上
     * @return 返回NestedScrollingParent是否消费全部惯性滑动
     */
    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return false;
    }

    /**
     * @return 返回当前嵌套滑动的方向
     */
    @Override
    public int getNestedScrollAxes() {
        return mPatentHelper.getNestedScrollAxes();
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getContext().getResources().getDisplayMetrics());
    }

    /**
     * 当mLlContent下滑距离小于下滑时收起按钮和滑动内容透明度临界值关闭按钮隐藏
     */
    private void checkIvClockVisibility() {
        if (mLlContent.getTranslationY() < downContentAlphaY) {
            mIvClose.setAlpha(0);
            mIvClose.setVisibility(View.GONE);
        } else {
            mIvClose.setAlpha(1);
            mIvClose.setVisibility(View.VISIBLE);
        }
    }
}
