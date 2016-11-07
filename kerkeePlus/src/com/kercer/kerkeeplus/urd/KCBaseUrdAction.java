package com.kercer.kerkeeplus.urd;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.kercer.kerkeeplus.base.KCBaseActivity;
import com.kercer.kerkeeplus.base.KCH5BaseActivity;
import com.kercer.kerkeeplus.urd.uridispatcher.IUriAction;
import com.kercer.kerkeeplus.urd.uridispatcher.impl.KCUriDispatcher;
import com.kercer.kernet.uri.KCNameValuePair;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * urd 抽象基础类,范型中传入，想要跳转的activity
 * Created by liweisu on 16/5/12.
 */
public abstract class KCBaseUrdAction<T extends FragmentActivity> implements IUriAction {
    public static final String EXTRA_LOAD_URL = KCH5BaseActivity.EXTRA_LOAD_URL;
    public static final String AFTER_URD = KCBaseActivity.AFTER_URD;
    //urd 透传给h5的参数
    public static final String URD_DATA = "urdData";
    private Class<T> clazz;
    protected KCUrdMetaData kcUrdMetaData;
    protected int requestCode = 10001;
    public KCBaseUrdAction() {
        kcUrdMetaData = new KCUrdMetaData(KCUrdEnv.getApplication());
        initSubClassInfo();
    }

    /**
     * 询问urd是否接受当前协议
     *
     * @param action host部分
     * @param path   path部分
     * @return true 接受，false不接受
     */
    @Override
    public boolean accept(String action, String path, List<KCNameValuePair> params) {
        kcUrdMetaData.resetData();
        kcUrdMetaData.setDefaultUrl(KCUrdEnv.isRemoteDebugEnable()?(TextUtils.isEmpty(getDebugH5Path())?defaultH5Path():getDebugH5Path()):defaultH5Path());
        kcUrdMetaData.setRootFilePath(getRootFilePath());
        kcUrdMetaData.setScheme(getScheme());
        kcUrdMetaData.initUrdData(action, path, params, clazz);
        return true;
    }

    /**
     * 此函数内部用于执行跳转指令,{@link #execAction(int...)}和{@link #execActionForResult(Activity, int, int...)}
     *
     * @param params  从协议中解析出来的参数
     * @param objects 如果不为空，默认第一项为activity
     */
    @Override
    public final void invokeAction(List<KCNameValuePair> params, Object... objects){
        if (objects != null && objects.length > 0 && objects[0] instanceof FragmentActivity)
            kcUrdMetaData.setActivity((FragmentActivity) objects[0]);
        if (!shouldRedirect(kcUrdMetaData)) {
            if (kcUrdMetaData.getActivity()!=null) {
                execActionForResult(kcUrdMetaData.getActivity(), requestCode);
            } else {
                execAction();
            }
        }
        kcUrdMetaData.resetData();
    }


    /**
     * 执行页面调度
     *
     * @param intentFlags
     */
    public void execAction(int... intentFlags) {
        kcUrdMetaData.initFlags();
        if (intentFlags != null && intentFlags.length > 0)
            kcUrdMetaData.initIntentFlags(intentFlags);
        Intent intent = kcUrdMetaData.getIntent();
        onBuildIntent(intent);
        KCUrdEnv.getApplication().startActivity(intent);
    }

    /**
     * 执行可以获取返回结果的activity跳转动作
     * activity 可以从 {@link #invokeAction(List, Object...)}中的object数组中获得
     *
     * @param activity
     * @param requestCode
     * @param intentFlags intentFlags
     */
    public void execActionForResult(Activity activity, int requestCode, int... intentFlags) {
        if (intentFlags != null && intentFlags.length > 0)
            kcUrdMetaData.initIntentFlags(intentFlags);
        Intent intent = kcUrdMetaData.getIntent();
        onBuildIntent(intent);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 检查类型信息
     */
    private void initSubClassInfo() {
        if (clazz == null) {
            try {
                Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                clazz = entityClass;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否要重定向当前urd，用于拦截时候，拦截后仍然可以跳转到用户目标页面
     *
     * @return 是否重定向
     */
    protected boolean shouldRedirect(KCUrdMetaData metaData) {
        return false;
    }

    /**
     * 子类定制intent使用
     *
     * @param intent
     */
    protected void onBuildIntent(Intent intent) {
    }

    /**
     * 返回默认的h5本地模版的相对路径,
     * 如果无默认模版路径(即，跳转的是native本地页面),则子类应该返回空字符串
     *
     * @return
     */
    public abstract String defaultH5Path();

    public String getDebugH5Path(){
        return "";
    }

    /**
     * 获得当前执行环境的模版根路径
     * @return
     */
    public String getRootFilePath() {
        return KCUrdEnv.isRemoteDebugEnable() ?
                KCUrdEnv.getRemoteDebugUrl():
                "file://" + KCUrdMetaData.getWebViewPath().getResRootPath();
    }

    public String getScheme(){
        return KCUriDispatcher.getDefaultScheme();
    }
}
