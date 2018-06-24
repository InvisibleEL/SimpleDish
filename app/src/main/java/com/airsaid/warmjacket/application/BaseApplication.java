package com.airsaid.warmjacket.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.airsaid.warmjacket.common.CommonConstants;
import com.mob.mobapi.MobAPI;


public class BaseApplication extends Application {

    private static Context mContext;
    private static Handler mHandler;
    private static Thread mMainThread;
    private static int mMainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mHandler = new Handler();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();

        // 初始化MobApi
        MobAPI.initSDK(this, CommonConstants.KEY);
    }


    /**
     * 获取上下文对象
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取Handler对象
     */
    public static Handler getHandler() {
        return mHandler;
    }

    /**
     * 获取主线程对象
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程ID
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }
}
