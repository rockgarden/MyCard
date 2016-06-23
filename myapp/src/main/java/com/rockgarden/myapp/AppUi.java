package com.rockgarden.myapp;

import android.graphics.PorterDuffXfermode;

import static android.graphics.PorterDuff.Mode.CLEAR;

/**
 * 设置APP通用的UI
 */
final class AppUi {
  static final int LIGHT_GREY = 0xFFbababa;
  static final int ROOT_COLOR = 0xFF84a6c5;
  static final int LEAK_COLOR = 0xFFb1554e;

  static final PorterDuffXfermode CLEAR_XFER_MODE = new PorterDuffXfermode(CLEAR);

  private AppUi() {
    throw new AssertionError();
  }
}
