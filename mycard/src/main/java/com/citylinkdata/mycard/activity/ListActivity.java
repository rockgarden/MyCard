package com.citylinkdata.mycard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.citylinkdata.mycard.R;
import com.citylinkdata.mycard.adapter.MyAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * 注解式实现ListView展示数据
 */
public class ListActivity extends Activity {
    @Bind(R.id.lv)
    ListView lv;
    private MyAdapter adapter;
    private ArrayList<String> list;

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
