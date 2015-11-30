package com.citylinkdata.mycard.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.citylinkdata.mycard.fragment.PayFragment;
import com.citylinkdata.mycard.fragment.RecordsFragment;

/**
 * Created by wk on 15/11/25.
 */
public class PagerAdapter_Fragment extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    private String tabTitles[] = new String[] { "Tab1", "Tab2" };
    int PAGE_COUNT = 2;
    private Context context;

    public PagerAdapter_Fragment(FragmentManager fm, CharSequence mTitles[],
                                 int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.PAGE_COUNT = mNumbOfTabsumb;
    }

    public PagerAdapter_Fragment(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PayFragment.newInstance(position + 1);
            case 1:
                return RecordsFragment.newInstance(position+1);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
