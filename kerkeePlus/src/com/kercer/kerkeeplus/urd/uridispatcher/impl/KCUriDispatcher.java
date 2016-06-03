package com.kercer.kerkeeplus.urd.uridispatcher.impl;


import android.text.TextUtils;
import android.util.Log;

import com.kercer.kerkeeplus.urd.uridispatcher.IUriRegister;
import com.kercer.kernet.uri.KCURI;

import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * urd 分发类,用于分发urd
 * Created by liweisu on 15/9/6.
 */

public class KCUriDispatcher {
    private static final String TAG = "KCUriDispatcher";
    private static String defaultScheme;
    private static HashMap<String, IUriRegister> schemes = new HashMap<String, IUriRegister>();

    private KCUriDispatcher() {

    }

    /**
     * 分发urd
     *
     * @param urd     urd协议
     * @param objects 相关的参数数组,自行传递
     * @return
     */
    public static boolean dispatcher(String urd, Object... objects) {
        if (!TextUtils.isEmpty(urd)) {
            KCURI uri = null;
            try {
                uri = KCURI.parse(urd);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            if (uri != null) {
                IUriRegister manager = getUriRegister(uri.getScheme());
                if (manager != null) {
                    return manager.dispatcher(uri, objects);
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是支持的urd协议
     * 只判断scheme
     * @param urd
     * @return
     */
    public static boolean isUrdProtocol(String urd) {
        KCURI uri = null;
        try {
            uri = KCURI.parse(urd);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (uri != null) {
            if (schemes.containsKey(uri.getScheme())) {
                return true;
            }
        }
        return false;
    }

    /**
     * before call getDefaultUrlManager(),should set setDefaultScheme
     *
     * @param scheme
     */
    public static void setDefaultScheme(String scheme) {
        if (!TextUtils.isEmpty(scheme) && !schemes.containsKey(scheme)) {
            defaultScheme = scheme;
            KCUriRegister manager = new KCUriRegister();
            schemes.put(defaultScheme, manager);
        }
    }

    /**
     * 获取默认的scheme
     */
    public static String getDefaultScheme() {
        if (!TextUtils.isEmpty(defaultScheme)) {
            return defaultScheme;
        }
        return "";
    }

    /**
     * 获取默认的urd注册器
     */
    public static IUriRegister getDefaultUriRegister() {
        if (!TextUtils.isEmpty(defaultScheme) && schemes.containsKey(defaultScheme)) {
            return schemes.get(defaultScheme);
        }
        Log.e(TAG, "未设置defaultScheme，无法提供默认Manager,调用setDefaultScheme设置默认scheme");
        return null;
    }

    /**
     * 根据scheme获取特定注册器
     * @param scheme scheme
     * @return 返回相关注册器
     */
    public static synchronized IUriRegister getUriRegister(String scheme) {
        if (!TextUtils.isEmpty(scheme)) {

            if (schemes.containsKey(scheme)) {
                return schemes.get(scheme);
            } else {
                KCUriRegister manager = new KCUriRegister();
                schemes.put(scheme, manager);
                return manager;
            }
        }
        return null;
    }
}
