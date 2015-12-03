package com.rockgarden.myapp.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rockgarden.myapp.uitl.ReMeasureLinearLayoutManager;
import com.rockgarden.myapp.R;
import com.rockgarden.myapp.adpater.RecyclerViewAdapter;

import butterknife.Bind;

public class CardActivity extends BaseLayoutDrawerActivity {
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        toolbar_layout.setTitle(getString(R.string.title_activity_funds_to_card));
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
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(true); // true: with header
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final ReMeasureLinearLayoutManager layoutManager = new ReMeasureLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setNestedScrollingEnabled(false); // Disables scrolling for RecyclerView, CustomLinearLayoutManager used instead of MyLinearLayoutManager
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
                    Log.i("LOG", "Last Item Reached!");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
