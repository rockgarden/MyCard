package com.citylinkdata.mycard.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.citylinkdata.mycard.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wk on 15/11/25.
 */
public class PayFragment extends Fragment {
    private static final String TAG = PayFragment.class.getSimpleName();

    @Bind(R.id.tvSample)
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
        View rootView = inflater.inflate(R.layout.fragment_pay, container, false);
        ButterKnife.bind(this,rootView);
        tvSample.setText("Fragment #" + mPage);
        return rootView;
    }
}
