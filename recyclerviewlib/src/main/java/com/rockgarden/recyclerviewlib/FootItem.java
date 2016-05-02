package com.rockgarden.recyclerviewlib;

import android.view.View;
import android.view.ViewGroup;

/**
 * Footer item
 *
 */
public abstract class FootItem {

    public CharSequence loadingText;
    public CharSequence endText;
    public CharSequence pullToLoadText;

    public abstract View onCreateView(ViewGroup parent);

    public abstract void onBindData(View view, int state);

}
