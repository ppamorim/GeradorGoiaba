package com.goiaba.goiabas.geradorgoiaba.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.WindowManager;

public class ViewUtil {

    private Context mContext;
    private Resources mResources;

    public ViewUtil(Context context) {
        mContext = context;
        mResources = context.getResources();
    }

    public int getScreenWidth() {
        int screenWidth = mResources.getDisplayMetrics().widthPixels;
        if(android.os.Build.VERSION.SDK_INT >= 13) {
            screenWidth = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        }
        return screenWidth;
    }

    public int getScreenHeight() {
        int screenHeight = mResources.getDisplayMetrics().heightPixels;
        if(android.os.Build.VERSION.SDK_INT >= 13) {
            screenHeight = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        }
        return screenHeight;
    }

}
