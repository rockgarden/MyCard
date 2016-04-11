package com.rockgarden.myapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.litesuits.android.view.GifView;
import com.rockgarden.myapp.R;

import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main Fragment
 */
public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    private static final String STATE_SELECTED_MAIN_CONTENT_VIEW = "selected_MainContentView";
    private int currentSelectedMainView = 0;
    @Bind(R.id.test_GifView)
    GifView testGifViw;

    public static MainFragment newInstance(int selectedMainContentView) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(STATE_SELECTED_MAIN_CONTENT_VIEW, selectedMainContentView);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int nb_view = getArguments().getInt(STATE_SELECTED_MAIN_CONTENT_VIEW);
        int resID = getActivity().getResources().getIdentifier("percent_view_" + String.valueOf(nb_view), "layout", getActivity().getPackageName());
        View rootView = inflater.inflate(resID, container, false);
        ButterKnife.bind(this, rootView); //重要所有UI操作必须在此后执行!!!

        if (nb_view == 4) {
            setGIF();

        }

        return rootView;
    }

    private void setGIF() {
        try {
            InputStream inputStream = getActivity().getAssets().open("test.gif");
            testGifViw.setGifStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO:如何动态控制注入onClick事件
    @OnClick(R.id.onclick_GifView)
    public void onGifClick(View v) {
        GifView gif = (GifView) v;
        gif.setPaused(!gif.isPaused());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_MAIN_CONTENT_VIEW, currentSelectedMainView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

}
