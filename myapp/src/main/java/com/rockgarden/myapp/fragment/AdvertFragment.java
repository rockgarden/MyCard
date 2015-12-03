package com.rockgarden.myapp.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.litesuits.android.log.Log;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 广告
 * Activities that contain this fragment must implement the
 * {@link AdvertFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdvertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdvertFragment extends Fragment {
    private static final String TAG = AdvertFragment.class.getSimpleName();

    private String advertLocalUrl = "assets://images/ad2.jpg";
    private String advertUrl = "http://img.zcool.cn/community/05ef61554ace6300000115a891c243.jpg";
    private Timer timer;
    private int counter = 5;
    private TimerTask timerTask = null;
    private ImageLoader loader;
    private ImageView advertImageView;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AdvertFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdvertFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdvertFragment newInstance(String param1, String param2) {
        AdvertFragment fragment = new AdvertFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View rootView=inflater.inflate(R.layout.fragment_loading, container, false);
        advertImageView = new ImageView(getActivity());
        advertImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View rootView = advertImageView;
        loadImage();
        return rootView;
    }

    private void loadImage() {
        loader = ImageLoader.getInstance();
        loader.displayImage(
                advertUrl,
                advertImageView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                        Log.i(TAG, "advertLoadingStarted");
                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1,
                                                FailReason arg2) {
                        Log.e(TAG, "advertLoadingFailed");
                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1,
                                                  Bitmap arg2) {
                        Log.i(TAG, "advertLoadingComplete");
                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        Log.i(TAG, "advertLoadingCancelled");
                    }
                });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    startTimer();
                    break;
                case -2:
                    stopTime();
                    break;
                default:
                    startTimer();
            }
        }

    };

    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                counter--;
                Message message = mHandler.obtainMessage();
                message.arg1 = counter;
                mHandler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 1000);
    }

    public void stopTime() {
        timer.cancel();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
