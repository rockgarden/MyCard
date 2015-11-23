package com.citylinkdata.mycard.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by rockgarden on 15/11/21.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> viewList;
    private Context context;

    public ViewPagerAdapter(List<View> viewList, Context context) {
        super();
        this.viewList = viewList;
        this.context = context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position),0);
        return viewList.get(position);
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
