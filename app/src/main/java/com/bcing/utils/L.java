package com.bcing.utils;

import android.util.Log;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.utils
 * File Name:Log封装类
 * Describe：Application
 */

public class L {
    //开关
    public static final  boolean DEBUG = true;
    //TAG
    public static final String TAG = "bcing";
    //五个等级 DIWEF
    public static void  d(String text){
        if (DEBUG){
            Log.d(TAG, text);
        }
    }

    public static void  i(String text){
        if (DEBUG){
            Log.i(TAG, text);
        }
    }

    public static void  w(String text){
        if (DEBUG){
            Log.w(TAG, text);
        }
    }

    public static void  e(String text){
        if (DEBUG){
            Log.e(TAG, text);
        }
    }


}
