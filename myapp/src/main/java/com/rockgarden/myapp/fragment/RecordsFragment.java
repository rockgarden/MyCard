package com.rockgarden.myapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.adpater.RecyclerViewAdapter_Record;
import com.rockgarden.myapp.model.Record;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Demo of EndlessRecyclerView
 * A fragment representing a list of Records.
 */
public class RecordsFragment extends Fragment {
    private static final String TAG = RecordsFragment.class.getSimpleName();

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_records)
    RecyclerView recyclerView;
    private List<Record> mRecords;
    NestedScrollingChild mChildHelper;

    RecyclerViewAdapter_Record adapter;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private int page = 1;
    private int numberOfPage = 20;

    public boolean isMoreLoading = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    public static RecordsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(TAG, page);
        RecordsFragment fragment = new RecordsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_records, container, false);
        mChildHelper = recyclerView;
        mRecords = new ArrayList<>();
        ButterKnife.bind(this, rootView);
        initRecyclerView();
        initSwipeContainer();
        loadData(0); //获取准备
        return rootView;
    }

    private void initRecyclerView() {
        adapter = new RecyclerViewAdapter_Record(this.getActivity());
        if (recyclerView instanceof RecyclerView) {
            Context context = recyclerView.getContext();
            // TODO:自定义的ReMeasureLinearLayoutManager不可用
            //final ReMeasureLinearLayoutManager layoutManager = new ReMeasureLinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
            // 设置布局管理器
            if (mColumnCount <= 1) {
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                layoutManager.scrollToPosition(0);
                recyclerView.setLayoutManager(layoutManager);
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), mColumnCount));
                // 实现多行的横向滚动
                /*recyclerView.setLayoutManager(new StaggeredGridLayoutManager(6,
                        StaggeredGridLayoutManager.HORIZONTAL));*/
                // TODO:在onBindViewHolder方法中设置item高度就形成瀑布流
            }
            // 设置adapter
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(true); //也可在布局文件里设android:nestedScrollingEnabled="false"
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            //recyclerView.addItemDecoration(new ItemDecoration_Linear(getActivity(),
            //ItemDecoration_Linear.VERTICAL_LIST));
            //recyclerView.setHasFixedSize(false);
            /*
            recyclerView.setAdapter(new Adapter(BaseItem.ITEMS, mListener));
            */

            // 监听Click
            adapter.addOnItemClickListener(new RecyclerViewAdapter_Record.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getActivity(), position + " was clicked!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    Toast.makeText(getActivity(), position + " long click", Toast.LENGTH_SHORT).show();
                    adapter.removeData(position);
                    recyclerView.scrollToPosition(0);
                }
            });
            // 监听Scroll
            if (layoutManager instanceof LinearLayoutManager) {
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int visibleThreshold = 0;
                        //若取layoutManager的getChildCount
                        int visibleItemCount = recyclerView.getChildCount();
                        int childCountOfLayoutManager = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                        if (!isMoreLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                            loadMore(page);
                            isMoreLoading = true;
                        }
                    }
                });
            }
            // 监听Touch
            recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public void onTouchEvent(RecyclerView recycler, MotionEvent event) {
                    // Handle on touch events here
                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                }

                @Override
                public boolean onInterceptTouchEvent(RecyclerView recycler, MotionEvent event) {
                    return false;
                }
            });
        }
    }

    /**
     * 配置SwipeRefreshLayout
     */
    private void initSwipeContainer() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            /**
             * fetchDataAsync
             */
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        loadData(1);

                    }
                }, 2000);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * 加载数据
     *
     * @param page
     */
    public void loadData(int page) {
        mRecords.clear();
        mRecords = Record.createRecordList(20);
        adapter.addAll(mRecords);
        // TODO:refresh data example
        // Send the network request to fetch the updated data
        /*
        client.getHomeTimeline(0, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
                // Remember to CLEAR OUT old items before appending in the new ones
                adapter.clear();
                // ...the data has come back, add new items to your adapter...
                adapter.addAll(...);
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeRefreshLayout.setRefreshing(false);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
        */
    }

    /**
     * 加载更多数据
     *
     * @param position
     * @return
     */
    public boolean loadMore(int position) {
        adapter.setProgressMore(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecords.clear();
                adapter.setProgressMore(false);
                int start = adapter.getItemCount();
                int end = start + numberOfPage;
                for (int i = start + 1; i <= end; i++) {
                    mRecords.add(new Record("Records" + i, false));
                }
                adapter.addItemMore(mRecords);
                isMoreLoading = false;
            }
        }, 2000);
        return true;
    }


    // 也可在adapter中实现
    public void updateItems() {
        // record this value before making any changes to the existing list
        int curSize = adapter.getItemCount();
        List<Record> newItems = Record.createRecordList(20);
        // update the existing list
        mRecords.addAll(newItems);
        adapter.notifyItemRangeInserted(curSize, newItems.size());
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecordsFragment() {
    }

}
