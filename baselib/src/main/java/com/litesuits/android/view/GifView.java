package com.litesuits.android.view;

import android.annotation.SuppressLint;
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

    private static final int DEFAULT_MOVIEW_DURATION = 1000;
    private int resourceId;
    private Resources resources;
    private Movie movie;
    private long movieStart;
    private int currentAnimationTime = 0;
    private float ratioWidth;
    private float ratioHeight;
    /**
     * Position for drawing animation frames in the center of the view.
     */
    private float left;
    private float top;
    //Scaling factor to fit the animation within view bounds.
    private float scale;
    /**
     * Scaled movie frames width and height.
     */
    private int measuredMovieWidth;
    private int measuredMovieHeight;

    private volatile boolean paused = false;
    private boolean visible = true;

    /**
     * "super(context)"to"this(context, null)",Call the next level of construction method.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @see #GifView(Context, AttributeSet)
     */
    public GifView(Context context) {
        this(context, null);
    }

    /**
     * add parameter defStyleAttr=0 to Call the next level of construction method.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see #GifView(Context, AttributeSet, int)
     */
    public GifView(Context context, AttributeSet attrs) {
        this(context, attrs, R.styleable.GifView_gifViewStyle);
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
        /**
         * Starting from HONEYCOMB have to turn off HW acceleration to draw Movie on Canvas.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        resources = context.getResources();
        //TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GifView);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GifView, defStyleAttr,
                R.style.View_GifView);
        resourceId = typedArray.getResourceId(R.styleable.GifView_src, -1);
        paused = typedArray.getBoolean(R.styleable.GifView_src, false);
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

    public void setMovie(Movie movie) {
        this.movie = movie;
        requestLayout();
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovieTime(int time) {
        currentAnimationTime = time;
        invalidate();
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        //Calculate new movie start time, so that it resumes from the same frame.
        if (!paused) {
            movieStart = android.os.SystemClock.uptimeMillis() - currentAnimationTime;
        }
        invalidate();
    }

    public boolean isPaused() {
        return this.paused;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (movie != null) {
            int movieWidth = movie.width();
            int movieHeight = movie.height();

            //Calculate horizontal scaling
            float scaleH = 1f;
            int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);
            if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
                int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
                if (movieWidth > maximumWidth) {
                    scaleH = (float) movieWidth / (float) maximumWidth;
                }
            }
            //calculate vertical scaling
            float scaleW = 1f;
            int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);
            if (measureModeHeight != MeasureSpec.UNSPECIFIED) {
                int maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
                if (movieHeight > maximumHeight) {
                    scaleW = (float) movieHeight / (float) maximumHeight;
                }
            }
            //calculate overall scale
            scale = 1f / Math.max(scaleH, scaleW);
            measuredMovieWidth = (int) (movieWidth * scale);
            measuredMovieHeight = (int) (movieHeight * scale);
            setMeasuredDimension(measuredMovieWidth, measuredMovieHeight);

//            if (movieWidth <= 0)
//                movieWidth = 1;
//            if (movieHeight <= 0)
//                movieHeight = 1;
//            int pLeft = getPaddingLeft();
//            int pRight = getPaddingRight();
//            int pTop = getTop();
//            int pBottom = getBottom();
//            int widthSize;
//            int heightSize;
//
//            movieWidth += pLeft + pRight;
//            movieHeight += pTop + pBottom;
//            movieWidth = Math.max(movieWidth, getSuggestedMinimumWidth());
//            movieHeight = Math.max(movieHeight, getSuggestedMinimumHeight());
//
//            widthSize = resolveSizeAndState(movieWidth, widthMeasureSpec, 0);
//            heightSize = resolveSizeAndState(movieHeight, heightMeasureSpec, 0);
//
//            ratioWidth = (float) widthSize / movieWidth;
//            ratioHeight = (float) heightSize / movieHeight;
//            setMeasuredDimension(widthSize, heightSize);

//            int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
//            float scaleW = (float) movieWidth / (float) maximumWidth;
//            scale = 1f / scaleW;
//            measuredMovieWidth = maximumWidth;
//            measuredMovieHeight = (int) (movieHeight * scale);
//            setMeasuredDimension(measuredMovieWidth, measuredMovieHeight);
        } else {
//            setMeasuredDimension(getSuggestedMinimumWidth(),
//                    getSuggestedMinimumHeight());
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        /*
		 * Calculate left / top for drawing in center
		 */
        left = (getWidth() - measuredMovieWidth) / 2f;
        top = (getHeight() - measuredMovieHeight) / 2f;
        visible = getVisibility() == View.VISIBLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (movie != null) {
            if (!paused) {
                updateAnimationTime();
                drawMovieFrame(canvas);
                invalidateView();
            } else {
                drawMovieFrame(canvas);
            }
        }

    }

    /**
     * Invalidates view only if it is visible.
     * <br>
     * {@link #postInvalidateOnAnimation()} is used for Jelly Bean and higher.
     */
    @SuppressLint("NewApi")
    private void invalidateView() {
        if (visible) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation();
            } else {
                invalidate();
            }
        }
    }

    /**
     * Calculate current animation time
     */
    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();
        if (movieStart == 0) {
            movieStart = now;
        }
        int duration = movie.duration();
        if (duration == 0) {
            duration = DEFAULT_MOVIEW_DURATION;
        }
        currentAnimationTime = (int) ((now - movieStart) % duration);
    }

    /**
     * Draw current GIF frame
     */
    private void drawMovieFrame(Canvas canvas) {
        movie.setTime(currentAnimationTime);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(scale, scale);//float scale = Math.min(ratioWidth, ratioHeight);
        movie.draw(canvas, left / scale, top / scale);
        //movie.draw(canvas, getWidth() - movie.width(), getHeight() - movie.height());
        canvas.restore();
    }

    @SuppressLint("NewApi")
    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        visible = screenState == SCREEN_STATE_ON;
        invalidateView();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        visible = visibility == View.VISIBLE;
        invalidateView();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        visible = visibility == View.VISIBLE;
        invalidateView();
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
