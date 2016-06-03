package com.kercer.kerkeeplus.urd;

import com.kercer.kerkeeplus.urd.uridispatcher.impl.KCUriDispatcher;

/**
 * Framework 中 ，会默认注册两个urd,分别是{@link #getBrowser()}
 * 通用浏览器框
 * 和{@link #getNativeH5()}
 * 通用本地模板框
 * Created by suliwei on 16/1/25.
 */
public class KCUrdConstance {
    /**
     * 返回当前的默认Scheme
     * @return
     */
    public static String getDefaultScheme(){
        return KCUriDispatcher.getDefaultScheme()+"://";
    }

    /**
     * 通用浏览器action字段
     * @return
     */
    public static String getBrowser(){
        return "Browser";
    }

    /**
     * 本地通用模板action 字段
     * @return
     */
    public static String getNativeH5(){
        return "NativeBrowser";
    }

    /**
     * 拼接分发本地h5通用模板urd 的字符串
     * @return
     */
    public static String getDisPatcherNativeH5(){
        return getDefaultScheme() + "NativeBrowser/";
    }

    /**
     * 拼接分发通用浏览器框urd 的字符串
     * @return
     */
    public static String getDisPatcherBrowser(){
        return getDefaultScheme() + "Browser/";
    }
}
