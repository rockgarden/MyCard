package com.rockgarden.myapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.adpater.RecyclerViewAdapter_Complex;
import com.rockgarden.myapp.model.Item;
import com.rockgarden.myapp.uitl.EndlessScrollListener_RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link FragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {
    @BindView(R.id.section_label)
    TextView textView;
    @BindView(R.id.Empty_label)
    TextView tvEmptyView;
    // TODO: Customize parameter argument names
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private FragmentInteractionListener fragmentInteractionListener;
    private RecyclerViewAdapter_Complex.ImageItemListener imageItemListener;
    RecyclerViewAdapter_Complex adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item, container, false);
        ButterKnife.bind(this, rootView);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        adapter = new RecyclerViewAdapter_Complex(Item.ITEMS, fragmentInteractionListener, imageItemListener);
        // RecyclerView若嵌在多个NestedScrollingParent中会引起滑动不流畅
        if (rootView instanceof RecyclerView) {
            Context context = rootView.getContext();
            RecyclerView recyclerView = (RecyclerView) rootView;
            if (mColumnCount <= 1) {
                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linearLayoutManager);
                /*TODO:用fab来触发behavior的设定
                final BottomSheetBehavior behavior = BottomSheetBehavior.from(recyclerView);
                getActivity().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        } else {
                            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    }
                });
                */
                recyclerView.addOnScrollListener(new EndlessScrollListener_RecyclerView(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        customLoadMoreDataFromApi(page);
                    }

                    @Override
                    public void onScrolled(RecyclerView view, int dx, int dy) {
                        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    }
                });
            } else {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, mColumnCount);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.addOnScrollListener(new EndlessScrollListener_RecyclerView(gridLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        // Triggered only when new data needs to be appended to the list
                        // Add whatever code is needed to append new items to the bottom of the list
                        customLoadMoreDataFromApi(page);
                    }
                });

            }
            recyclerView.setAdapter(adapter);

            if (Item.ITEMS.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvEmptyView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmptyView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
            }
        }
        return rootView;
    }

    // Append more data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int offset) {
        // TODO:实现moreItems
        //Item.ITEMS.addAll(moreItems);
        // For efficiency purposes, notify the adapter of only the elements that got changed
        // curSize will equal to the index of the first element inserted because the list is 0-indexed
        int curSize = adapter.getItemCount();
        adapter.notifyItemRangeInserted(curSize, Item.ITEMS.size() - 1);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            fragmentInteractionListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInteractionListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface FragmentInteractionListener {
        // TODO: Update argument type and title
        void onFragmentInteraction(Object item);
    }
}
