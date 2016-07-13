package com.android.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.common.log.Logger;

/**
 * 功能：view工具类，用于适配UI
 * 作者：yangtao
 * 创建时间：2016/4/8 16:31
 */
public class ACViewUtils {
    private static String TAG = ACViewUtils.class.getName();

    /**  UI设计的基准宽度. */
    public static int UI_WIDTH = 720;
    /**  UI设计的基准高度. */
    public static int UI_HEIGHT = 1080;

    /**
     * 无效值
     */
    public static final int INVALID = Integer.MIN_VALUE;

    /**
     * @param activity activity
     * @return 1 竖屏 0 横屏
     */
    public static int getScreenDirection(Activity activity){
        return activity.getResources().getConfiguration().orientation;
    }

    /**
     * 获取屏幕尺寸与密度.
     *
     * @param context the context
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null){
            mResources = Resources.getSystem();

        }else{
            mResources = context.getResources();
        }
        return mResources.getDisplayMetrics();
    }

    /**
     * 获取dialog宽度
     */
    public static int getDialogW(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.widthPixels - 100;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenW(Context aty) {
        DisplayMetrics dm;
        dm = aty.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenH(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    //获取屏幕高宽
    @SuppressLint("NewApi")
    public static Point getScreenSize(Activity activity){
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用

        Point size = new Point();
        d.getSize(size);
        return size;
    }

    // 在values中dimens的大小
    public static int getIntFromDimens(int index, Context context) {
        return context.getResources().getDimensionPixelSize(index);
    }

    /**
     * 描述：layout转view
     *
     * @param context
     *            上下文
     * @param id
     *            layout id
     * @return view
     */
    public static View layoutToView(Context context, int id){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(id, null);
    }

    /**
     * 描述：重置AbsListView的高度. item 的最外层布局要用
     * RelativeLayout,如果计算的不准，就为RelativeLayout指定一个高度
     *
     * @param absListView
     *            the abs list view
     * @param lineNumber
     *            每行几个 ListView一行一个item
     * @param verticalSpace
     *            the vertical space
     *
     */
    public static void setAbsListViewHeight(AbsListView absListView,
                                            int lineNumber, int verticalSpace) {

        int totalHeight = getAbsListViewHeight(absListView, lineNumber,
                verticalSpace);
        ViewGroup.LayoutParams params = absListView.getLayoutParams();
        params.height = totalHeight;
        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        absListView.setLayoutParams(params);
    }

    /**
     * 描述：获取ListView的高度.
     *
     * @param absListView            the abs list view
     * @param lineNumber            每行几个 ListView一行一个item
     * @param verticalSpace            the vertical space
     * @return the abs list view height
     */
    public static int getAbsListViewHeight(AbsListView absListView,
                                           int lineNumber, int verticalSpace) {
        int totalHeight = 0;
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        absListView.measure(w, h);
        ListAdapter mListAdapter = absListView.getAdapter();
        if (mListAdapter == null) {
            return totalHeight;
        }

        int count = mListAdapter.getCount();
        if (absListView instanceof ListView) {
            for (int i = 0; i < count; i++) {
                View listItem = mListAdapter.getView(i, null, absListView);
                listItem.measure(w, h);
                totalHeight += listItem.getMeasuredHeight();
            }
            if (count == 0) {
                totalHeight = verticalSpace;
            } else {
                totalHeight = totalHeight
                        + (((ListView) absListView).getDividerHeight() * (count - 1));
            }

        } else if (absListView instanceof GridView) {
            int remain = count % lineNumber;
            if (remain > 0) {
                remain = 1;
            }
            if (mListAdapter.getCount() == 0) {
                totalHeight = verticalSpace;
            } else {
                View listItem = mListAdapter.getView(0, null, absListView);
                listItem.measure(w, h);
                int line = count / lineNumber + remain;
                totalHeight = line * listItem.getMeasuredHeight() + (line - 1)
                        * verticalSpace;
            }

        }
        return totalHeight;
    }

    /**
     * 测量这个view
     * 最后通过getMeasuredWidth()获取宽度和高度.
     * @param view 要测量的view
     * 测量过的view
     */
    public static void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
                    View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 描述：dip转换为px.
     *
     * @param context the context
     * @param dipValue the dip value
     * @return px值
     */
    public static float dip2px(Context context, float dipValue) {
        DisplayMetrics mDisplayMetrics = getDisplayMetrics(context);
        return applyDimension(TypedValue.COMPLEX_UNIT_DIP,dipValue,mDisplayMetrics);
    }

    /**
     * 描述：px转换为dip.
     *
     * @param context the context
     * @param pxValue the px value
     * @return dip值
     */
    public static float px2dip(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.density;
    }

    /**
     * 描述：sp转换为px.
     *
     * @param context the context
     * @param spValue the sp value
     * @return sp值
     */
    public static float sp2px(Context context, float spValue) {
        DisplayMetrics mDisplayMetrics = getDisplayMetrics(context);
        return applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, mDisplayMetrics);
    }

    /**
     * 描述：px转换为sp.
     *
     * @param context the context
     * @param pxValue the sp value
     * @return sp值
     */
    public static float px2sp(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.scaledDensity;
    }

    /**
     * 描述：dp转换为px.
     *
     * @param dp the context
     * @return sp值
     */
    public static int dp2px(Context context,int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }


    /**
     * 描述：根据屏幕大小缩放.
     *
     * @param context the context
     * @param value the px value
     * @return the int
     */
    public static int scale(Context context, float value) {
        DisplayMetrics mDisplayMetrics = getDisplayMetrics(context);
        return scale(mDisplayMetrics.widthPixels,
                mDisplayMetrics.heightPixels, value);
    }

    /**
     * 描述：根据屏幕大小缩放.
     *
     * @param displayWidth the display width
     * @param displayHeight the display height
     * @param pxValue the px value
     * @return the int
     */
    public static int scale(int displayWidth, int displayHeight, float pxValue) {
        if(pxValue == 0 ){
            return 0;
        }
        float scale = 1;
        try {
            float scaleWidth = (float) displayWidth / UI_WIDTH;
            float scaleHeight = (float) displayHeight / UI_HEIGHT;
            scale = Math.min(scaleWidth, scaleHeight);
        } catch (Exception e) {
            Logger.e(TAG, e.toString());
        }
        return Math.round(pxValue * scale + 0.5f);
    }

    /**
     * TypedValue官方源码中的算法，任意单位转换为PX单位
     * @param unit  TypedValue.COMPLEX_UNIT_DIP
     * @param value 对应单位的值
     * @param metrics 密度
     * @return px值
     */
    public static float applyDimension(int unit, float value,
                                       DisplayMetrics metrics){
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                return value;
            case TypedValue.COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case TypedValue.COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case TypedValue.COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f/72);
            case TypedValue.COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case TypedValue.COMPLEX_UNIT_MM:
                return value * metrics.xdpi * (1.0f/25.4f);
        }
        return 0;
    }

    /**
     *
     * 描述：View树递归调用做适配.
     * uiWidth = 1080;
     * uiHeight = 700;
     * scaleContentView((RelativeLayout)findViewById(R.id.rootLayout));
     * 要求布局中的单位都用px并且和美工的设计图尺寸一致，包括所有宽高，Padding,Margin,文字大小
     * @param contentView ViewGroup
     */
    public static void scaleContentView(ViewGroup contentView){
        scaleView(contentView);
        if(contentView.getChildCount()>0){
            for(int i=0;i<contentView.getChildCount();i++){
                if(contentView.getChildAt(i) instanceof ViewGroup){
                    scaleContentView((ViewGroup)(contentView.getChildAt(i)));
                }else{
                    scaleView(contentView.getChildAt(i));
                }
            }
        }
    }

    /**
     * 按比例缩放View，以布局中的尺寸为基准
     * @param view view
     */
    public static void scaleView(View view){
        if (view instanceof TextView){
            TextView textView = (TextView) view;
            setTextSize(textView,textView.getTextSize());
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (null != params){
            int width = INVALID;
            int height = INVALID;
            if (params.width != ViewGroup.LayoutParams.WRAP_CONTENT
                    && params.width != ViewGroup.LayoutParams.MATCH_PARENT){
                width = params.width;
            }

            if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT
                    && params.height != ViewGroup.LayoutParams.MATCH_PARENT){
                height = params.height;
            }

            //size
            setViewSize(view,width,height);

            // Padding
            setPadding(view,view.getPaddingLeft(),view.getPaddingTop(),view.getPaddingRight(),view.getPaddingBottom());
        }

        // Margin
        if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
            ViewGroup.MarginLayoutParams mMarginLayoutParams = (ViewGroup.MarginLayoutParams) view
                    .getLayoutParams();
            if (mMarginLayoutParams != null){
                setMargin(view,mMarginLayoutParams.leftMargin,mMarginLayoutParams.topMargin,mMarginLayoutParams.rightMargin,mMarginLayoutParams.bottomMargin);
            }
        }

    }

    /**
     * 缩放文字大小
     * @param textView button
     * @param size sp值
     */
    public static void setSPTextSize(TextView textView,float size) {
        float scaledSize = scale(textView.getContext(),size);
        textView.setTextSize(scaledSize);
    }

    /**
     * 缩放文字大小,这样设置的好处是文字的大小不和密度有关，
     * 能够使文字大小在不同的屏幕上显示比例正确
     * @param textView button
     * @param sizePixels px值
     */
    public static void setTextSize(TextView textView,float sizePixels) {
        float scaledSize = scale(textView.getContext(), sizePixels);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,scaledSize);
    }

    /**
     * 缩放文字大小
     * @param context 上下文
     * @param textPaint textPaint
     * @param sizePixels px值
     */
    public static void setTextSize(Context context,TextPaint textPaint,float sizePixels) {
        float scaledSize = scale(context,sizePixels);
        textPaint.setTextSize(scaledSize);
    }

    /**
     * 缩放文字大小
     * @param context 上下文
     * @param paint paint
     * @param sizePixels px值
     */
    public static void setTextSize(Context context,Paint paint,float sizePixels) {
        float scaledSize = scale(context,sizePixels);
        paint.setTextSize(scaledSize);
    }

    /**
     * 设置View的PX尺寸
     * @param view  如果是代码new出来的View，需要设置一个适合的LayoutParams
     * @param widthPixels widthPixels
     * @param heightPixels heightPixels
     */
    public static void setViewSize(View view,int widthPixels, int heightPixels){
        int scaledWidth = scale(view.getContext(), widthPixels);
        int scaledHeight = scale(view.getContext(), heightPixels);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if(params == null){
            Logger.e(TAG, "setViewSize出错,如果是代码new出来的View，需要设置一个适合的LayoutParams");
            return;
        }
        if (widthPixels != INVALID){
            params.width = scaledWidth;
        }
        if (heightPixels != INVALID){
            params.height = scaledHeight;
        }
        view.setLayoutParams(params);
    }

    /**
     * 设置PX padding.
     *
     * @param view the view
     * @param left the left padding in pixels
     * @param top the top padding in pixels
     * @param right the right padding in pixels
     * @param bottom the bottom padding in pixels
     */
    public static void setPadding(View view, int left,
                                  int top, int right, int bottom) {
        int scaledLeft = scale(view.getContext(), left);
        int scaledTop = scale(view.getContext(), top);
        int scaledRight = scale(view.getContext(), right);
        int scaledBottom = scale(view.getContext(), bottom);
        view.setPadding(scaledLeft, scaledTop, scaledRight, scaledBottom);
    }

    /**
     * 设置 PX margin.
     *
     * @param view the view
     * @param left the left margin in pixels
     * @param top the top margin in pixels
     * @param right the right margin in pixels
     * @param bottom the bottom margin in pixels
     */
    public static void setMargin(View view, int left, int top,
                                 int right, int bottom) {
        int scaledLeft = scale(view.getContext(), left);
        int scaledTop = scale(view.getContext(), top);
        int scaledRight = scale(view.getContext(), right);
        int scaledBottom = scale(view.getContext(), bottom);

        if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
            ViewGroup.MarginLayoutParams mMarginLayoutParams = (ViewGroup.MarginLayoutParams) view
                    .getLayoutParams();
            if (left != INVALID) {
                mMarginLayoutParams.leftMargin = scaledLeft;
            }
            if (right != INVALID) {
                mMarginLayoutParams.rightMargin = scaledRight;
            }
            if (top != INVALID) {
                mMarginLayoutParams.topMargin = scaledTop;
            }
            if (bottom != INVALID) {
                mMarginLayoutParams.bottomMargin = scaledBottom;
            }
            view.setLayoutParams(mMarginLayoutParams);
        }

    }

    /**
     * view设置background drawable
     *
     * @param view view
     * @param drawable d
     */
    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return 控件高度
     */
    public static int getViewMeasuredHeight(View view) {
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }

    /**
     * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
     *
     * @param view
     * @return 控件宽度
     */
    public static int getViewMeasuredWidth(View view) {
        calcViewMeasure(view);
        return view.getMeasuredWidth();
    }

    /**
     * 测量控件的尺寸
     *
     * @param view
     */
    public static void calcViewMeasure(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }

    /**
     * 从父亲布局中移除自己
     * @param v view
     */
    public static void removeSelfFromParent(View v) {
        ViewParent parent = v.getParent();
        if(parent != null){
            if(parent instanceof ViewGroup){
                ((ViewGroup)parent).removeView(v);
            }
        }
    }

    /**
     * 使用ColorFilter来改变亮度
     *
     * @param imageview
     * @param brightness
     */
    public static void changeBrightness(ImageView imageview, float brightness) {
        imageview.setColorFilter(getBrightnessMatrixColorFilter(brightness));
    }
    public static void changeBrightness(Drawable drawable, float brightness) {
        drawable.setColorFilter(getBrightnessMatrixColorFilter(brightness));
    }
    private static ColorMatrixColorFilter getBrightnessMatrixColorFilter(float brightness) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(
                new float[]{
                        1, 0, 0, 0, brightness,
                        0, 1, 0, 0, brightness,
                        0, 0, 1, 0, brightness,
                        0, 0, 0, 1, 0
                });
        return new ColorMatrixColorFilter(matrix);
    }
}
