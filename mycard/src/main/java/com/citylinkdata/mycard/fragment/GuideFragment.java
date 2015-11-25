package com.citylinkdata.mycard.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.citylinkdata.mycard.R;
import com.citylinkdata.mycard.adapter.PagerAdapter_View;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能介绍
 */
public class GuideFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private View rootView;
    private ViewPager vp;
    private PagerAdapter_View vpAdapter;

    private List<View> viewList;
    
    private int numberPagers = 3;

    private ImageView indicator;
    private ImageView guideIV1,guideIV2,guideIV3;
    private ImageView[] indicators;

    private ViewGroup viewPagerIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_guide, container, false);
        initViewPager();
        initViewPagerIndicator();
        return rootView;
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        guideIV1 = new ImageView(getActivity());
        guideIV2 = new ImageView(getActivity());
        guideIV3 = new ImageView(getActivity());

        guideIV1.setImageDrawable(getResources().getDrawable(R.drawable.guide1));
        guideIV2.setImageDrawable(getResources().getDrawable(R.drawable.guide2));
        guideIV3.setImageDrawable(getResources().getDrawable(R.drawable.guide3));

        viewList = new ArrayList<View>();
        viewList.add(guideIV1);
        viewList.add(guideIV2);
        viewList.add(guideIV3);

        vpAdapter = new PagerAdapter_View(viewList, getActivity());
        vp = (ViewPager) rootView.findViewById(R.id.vpGuide);
        vp.setAdapter(vpAdapter);
        vp.addOnPageChangeListener(this);
    }

    /**
     * 初始化ViewPager指示器
     */
    private void initViewPagerIndicator() {
        viewPagerIndicator =(ViewGroup) rootView.findViewById(R.id.viewPageDots); //实例化示意图例
        indicators =new ImageView[viewList.size()];
        for(int i=0;i<numberPagers;i++){
            indicator = new ImageView(getActivity());
            indicator.setLayoutParams(new ViewGroup.LayoutParams(60, 5));
            indicators[i] = indicator;
            if(i==0){
                indicators[i].setBackgroundResource(R.color.colorPrimaryText);
            }else{
                indicators[i].setBackgroundResource(R.color.white_overlay);
            }
            viewPagerIndicator.addView(indicators[i]);
        }
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int arg0) {
        for (int i = 0; i < numberPagers; i++) {
            if (arg0 == i) {
                indicators[i].setBackgroundResource(R.color.colorPrimaryText);
            } else {
                indicators[i].setBackgroundResource(R.color.white_overlay);;
            }
        }
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
