package com.rockgarden.myapp.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.adpater.RecyclerViewAdapter_Card;

import butterknife.Bind;

public class CardActivity extends BaseLayoutDrawerActivity {
    public static final String TAG = CardActivity.class.getName();
    public static final String ACTION=".activity.CardActivity";
    @Bind(R.id.collapse_toolbar_layout)
    CollapsingToolbarLayout collapseToolbarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        collapseToolbarLayout.setTitle(getString(R.string.title_activity_card));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initCardRecyclerView();
    }

    private void initCardRecyclerView() {
        // RVAdapter adapter = new RVAdapter();
        RecyclerViewAdapter_Card recyclerViewAdapterCard = new RecyclerViewAdapter_Card(true); // true: with header
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //final ReMeasureLinearLayoutManager layoutManager = new ReMeasureLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapterCard);
        recyclerView.setNestedScrollingEnabled(true);
        // recyclerView.setHasFixedSize(false);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPos = layoutManager.findLastVisibleItemPosition();
                Log.i("getChildCount", String.valueOf(visibleItemCount));
                Log.i("getItemCount", String.valueOf(totalItemCount));
                Log.i("lastVisibleItemPos", String.valueOf(lastVisibleItemPos));
                if ((visibleItemCount + lastVisibleItemPos) >= totalItemCount) {
                    Log.i(TAG, "Last Item Reached!");
                }
            }
        });
    }

}
