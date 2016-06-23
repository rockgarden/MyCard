package com.litesuits.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.PorterDuff.Mode.CLEAR;

import com.litesuits.common.utils.AndroidUtil;

public final class LeakConnectorView extends View {

  private static final Paint iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private static final Paint rootPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private static final Paint leakPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private static final Paint clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

  static final int LIGHT_GREY = 0xFFbababa;
  static final int ROOT_COLOR = 0xFF84a6c5;
  static final int LEAK_COLOR = 0xFFb1554e;

  static final PorterDuffXfermode CLEAR_XFER_MODE = new PorterDuffXfermode(CLEAR);

  static {
    iconPaint.setColor(LIGHT_GREY);
    rootPaint.setColor(ROOT_COLOR);
    leakPaint.setColor(LEAK_COLOR);
    clearPaint.setColor(Color.TRANSPARENT);
    clearPaint.setXfermode(CLEAR_XFER_MODE);
  }

  public enum Type {
    START, NODE, END
  }

  private Type type;
  private Bitmap cache;

  public LeakConnectorView(Context context, AttributeSet attrs) {
    super(context, attrs);
    type = Type.NODE;
  }

  @SuppressWarnings("SuspiciousNameCombination") @Override protected void onDraw(Canvas canvas) {
    int width = getWidth();
    int height = getHeight();

    if (cache != null && (cache.getWidth() != width || cache.getHeight() != height)) {
      cache.recycle();
      cache = null;
    }

    if (cache == null) {
      cache = Bitmap.createBitmap(width, height, ARGB_8888);

      Canvas cacheCanvas = new Canvas(cache);

      float halfWidth = width / 2f;
      float halfHeight = height / 2f;
      float thirdWidth = width / 3f;

      float strokeSize = AndroidUtil.dpToPixel(4f, getResources());

      iconPaint.setStrokeWidth(strokeSize);
      rootPaint.setStrokeWidth(strokeSize);

      switch (type) {
        case NODE:
          cacheCanvas.drawLine(halfWidth, 0, halfWidth, height, iconPaint);
          cacheCanvas.drawCircle(halfWidth, halfHeight, halfWidth, iconPaint);
          cacheCanvas.drawCircle(halfWidth, halfHeight, thirdWidth, clearPaint);
          break;
        case START:
          float radiusClear = halfWidth - strokeSize / 2f;
          cacheCanvas.drawRect(0, 0, width, radiusClear, rootPaint);
          cacheCanvas.drawCircle(0, radiusClear, radiusClear, clearPaint);
          cacheCanvas.drawCircle(width, radiusClear, radiusClear, clearPaint);
          cacheCanvas.drawLine(halfWidth, 0, halfWidth, halfHeight, rootPaint);
          cacheCanvas.drawLine(halfWidth, halfHeight, halfWidth, height, iconPaint);
          cacheCanvas.drawCircle(halfWidth, halfHeight, halfWidth, iconPaint);
          cacheCanvas.drawCircle(halfWidth, halfHeight, thirdWidth, clearPaint);
          break;
        default:
          cacheCanvas.drawLine(halfWidth, 0, halfWidth, halfHeight, iconPaint);
          cacheCanvas.drawCircle(halfWidth, halfHeight, thirdWidth, leakPaint);
          break;
      }
    }
    canvas.drawBitmap(cache, 0, 0, null);
  }

  public void setType(Type type) {
    if (type != this.type) {
      this.type = type;
      if (cache != null) {
        cache.recycle();
        cache = null;
      }
      invalidate();
    }
  }
}
