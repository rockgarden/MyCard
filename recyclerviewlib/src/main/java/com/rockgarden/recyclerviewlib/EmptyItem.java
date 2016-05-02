package com.rockgarden.recyclerviewlib;

import android.view.View;
import android.view.ViewGroup;

/**
 * 没数据时候的默认View
 *
 */
public abstract class EmptyItem {

    CharSequence mEmptyText;
    int mEmptyIconRes = -1;

    abstract View onCreateView(ViewGroup parent);

    abstract void onBindData(View view);
}
