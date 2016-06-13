package com.kercer.kerkeeplus.urd;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.kercer.kerkee.webview.KCWebPath;
import com.kercer.kerkeeplus.urd.uridispatcher.impl.KCUriDispatcher;
import com.kercer.kernet.uri.KCNameValuePair;
import com.kercer.kernet.uri.KCURI;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * urd 基础数据
 * Created by liwei on 16/5/12.
 */
public class KCUrdMetaData {
    private static KCWebPath webViewPath;
    /**
     * 当前urd 的path内容
     */
    private String path;
    /**
     * 是否是相对路径的uri
     */
    private boolean isRelativePath = false;
    /**
     * 是否存在path信息
     */
    private boolean hasPath = false;
    /**
     * 原始urdString
     */
    private String urdString;
    /**
     * 当前触发的urd 所携带的参数
     */
    private List<KCNameValuePair> params;

    /**
     * 页面跳转的intent
     */
    private Intent intent;

    /**
     * 默认本地模版路径
     */
    private String defaultUrl;

    /**
     * 本地模版根路径
     */
    private String rootFilePath;

    /**
     * 当前urd所使用的scheme
     */
    private String scheme = KCUriDispatcher.getDefaultScheme();

    /**
     * 构造基础路径
     *
     * @param application
     */
    public KCUrdMetaData(Application application) {
        if (webViewPath == null)
            webViewPath = new KCWebPath(application);
    }

    public void initUrdData(String action, String path, List<KCNameValuePair> params, Class clazz) {
        this.params = params;
        initUrdString(action, path, params);
        initPath();
        initIntent(clazz);
    }

    /**
     * 构造原始urd
     *
     * @param action
     * @param path
     * @param params
     */
    private void initUrdString(String action, String path, List<KCNameValuePair> params) {
        urdString = scheme + "://";
        if (!TextUtils.isEmpty(action))
            urdString += action;
        if (!TextUtils.isEmpty(path) && !path.equalsIgnoreCase("/")) {
            hasPath = true;
            this.path = path;
            urdString += path;
        } else {
            hasPath = false;
            this.path = null;
            isRelativePath = false;
        }
        String urdParams = "";
        if (params != null && params.size() > 0) {
            for (int i = 0; i < params.size(); i++) {
                if (i == 0)
                    urdParams += String.format("?%s=%s", params.get(i).mKey, params.get(i).mValue);
                else
                    urdParams += String.format("&%s=%s", params.get(i).mKey, params.get(i).mValue);
            }
        }
        this.urdString += urdParams;
    }

    /**
     * check当前path是相对路径，还是绝对路径,并初始化成员变量path
     */
    private void initPath() {
        if (hasPath) {
            String url;
            try {
                path = URLDecoder.decode(path.substring(1), "utf-8");
                KCURI kcuri = KCURI.parse(path);
                //如果是相对路径
                if (TextUtils.isEmpty(kcuri.getScheme())) {
                    isRelativePath = true;
                    url = getRootFilePath() + "/" + getPath();
                    path = url;
                } else {
                    isRelativePath = false;
                }
            } catch (UnsupportedEncodingException e) {
                path = null;
                Log.e("UrdAction", e.getMessage());
            } catch (URISyntaxException e) {
                path = null;
                Log.e("UrdAction", e.getMessage());
            }
        } else {
            isRelativePath = false;
            this.path = null;
        }
    }

    /**
     * 构造一个跳转intent
     *
     * @param clazz 目标class
     */
    private void initIntent(Class clazz) {
        intent = new Intent(KCUrdEnv.getApplication(), clazz);
        //获得加载路径
        String url = getUrl();
        intent.putExtra(KCBaseUrdAction.EXTRA_LOAD_URL, url);
        if (params != null && params.size() > 0) {
            for (KCNameValuePair pair : params) {
                if (pair.mKey.equalsIgnoreCase(KCBaseUrdAction.AFTER_URD)) {
                    String afterUrd = "";
                    try {
                        afterUrd = URLDecoder.decode(pair.mValue, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (!TextUtils.isEmpty(afterUrd)) {
                        intent.putExtra(pair.mKey, afterUrd);
                    }
                } else {
                    intent.putExtra(pair.mKey, pair.mValue);
                }
            }
        }
    }

    void initFlags() {
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }

    /**
     * 初始化intent flags值
     *
     * @param intentFlags
     */
    void initIntentFlags(int... intentFlags) {
        if (intent != null && intentFlags != null && intentFlags.length > 0) {
            for (int i : intentFlags) {
                intent.addFlags(i);
            }
        }
    }

    /**
     * 获得页面应该加载的路径,如果有path则返回path路径，无path则返回默认路径
     *
     * @return
     */
    private String getUrl() {
        if (hasPath) {
            return this.path;
        } else {
            return getDefaultUrl();
        }
    }


    /**
     * 如果当前urd不存在path路径，则加载默认的模板地址.
     *
     * @return 返回默认的模板地址路径
     */
    protected String getDefaultUrl() {
        if (TextUtils.isEmpty(defaultUrl))
            return "";
        return getRootFilePath() + defaultUrl;
    }

    /**
     * 重定向urd
     *
     * @param urdStr 要重定向哪里的 urd字符串
     */
    public void loadRedirectUrdString(String urdStr) {
        String afterUrd = "";
        try {
            afterUrd = URLEncoder.encode(getUrdString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String urd = urdStr;
        if (urd.contains("?"))
            urd = urd + String.format("&%s=%s", KCBaseUrdAction.AFTER_URD, afterUrd);
        else
            urd = urd + String.format("?%s=%s", KCBaseUrdAction.AFTER_URD, afterUrd);
        KCUriDispatcher.dispatcher(urd);
    }

    public void resetData() {
        path = null;
        isRelativePath = false;
        hasPath = false;
        urdString = "";
        params = null;
        intent = null;
        defaultUrl = "";
        rootFilePath = "";
        scheme = "";
    }


    public static KCWebPath getWebViewPath() {
        return webViewPath;
    }

    public String getPath() {
        return path;
    }

    void setPath(String path) {
        this.path = path;
    }

    public boolean isRelativePath() {
        return isRelativePath;
    }

    public boolean isHasPath() {
        return hasPath;
    }

    public String getUrdString() {
        return urdString;
    }

    public List<KCNameValuePair> getParams() {
        return params;
    }

    public Intent getIntent() {
        return intent;
    }

    void setDefaultUrl(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public String getRootFilePath() {
        return rootFilePath;
    }

    void setRootFilePath(String rootFilePath) {
        this.rootFilePath = rootFilePath;
    }

    public String getScheme() {
        return scheme;
    }

    void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
