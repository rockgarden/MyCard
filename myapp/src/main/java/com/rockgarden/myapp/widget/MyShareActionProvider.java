package com.rockgarden.myapp.widget;

import android.content.Context;
import android.support.v7.widget.ShareActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.rockgarden.myapp.R;

/**
 * Created by rockgarden on 16/4/16.
 */
public class MyShareActionProvider extends ShareActionProvider {

    public MyShareActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();
        subMenu.add("sub item 1").setIcon(R.mipmap.ic_launcher)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
        subMenu.add("sub item 2").setIcon(R.mipmap.ic_launcher)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }

}
