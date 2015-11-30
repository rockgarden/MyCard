package com.citylinkdata.mycard.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.citylinkdata.mycard.R;
import com.citylinkdata.mycard.adapter.RecordAdapter;
import com.citylinkdata.mycard.model.Record;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Records.
 */
public class RecordsFragment extends Fragment {
    private static final String TAG = RecordsFragment.class.getSimpleName();
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @Bind(R.id.rv_records)
    RecyclerView recyclerView;

    RecordAdapter adapter;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

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
        ButterKnife.bind(this, rootView);
        initSwipeContainer();
        initRecyclerView();
        return rootView;
    }

    private void initRecyclerView() {
        List<Record> mRecords = Record.createRecordList(10);
        adapter = new RecordAdapter(mRecords, this.getActivity());
        if (recyclerView instanceof RecyclerView) {
            Context context = recyclerView.getContext();
            recyclerView.setHasFixedSize(true);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(adapter);
            /*
            recyclerView.setAdapter(new ItemRecyclerViewAdapter(BaseItem.ITEMS, mListener));
            */
            adapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getActivity(), position + " was clicked!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    Toast.makeText(getActivity(), position + " long click", Toast.LENGTH_SHORT).show();
                    adapter.removeData(position);
                }
            });

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

    private void initSwipeContainer() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDataAsync(1);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void fetchDataAsync(int page) {
        // TODO get data then refresh view
        adapter.clear();
        final List<Record> mRecords = Record.createRecordList(10);
        adapter.addAll(mRecords);
        swipeContainer.setRefreshing(false);
    }


//    private OnListFragmentInteractionListener mListener;
//
//    /**
//     * Mandatory empty constructor for the fragment manager to instantiate the
//     * fragment (e.g. upon screen orientation changes).
//     */
//    public RecordsFragment() {
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnListFragmentInteractionListener {
//        void onListFragmentInteraction(TestItem item);
//    }
}
