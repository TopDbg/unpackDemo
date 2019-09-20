package com.timi.origindex;

public class OriginUtils {
    public static String pwd = "WQ_PWD";
    public String key = "WQ_KEY";

    public static String getPwd(){
        return pwd;
    }

    public int calculateMoney(int a, int b){
        int c;
        c = a + b * 2;
        return c;
    }
}
