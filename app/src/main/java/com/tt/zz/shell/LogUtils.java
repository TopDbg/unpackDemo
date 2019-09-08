package com.tt.zz.shell;


import android.util.Log;

public class LogUtils {
    private static final String tag = "TReflex";

    public static void i(String msg) {
        Log.i(tag, msg);
    }

    public static void e(String msg) {
        Log.e(tag, msg);
    }

    public static void e(Exception e){
        Log.e(tag,""+e);
    }
}