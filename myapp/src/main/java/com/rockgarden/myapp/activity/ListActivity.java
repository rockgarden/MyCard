package com.rockgarden.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.adpater.BaseAdapter_List;
import com.rockgarden.myapp.uitl.EndlessScrollListener_AbsListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * 注解式实现ListView展示数据
 */
public class ListActivity extends Activity {

    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    @BindView(R.id.lv)
    ListView lv;
    private BaseAdapter_List adapter;
    private ArrayList<String> list;

    public static void startListFromLocation(int[] startingLocation, Activity startingActivity) {
        Intent intent = new Intent(startingActivity, ListActivity.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        list = new ArrayList<String>();
        list.add("测试一");
        list.add("测试2");
        list.add("测试3");
        list.add("测试4");
        list.add("测试5");
        list.add("测试10");
        list.add("测试11");
        list.add("测试12");
        list.add("测试13");
        list.add("测试14");
        adapter = new BaseAdapter_List(this, list);
        lv.setAdapter(adapter);
        lv.setOnScrollListener(new EndlessScrollListener_AbsListView() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
    }

    @OnItemClick(R.id.lv)
    public void onMyItemClick(int position) {
        Toast.makeText(this, position + "", Toast.LENGTH_SHORT).show();
    }

}
