package com.litesuits.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.litesuits.common.utils.AndroidUtil;

public final class MoreDetailsView extends View {

  static final int ROOT_COLOR = 0xFF84a6c5;

  private static final Paint iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

  static {
    iconPaint.setColor(ROOT_COLOR);
  }

  public MoreDetailsView(Context context, AttributeSet attrs) {
    super(context, attrs);

    float strokeSize = AndroidUtil.dpToPixel(2f, getResources());
    iconPaint.setStrokeWidth(strokeSize);
  }

  private boolean opened;

  @Override protected void onDraw(Canvas canvas) {
    int width = getWidth();
    int height = getHeight();
    int halfHeight = height / 2;
    int halfWidth = width / 2;

    if (opened) {
      canvas.drawLine(0, halfHeight, width, halfHeight, iconPaint);
    } else {
      canvas.drawLine(0, halfHeight, width, halfHeight, iconPaint);
      canvas.drawLine(halfWidth, 0, halfWidth, height, iconPaint);
    }
  }

  public void setOpened(boolean opened) {
    if (opened != this.opened) {
      this.opened = opened;
      invalidate();
    }
  }
}
