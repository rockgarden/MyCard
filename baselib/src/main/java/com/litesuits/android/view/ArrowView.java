package com.litesuits.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.litesuits.common.R;

/**
 * Created by wangkan on 16/6/14.
 */
public class ArrowView extends ImageView {

    private int mWidth;
    private int mHeight;
    private int mPaintAlpha = 48;

    private int mPressedColor;
    private Paint mPaint;
    private int mShapeType;
    private int mRadius;

    public enum State {
        未处理, 处理中, 完成
    }

    private State state;

    public ArrowView(Context context) {
        super(context);
    }


    public ArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        state = State.未处理;
    }


    public ArrowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        if (isInEditMode()) return;
        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.UIButton);
        mPressedColor = typedArray.getColor(R.styleable.UIButton_color_pressed,
                getResources().getColor(R.color.color_pressed));
        mPaintAlpha = typedArray.getInteger(R.styleable.UIButton_alpha_pressed,
                mPaintAlpha);
        mShapeType = typedArray.getInt(R.styleable.UIButton_shape_type, 1);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.UIButton_radius,
                getResources().getDimensionPixelSize(R.dimen.ui_radius));
        typedArray.recycle();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mPressedColor);
        this.setWillNotDraw(false);
        mPaint.setAlpha(0);
        mPaint.setAntiAlias(true);
        this.setDrawingCacheEnabled(true);
        this.setClickable(true);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) return;
        if (mShapeType == 0) {
            canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2.1038f,
                    mPaint);
        } else {
            RectF rectF = new RectF();
            rectF.set(0, 0, mWidth, mHeight);
            canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPaint.setAlpha(mPaintAlpha);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mPaint.setAlpha(0);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }


    public int getPressedColor() {
        return mPressedColor;
    }


    /**
     * Set the pressed color.
     *
     * @param pressedColor pressed color
     */
    public void setPressedColor(int pressedColor) {
        mPaint.setColor(mPressedColor);
        invalidate();
    }

}
