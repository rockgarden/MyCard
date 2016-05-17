package com.litesuits.android.async;

import android.os.Handler;
import android.os.Looper;

import com.litesuits.android.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 异步执行
 * TODO:说明实现
 * @author MaTianyu
 */
public class AsyncExecutor {
    private static final String TAG = AsyncExecutor.class.getSimpleName();
    public static Handler handler = new Handler(Looper.getMainLooper());
    private static ExecutorService threadPool;

    public AsyncExecutor() {
        this(null);
    }

    public AsyncExecutor(ExecutorService threadPool) {
        if (AsyncExecutor.threadPool != null) {
            shutdownNow();
        }
        if (threadPool == null) {
            AsyncExecutor.threadPool = Executors.newCachedThreadPool();
        } else {
            AsyncExecutor.threadPool = threadPool;
        }
    }

    public static synchronized void shutdownNow() {
        if (threadPool != null && !threadPool.isShutdown()) threadPool.shutdownNow();
        threadPool = null;
    }

    /**
     * 将任务投入线程池执行
     * 泛型方法
     * @param worker
     * @return
     *
     * T 表示一般意义上的数据类型;
     */
    public <T> FutureTask<T> execute(final Worker<T> worker) {
        // 实例化泛型接口 T = object the result type of method
        Callable<T> call = new Callable<T>() {
            @Override
            public T call() throws Exception {
                return postResult(worker, worker.doInBackground());
            }
        };
        // 实例化泛型类 T = object the callable task
        FutureTask<T> task = new FutureTask<T>(call) {
            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException e) {
                    Log.e(TAG, e);
                    worker.abort();
                    postCancel(worker);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("An error occured while executing doInBackground()", e.getCause());
                } catch (CancellationException e) {
                    worker.abort();
                    postCancel(worker);
                    Log.e(TAG, e);
                    e.printStackTrace();
                }
            }
        };
        threadPool.execute(task);
        return task;
    }

    /**
     * 将子线程结果传递到UI线程
     *
     * @param worker
     * @param result
     * @return
     */
    private <T> T postResult(final Worker<T> worker, final T result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                worker.onPostExecute(result);
            }
        });
        return result;
    }

    /**
     * 将子线程结果传递到UI线程
     *
     * @param worker
     * @return
     */
    private void postCancel(final Worker worker) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                worker.onCanceled();
            }
        });
    }

    public <T> FutureTask<T> execute(Callable<T> call) {
        FutureTask<T> task = new FutureTask<T>(call);
        threadPool.execute(task);
        return task;
    }

    // 泛型抽象类
    public static abstract class Worker<T> {
        /**
         * T 类型参数
         * @return
         */
        protected abstract T doInBackground();

        /**
         * @param data
         * data 传值参数
         */
        protected void onPostExecute(T data) {
        }

        protected void onCanceled() {
        }

        protected void abort() {
        }
    }
}
