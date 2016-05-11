package com.rockgarden.myapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.adpater.RecyclerViewAdapter_Student;
import com.rockgarden.myapp.model.Student;
import com.rockgarden.recyclerviewlib.DefaultFootItem;
import com.rockgarden.recyclerviewlib.OnLoadMoreListener;
import com.rockgarden.recyclerviewlib.RecyclerViewWithFooter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class StudentsFragment extends Fragment {

    @BindView(R.id.my_recycler_view)
    RecyclerViewWithFooter mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerViewAdapter_Student mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Student> studentList;
    protected Handler handler;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public StudentsFragment() {
    }

    public static StudentsFragment newInstance(int sectionNumber) {
        StudentsFragment fragment = new StudentsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        handler = new Handler();
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_student, container, false);
        ButterKnife.bind(this, rootView);
        setSwipeRefreshLayout();
        if (mRecyclerView instanceof RecyclerViewWithFooter) {
            initRecyclerView(mRecyclerView);
        }
        return rootView;
    }

    /**
     * 准备数据
     */
    private void initData() {
        studentList = new ArrayList<Student>();
        for (int i = 1; i <= 20; i++) {
            studentList.add(new Student("Student " + i, "AndroidStudent" + i + "@gmail.com"));
        }
    }

    private void setSwipeRefreshLayout(){
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addData();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private void initRecyclerView(final RecyclerViewWithFooter mRecyclerView) {
        Context context = mRecyclerView.getContext();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter_Student(studentList, mRecyclerView, context);
        mRecyclerView.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();

        mRecyclerView.setFootItem(new DefaultFootItem());//默认是这种
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addData();
                    }
                }, 2000);
            }
        });
    }

    protected void addData() {
        int start = studentList.size();
        int end = start + 20;
        for (int i = start + 1; i <= end; i++) {
            studentList.add(new Student("Student " + i, "AndroidStudent" + i + "@gmail.com"));
        }
        mAdapter.notifyDataSetChanged(); //=mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public boolean moreData() {
        //add null , so the adapter will check view_type and show progress bar at bottom
        studentList.add(null);
        mAdapter.notifyItemInserted(studentList.size() - 1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // remove progress item
                studentList.remove(studentList.size() - 1);
                mAdapter.notifyItemRemoved(studentList.size());
                //add items one by one
                int start = studentList.size();
                int end = start + 20;
                for (int i = start + 1; i <= end; i++) {
                    studentList.add(new Student("Student " + i, "AndroidStudent" + i + "@gmail.com"));
                    mAdapter.notifyItemInserted(studentList.size());
                }
                // or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
            }
        }, 2000);
        return true;
    }

    /**
     * 重新配置OptionsMenu
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            final int menuItemCount = menu.size();
            for (int i = 0; i < menuItemCount; i++) menu.getItem(i).setVisible(false);
            getActivity().invalidateOptionsMenu();
        }
        inflater.inflate(R.menu.menu_fragment_student, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addMoreAction) {
            mRecyclerView.setLoading();
            addData();
            return true;
        }
        if (id == R.id.endAction) {
            mRecyclerView.setEnd("没有更多数据了");
            return true;
        }
        if (id == R.id.emptyAction) {
            studentList.clear();
            mRecyclerView.setEmpty("没有数据", R.mipmap.ic_launcher);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); //参数为null的话,将所有的Callbacks和Messages全部清除
    }
}
