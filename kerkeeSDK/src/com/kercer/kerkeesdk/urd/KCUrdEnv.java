package com.kercer.kerkeesdk.urd;

import android.app.Application;

/**
 * urd 基本环境
 * Created by liweisu on 16/5/13.
 */
public class KCUrdEnv {
    /**
     * 当前app 的application
     */
    private static Application application;
    /**
     * 是否开启远程调试,需要自己开一个服务才可使用,设置后，本地的模版路径会变成远程路径
     */
    private static boolean isRemoteDebugEnable;
    /**
     * 远程调试地址
     */
    private static String remoteDebugUrl;

    public static void initUrdContext(Application application){
        KCUrdEnv.application = application;
    }

    public static void initUrdDebug(boolean isRemoteDebugEnable,String remoteDebugUrl){
        KCUrdEnv.isRemoteDebugEnable = isRemoteDebugEnable;
        KCUrdEnv.remoteDebugUrl = remoteDebugUrl;
    }

    public static Application getApplication() {
        return application;
    }

    public static boolean isRemoteDebugEnable() {
        return isRemoteDebugEnable;
    }

    public static String getRemoteDebugUrl() {
        return remoteDebugUrl;
    }
}
