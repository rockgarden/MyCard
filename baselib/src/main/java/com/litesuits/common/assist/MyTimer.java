package com.litesuits.common.assist;

import android.os.Handler;
import android.os.Looper;

import com.litesuits.android.Log;

import java.util.Calendar;

/**
 * Created by rockgarden on 15/11/9.
 */
public class MyTimer {

    private static final String TAG = "MyTimer";
    public static final int DEFAULT_INTERVAL = 1000; // 1s

    private Handler mTimerHandler = null;
    private int mTimerIntervalInMilliseconds = DEFAULT_INTERVAL;
    private OnTickListener mListener = null;
    private Boolean mIsTimerRunning = false;

    // Must create Timer instance via Builder
    private MyTimer() {
    }

    private Runnable mTimerRunnable = new Runnable() {

        @Override
        public void run() {
            if (mTimerHandler == null) {
                NullTimerHandlerException.throwException();
                return;
            }

            if (mListener != null) {
                mListener.onTick(Calendar.getInstance().getTimeInMillis());
            }

            mTimerHandler.postDelayed(mTimerRunnable, mTimerIntervalInMilliseconds);
        }
    };

    /**
     * Start the timer
     */
    public void start() {
        synchronized (mIsTimerRunning) {
            if (mIsTimerRunning) {
                TriggerTimerException.throwTriggerStartingTimerException();
                return;
            } else if (mTimerHandler == null){
                NullTimerHandlerException.throwException();
                return;
            } else {
                mIsTimerRunning = true;
                mTimerHandler.post(mTimerRunnable);
                Log.d(TAG, "Timer is started");
            }
        }
    }

    /**
     * Stop the timer
     */
    public void stop() {
        synchronized (mIsTimerRunning) {
            if (!mIsTimerRunning) {
                TriggerTimerException.throwTriggerStoppingTimerException();
                return;
            } else if (mTimerHandler == null){
                NullTimerHandlerException.throwException();
                return;
            } else {
                mIsTimerRunning = false;
                mTimerHandler.removeCallbacks(mTimerRunnable);
                Log.d(TAG, "Timer is stopped");
            }
        }
    }

    /*
    mTimer = new AndroidTimer.Builder()
            .listener(this)
            .looper(Looper.getMainLooper())
            .timerIntervalInSeconds(1)
            .build();
    */

    public static class Builder {
        private int mTimerIntervalInMilliseconds = DEFAULT_INTERVAL;
        private Looper mLooper = null;
        private OnTickListener mListener;

        /**
         * Builder Timer instance basing on properties
         */
        public MyTimer build() {
            MyTimer timer = new MyTimer();
            if (mLooper == null) {
                timer.mTimerHandler = new Handler();
            } else {
                timer.mTimerHandler = new Handler(mLooper);
            }
            timer.mListener = mListener;
            timer.mTimerIntervalInMilliseconds = mTimerIntervalInMilliseconds;
            return timer;
        }

        /**
         * Set listener to builder
         * @param listener the listener will observer onTick event
         * @return current building Builder
         */
        public Builder listener(OnTickListener listener) {
            mListener = listener;
            return this;
        }

        /**
         * Set timer interval to builder
         * @param timerIntervalInMilliseconds timer interval in milliseconds
         * @return current building Builder
         */
        public Builder timerIntervalInMilliseconds(int timerIntervalInMilliseconds) {
            mTimerIntervalInMilliseconds = timerIntervalInMilliseconds;
            return this;
        }

        /**
         * Set timer interval to builder
         * @param timerIntevalInSeconds timer interval in seconds
         * @return current building Builder
         */
        public Builder timerIntervalInSeconds(int timerIntevalInSeconds) {
            mTimerIntervalInMilliseconds = timerIntevalInSeconds * 1000;
            return this;
        }

        /**
         * Set the looper to schedule the timer, if it is null, the default looper will be used
         * @param looper a Looper
         * @return current building Builder
         */
        public Builder looper(Looper looper) {
            mLooper = looper;
            return this;
        }
    }

    public interface OnTickListener {
        public void onTick(long timestampInMilliseconds);
    }

}

class NullTimerHandlerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private NullTimerHandlerException() {
        super("Haven't set handler to Timer yet");
    }

    public static void throwException() {
        throw new NullTimerHandlerException();
    }
}

class TriggerTimerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private TriggerTimerException(String message) {
        super(message);
    }

    public static void throwTriggerStartingTimerException() {
        throw new TriggerTimerException("Timer is started already");
    }

    public static void throwTriggerStoppingTimerException() {
        throw new TriggerTimerException("Timer is stopped already or haven't started yet");
    }
}