package com.litesuits.android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.litesuits.common.R;

import java.io.InputStream;

/**
 * Created by rockgarden on 15/12/9.
 */
public class GifView extends View {

    private Resources resources;
    private Movie movie;
    private long movieStart;
    private float ratioWidth;
    private float ratioHeight;

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public GifView(Context context) {
        super(context);
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p/>
     * <p/>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see #GifView(Context, AttributeSet, int)
     */
    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute. This constructor of View allows subclasses to use their
     * own base style when they are inflating. For example, a Button class's
     * constructor would call this version of the super class constructor and
     * supply <code>R.attr.buttonStyle</code> for <var>defStyleAttr</var>; this
     * allows the theme's button style to modify all of the base view attributes
     * (in particular its background) as well as the Button class's attributes.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @see #GifView(Context, AttributeSet)
     */
    public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resources = context.getResources();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GifView);
        int resourceId = typedArray.getResourceId(R.styleable.GifView_src, -1);
        setGifResource(resourceId);
        typedArray.recycle();
    }

    public void setGifResource(int resourceId) {
        if (resourceId == -1) {
            return;
        }
        InputStream inputStream = resources.openRawResource(resourceId);
        movie = Movie.decodeStream(inputStream);
        requestLayout();
    }

    public void setGifStream(InputStream inputStream) {
        movie = Movie.decodeStream(inputStream);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (movie != null) {
            int movieWidth = movie.width();
            int movieHeight = movie.height();
            int pLeft = getPaddingLeft();
            int pRight = getPaddingRight();
            int pTop = getTop();
            int pBottom = getBottom();
            int widthSize;
            int heightSize;

            movieWidth += pLeft + pRight;
            movieHeight += pTop + pBottom;
            movieWidth=Math.max(movieWidth,getSuggestedMinimumWidth());
            movieHeight=Math.max(movieHeight,getSuggestedMinimumHeight());

            widthSize=resolveSizeAndState(movieWidth,widthMeasureSpec,0);
            heightSize=resolveSizeAndState(movieHeight,heightMeasureSpec,0);

            ratioWidth = (float) widthSize / movieWidth;
            ratioHeight=(float)heightSize/movieHeight;
            setMeasuredDimension(widthSize,heightSize);

//            int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
//            float scaleW = (float) movieWidth / (float) maximumWidth;
//            mScale = 1f / scaleW;
//            mMeasuredMovieWidth = maximumWidth;
//            mMeasuredMovieHeight = (int) (movieHeight * mScale);
//            setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);
        } else {
//            setMeasuredDimension(getSuggestedMinimumWidth(),
//                    getSuggestedMinimumHeight());
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long now = android.os.SystemClock.uptimeMillis();
        if (movieStart == 0) {
            movieStart = now;
        }
        if (movie != null) {
            int duration = movie.duration();
            if (duration == 0) {
                duration = 1000;
            }
            int realTime = (int) ((now - movieStart) % duration); //循环
            movie.setTime(realTime);
//            movie.draw(canvas, getWidth() - movie.width(),
//                    getHeight() - movie.height());
            float scale=Math.min(ratioWidth,ratioHeight);
            canvas.scale(scale,scale);
            movie.draw(canvas,0,0);
            invalidate();
        }
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute or style resource. This constructor of View allows
     * subclasses to use their own base style when they are inflating.
     * <p/>
     * When determining the final value of a particular attribute, there are
     * four inputs that come into play:
     * <ol>
     * <li>Any attribute values in the given AttributeSet.
     * <li>The style resource specified in the AttributeSet (named "style").
     * <li>The default style specified by <var>defStyleAttr</var>.
     * <li>The default style specified by <var>defStyleRes</var>.
     * <li>The base values in this theme.
     * </ol>
     * <p/>
     * Each of these inputs is considered in-order, with the first listed taking
     * precedence over the following ones. In other words, if in the
     * AttributeSet you have supplied <code>&lt;Button * textColor="#ff000000"&gt;</code>
     * , then the button's text will <em>always</em> be black, regardless of
     * what is specified in any of the styles.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @param defStyleRes  A resource identifier of a style resource that
     *                     supplies default values for the view, used only if
     *                     defStyleAttr is 0 or can not be found in the theme. Can be 0
     *                     to not look for defaults.
     * @see #GifView(Context, AttributeSet, int)
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GifView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
