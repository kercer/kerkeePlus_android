package com.kercer.kerkee.activity;

import android.app.Application;

import com.kercer.kerkee.urd.LoginUrd;
import com.kercer.kerkee.urd.UserCenterUrd;
import com.kercer.kerkeesdk.urd.KCUrdEnv;
import com.kercer.kerkeesdk.urd.uridispatcher.IUriRegister;
import com.kercer.kerkeesdk.urd.uridispatcher.impl.KCUriDispatcher;

/**
 * Created by liweisu on 16/5/13.
 */
public class UrdSample {
    public static String scheme = "test";
    public static String loginAction = "login";
    public static String userCenterAction = "usercenter";

    public static String loginUrdStr = scheme + "://" + loginAction;
    public static String userCenterUrdStr = scheme + "://" + userCenterAction;
    /**
     * urd环境，action等 初始化 工作
     */
    public static void initUrd(Application application) {
        KCUrdEnv.initUrdContext(application);
        KCUrdEnv.initUrdDebug(false, "");

        KCUriDispatcher.setDefaultScheme(scheme);
        IUriRegister uriRegister = KCUriDispatcher.getDefaultUriRegister();
        if (uriRegister!=null) {
            uriRegister.registerAction(loginAction, new LoginUrd());
            uriRegister.registerAction(userCenterAction, new UserCenterUrd());
        }
    }
}
