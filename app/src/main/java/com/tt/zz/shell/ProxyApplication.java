package com.tt.zz.shell;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.ArrayMap;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.TreeMap;

import dalvik.system.DexClassLoader;

public class ProxyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        File cache = getDir("shell",MODE_PRIVATE);
        String srcDex = cache + "/encrypt.dex";
        File dexFile = FileManager.releaseAssesFile(this,"encrypt.dex",srcDex,null);
        //加密之后类的loader
        DexClassLoader cl = new DexClassLoader(srcDex,getDir("shell_oat",MODE_PRIVATE).getAbsolutePath(),getApplicationInfo().nativeLibraryDir,getClassLoader());

        Object clazzActivityThread = TReflex.newInstance("android.app.ActivityThread");
        Object currentActivityThread = TReflex.invokeMethodStatic(clazzActivityThread,"currentActivityThread",new Class[]{},new Object[]{});

        ArrayMap mPackages = (ArrayMap)TReflex.getField(currentActivityThread,"mPackages");
        WeakReference wr = (WeakReference) mPackages.get(getPackageName());
        TReflex.setFieldValue(wr.get(),"mClassLoader",cl);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Object clazzActivityThread = TReflex.newInstance("android.app.ActivityThread");
        Object currentActivityThread = TReflex.invokeMethodStatic(clazzActivityThread,"currentActivityThread",new Class[]{},new Object[]{});

        //在makeApplication进行修复
        Object mBoundApplicaion = TReflex.getField(currentActivityThread,"mBoundApplication");
        Object loadedApkInfo = TReflex.getField(mBoundApplicaion,"info");
        TReflex.setFieldValue(loadedApkInfo,"mApplication",null);

        String srcAppName = "com.tt.zz.shell.MyApplicaiton";
        ApplicationInfo appinfoInLoadedApk = (ApplicationInfo) TReflex.getField(loadedApkInfo,"mApplicationInfo");
        appinfoInLoadedApk.className = srcAppName;
        ApplicationInfo appInfoInAppBindData = (ApplicationInfo) TReflex.getField(mBoundApplicaion,"appInfo");
        appInfoInAppBindData.className = srcAppName;

        Application oldApplication = (Application) TReflex.getField(currentActivityThread,"mInitialApplication");

        ArrayList<Application> mAllApplications = (ArrayList<Application>) TReflex.getField(currentActivityThread,"mAllApplications");
        mAllApplications.remove(oldApplication);

        ArrayMap mPackages = (ArrayMap)TReflex.getField(currentActivityThread,"mPackages");
        WeakReference wr = (WeakReference) mPackages.get(getPackageName());

        Application realApp = (Application) TReflex.invokeMethod(wr.get(),"makeApplication",new Class[]{boolean.class, Instrumentation.class},new Object[]{false,null});
        realApp.onCreate();

        oldApplication = (Application) TReflex.getField(currentActivityThread,"mInitialApplication");
        TReflex.setFieldValue(oldApplication,"mInitalApplication",realApp);
    }
}
