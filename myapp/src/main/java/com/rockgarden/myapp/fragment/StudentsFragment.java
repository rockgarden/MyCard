package com.rockgarden.myapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.adpater.RecyclerViewAdapter_Student;
import com.rockgarden.myapp.model.Student;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class StudentsFragment extends Fragment {
    @Bind(R.id.section_label)
    TextView textView;
    @Bind(R.id.Empty_label)
    TextView tvEmptyView;
    @Bind(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_student, container, false);
        ButterKnife.bind(this, rootView);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        handler = new Handler();
        initData();
        if (mRecyclerView instanceof RecyclerView) {
            initRecyclerView(mRecyclerView);
        }
        return rootView;
    }

    private void initData() {
        studentList = new ArrayList<Student>();
        for (int i = 1; i <= 20; i++) {
            studentList.add(new Student("Student " + i, "AndroidStudent" + i + "@gmail.com"));
        }
    }

    private void initRecyclerView(RecyclerView mRecyclerView) {
        Context context = mRecyclerView.getContext();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter_Student(studentList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();

        if (studentList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }

        mAdapter.setOnLoadMoreListener(new RecyclerViewAdapter_Student.OnLoadMoreListener() {

            @Override
            public boolean onLoadMore(int position) {
                return true;
            }

            @Override
            public boolean onLoadMore() {
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
                        mAdapter.setLoaded();
                        // or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);
                return true;
            }
        });
    }

}
