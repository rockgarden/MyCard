package com.rockgarden.myapp.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.rockgarden.myapp.R;

import java.util.ArrayList;

/**
 * 演示MapView的基本用法
 */
public class BaseMapDemo extends Activity {

    @SuppressWarnings("unused")
    private static final String LTAG = BaseMapDemo.class.getSimpleName();
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private InfoWindow mInfoWindow;
    private SeekBar alphaSeekBar = null;
    private CheckBox animationBox = null;
    private View markerView;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA, bdB, bdC, bdD, bd, bd1, bdGround;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // 介绍如何使用个性化地图，需在MapView 构造前设置个性化地图文件路径
        // 注: 设置个性化地图，将会影响所有地图实例。
        // MapView.setCustomMapStylePath("个性化地图config绝对路径");
        super.onCreate(savedInstanceState);

        // 初始化全局 bitmap 信息，不用时及时 recycle
        Intent intent = getIntent();
        if (intent.hasExtra("x") && intent.hasExtra("y")) {
            // 当用intent参数时，设置中心点为指定点
            Bundle b = intent.getExtras();
            LatLng p = new LatLng(b.getDouble("x"), b.getDouble("y"));
            mMapView = new MapView(this,
                    new BaiduMapOptions().mapStatus(new MapStatus.Builder().target(p).zoom(8).build()));
        } else {
            LatLng p = new LatLng(30.23300678, 120.233899);
            mMapView = new MapView(this, new BaiduMapOptions().mapStatus(new MapStatus.Builder().target(p).zoom(10).build()));
        }
        initUI();
        mBaiduMap = mMapView.getMap();

        markerView = this.getLayoutInflater().inflate(R.layout.marker_view, null);
        bdA = BitmapDescriptorFactory
                .fromView(markerView);
        bdB = BitmapDescriptorFactory
                .fromView(markerView);
        bdC = BitmapDescriptorFactory
                .fromView(markerView);
        bdD = BitmapDescriptorFactory
                .fromView(markerView);
        bd = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_heart_red_mini);
        bd1 = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_heart_blue_mini);
        bdGround = BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_launcher);

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(13.0f);
        mBaiduMap.setMapStatus(msu);
        initOverlay();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.popup);
                InfoWindow.OnInfoWindowClickListener listener = null;
                if (marker == mMarkerA || marker == mMarkerD) {
                    button.setText("更改位置");
                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {
                            LatLng ll = marker.getPosition();
                            LatLng llNew = new LatLng(ll.latitude + 0.005,
                                    ll.longitude + 0.005);
                            marker.setPosition(llNew);
                            mBaiduMap.hideInfoWindow();
                        }
                    };
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                } else if (marker == mMarkerB) {
                    button.setText("更改图标");
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            marker.setIcon(bd);
                            mBaiduMap.hideInfoWindow();
                        }
                    });
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(button, ll, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                } else if (marker == mMarkerC) {
                    button.setText("删除");
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            marker.remove();
                            mBaiduMap.hideInfoWindow();
                        }
                    });
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(button, ll, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });


    }

    private void initUI() {
        /* 纯代码生成UI:
        可通过getDecorView()获取顶级View,也可直接setContentView(),本质都是对DecorView进行操作
        ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView()*/
        // 生成rootView
        RelativeLayout rootLayout = new RelativeLayout(this);
        // 根布局参数
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT); //相当于直接加Layout不带layoutParams
        setContentView(rootLayout, layoutParams);
        rootLayout.addView(mMapView);

        animationBox = new CheckBox(this);
        animationBox.setText("animation");
        animationBox.setId(R.id.animationBox);
        RelativeLayout.LayoutParams animationBoxRLP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        animationBoxRLP.addRule(RelativeLayout.ALIGN_PARENT_TOP, -1);
        animationBoxRLP.addRule(RelativeLayout.ALIGN_PARENT_START, -1);
        animationBoxRLP.topMargin = 15;
        animationBoxRLP.rightMargin =8;
        rootLayout.addView(animationBox, animationBoxRLP);

        alphaSeekBar = new SeekBar(this);
        alphaSeekBar.setOnSeekBarChangeListener(new SeekBarListener());
        alphaSeekBar.setId(R.id.alphaSeekBar);
        RelativeLayout.LayoutParams alphaSeekBarRLP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alphaSeekBarRLP.addRule(RelativeLayout.ALIGN_PARENT_TOP, -1);
        alphaSeekBarRLP.addRule(RelativeLayout.ALIGN_PARENT_END, -1);
        alphaSeekBarRLP.addRule(RelativeLayout.RIGHT_OF, R.id.animationBox);
        alphaSeekBarRLP.topMargin = 15;
        rootLayout.addView(alphaSeekBar, alphaSeekBarRLP);

        View controlPanel = this.getLayoutInflater().inflate(R.layout.view_map_control_panel, null);;
        RelativeLayout.LayoutParams controlPanelRLP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        controlPanelRLP.addRule(RelativeLayout.BELOW, R.id.animationBox);
        rootLayout.addView(controlPanel, controlPanelRLP);
    }

    public void initOverlay() {
        // add marker overlay
        LatLng llA = new LatLng(30.21300678, 120.183899);
        LatLng llB = new LatLng(30.22300678, 120.203899);
        LatLng llC = new LatLng(30.23300678, 120.223899);
        LatLng llD = new LatLng(30.24300678, 120.243899);

        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA)
                .zIndex(9).draggable(true);
        if (animationBox.isChecked()) {
            // 掉下动画
            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
        }
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        MarkerOptions ooB = new MarkerOptions().position(llB).icon(bdB)
                .zIndex(5);
        if (animationBox.isChecked()) {
            // 掉下动画
            ooB.animateType(MarkerOptions.MarkerAnimateType.drop);
        }
        mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
        MarkerOptions ooC = new MarkerOptions().position(llC).icon(bdC)
                .perspective(false).anchor(0.5f, 0.5f).rotate(30).zIndex(7);
        if (animationBox.isChecked()) {
            // 生长动画
            ooC.animateType(MarkerOptions.MarkerAnimateType.grow);
        }
        mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));
        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(bd);
        giflist.add(bd1);
        MarkerOptions ooD = new MarkerOptions().position(llD).icons(giflist)
                .zIndex(0).period(100);
        if (animationBox.isChecked()) {
            // 生长动画
            ooD.animateType(MarkerOptions.MarkerAnimateType.grow);
        }
        mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));

        // add ground overlay
        LatLng southwest = new LatLng(30.1900067, 120.213899);
        LatLng northeast = new LatLng(30.2100067, 120.253899);
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
                .include(southwest).build();

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bdGround).transparency(0.8f);
        mBaiduMap.addOverlay(ooGround);

        MapStatusUpdate u = MapStatusUpdateFactory
                .newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(u);

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(
                        BaseMapDemo.this,
                        "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                                + marker.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    /**
     * 清除所有Overlay
     *
     * @param view
     */
    public void clearOverlay(View view) {
        mBaiduMap.clear();
        if (mMarkerA != null) {
            mMarkerA = null;
            mMarkerB = null;
            mMarkerC = null;
            mMarkerD = null;
        }
    }

    /**
     * 重新添加Overlay
     *
     * @param view
     */
    public void resetOverlay(View view) {
        clearOverlay(null);
        initOverlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        mMapView.onDestroy();
        if (bdA != null) {
            bdA.recycle();
            bdB.recycle();
            bdC.recycle();
            bdD.recycle();
            bd.recycle();
            bd1.recycle();
            bdGround.recycle();
        }
    }

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            float alpha = ((float) seekBar.getProgress()) / 10;
            if (mMarkerA != null) {
                mMarkerA.setAlpha(alpha);
            }
            if (mMarkerB != null) {
                mMarkerB.setAlpha(alpha);
            }
            if (mMarkerC != null) {
                mMarkerC.setAlpha(alpha);
            }
            if (mMarkerD != null) {
                mMarkerD.setAlpha(alpha);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

    }
}
