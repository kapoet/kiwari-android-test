package com.ervin.kiwariandroidtest.helpers;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

public class AppExecutors {

    private final Executor mDiskIO;

    private final Executor mNetworkIO;

    private final Executor mMainThread;

    private static volatile AppExecutors mInstance;

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.mDiskIO = diskIO;
        this.mNetworkIO = networkIO;
        this.mMainThread = mainThread;
    }

    private static AppExecutors getInstance() {
        if (mInstance == null) {
            synchronized (AppExecutors.class) {
                mInstance = new AppExecutors();
            }
        }
        return mInstance;
    }

    @Inject
    public AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
                new MainThreadExecutor());
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    public static void xDisk(@NonNull Runnable command) {
        getInstance().diskIO().execute(command);
    }
    public static void xNet(@NonNull Runnable command) {
        getInstance().networkIO().execute(command);
    }
    public static void xMain(@NonNull Runnable command) {
        getInstance().mainThread().execute(command);
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
