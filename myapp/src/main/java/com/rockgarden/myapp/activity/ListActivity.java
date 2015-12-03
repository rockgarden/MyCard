package com.rockgarden.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.adpater.MyAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * 注解式实现ListView展示数据
 */
public class ListActivity extends Activity {

    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    @Bind(R.id.lv)
    ListView lv;
    private MyAdapter adapter;
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
        adapter = new MyAdapter(this, list);
        lv.setAdapter(adapter);
    }

    @OnItemClick(R.id.lv)
    public void onMyItemClick(int position) {
        Toast.makeText(this, position + "", Toast.LENGTH_SHORT).show();
    }

}
