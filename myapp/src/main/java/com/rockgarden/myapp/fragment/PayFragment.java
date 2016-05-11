package com.rockgarden.myapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rockgarden.myapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wk on 15/11/25.
 */
public class PayFragment extends Fragment {
    private static final String TAG = PayFragment.class.getSimpleName();

    @BindView(R.id.tvSample)
    TextView tvSample;
    private int mPage;

    public static PayFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(TAG, page);
        PayFragment fragment = new PayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_no_scroll, container, false);
        ButterKnife.bind(this,rootView);
        tvSample.setText("Fragment #" + mPage);
        return rootView;
    }
}
