package com.airsaid.warmjacket.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.view.View;

import com.airsaid.warmjacket.application.BaseApplication;


public class UIUtils {

    /**
     * 获取上下文
     */
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    /**
     * 获取Handler
     */
    public static Handler getHandler() {
        return BaseApplication.getHandler();
    }

    /**
     * 获取主线程
     */
    public static Thread getMainThread() {
        return BaseApplication.getMainThread();
    }

    /**
     * 获取主线程ID
     */
    public static int getMainThreadId() {
        return BaseApplication.getMainThreadId();
    }

    /**
     * 根据布局文件id填充View对象
     */
    public static View inflate(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }

    /**
     * 获取Resources资源目录
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 通过id获取颜色
     */
    public static int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(id, null);
        } else {
            return getResources().getColor(id);
        }
    }

    /**
     * 通过id获取一张图片
     */
    public static Drawable getDrawable(int drawableId) {
        return getResources().getDrawable(drawableId);
    }

    public static String getString(int stringId) {
        return getResources().getString(stringId);
    }


    public static String[] getStringArray(int stringarrayId) {
        return getResources().getStringArray(stringarrayId);
    }

    /**
     * 根据文字选择器id获取颜色选择器
     */
    public static ColorStateList getColorStateList(int mTabTextColorResId) {
        return getResources().getColorStateList(mTabTextColorResId);
    }

    /**
     * 将任务维护在主线程中运行
     */
    public static void runInMainThread(Runnable runnable) {
        if (getMainThreadId() == android.os.Process.myTid()) {
            runnable.run();
        } else {
            getHandler().post(runnable);
        }
    }

    public static int dip2px(int dip) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5);
    }

    public static int px2dip(int px) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);
    }
}