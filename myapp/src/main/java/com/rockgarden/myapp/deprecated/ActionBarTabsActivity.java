package com.rockgarden.myapp.deprecated;

import android.app.ActionBar;
import android.os.Bundle;

import com.rockgarden.myapp.activity.BaseActivity;

public class ActionBarTabsActivity extends BaseActivity {
    public static final String TAG = ActionBarTabsActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab tab = actionBar
                .newTab()
                .setText("artist")
                .setTabListener(
                        new TabListener<ArtistFragment>(this, "artist",
                                ArtistFragment.class));
        actionBar.addTab(tab);
        tab = actionBar
                .newTab()
                .setText("album")
                .setTabListener(
                        new TabListener<AlbumFragment>(this, "album",
                                AlbumFragment.class));
        actionBar.addTab(tab);

    }

}
