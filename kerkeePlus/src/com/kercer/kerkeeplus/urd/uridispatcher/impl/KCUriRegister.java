package com.kercer.kerkeeplus.urd.uridispatcher.impl;

import android.text.TextUtils;

import com.kercer.kercore.task.KCTaskExecutor;
import com.kercer.kerkeeplus.urd.uridispatcher.IUriAction;
import com.kercer.kerkeeplus.urd.uridispatcher.IUriRegister;
import com.kercer.kernet.uri.KCURI;

import java.util.HashMap;

/**
 * urd注册类,用于注册urd
 * Created by liweisu on 15/9/6.
 */
class KCUriRegister implements IUriRegister {
    private HashMap<String, IUriAction> actionsWithKey = new HashMap<String, IUriAction>();
    private IUriAction defaultUrdAction;

    /**
     * 是否包含此action
     *
     * @param key key值
     * @return 存在返回true, 不存在返回false
     */
    protected boolean containsAction(String key) {

        return key != null && actionsWithKey.containsKey(key);
    }

    /**
     * 设置默认urd action
     *
     * @param defaultUrdAction 默认urd
     * @return
     */
    @Override
    public boolean setDefaultAction(IUriAction defaultUrdAction) {
        this.defaultUrdAction = defaultUrdAction;
        return true;
    }

    /**
     * 注册urd action
     *
     * @param key       key
     * @param urdAction urd动作
     * @return
     */
    @Override
    public boolean registerAction(final String key, final IUriAction urdAction) {
        if (!TextUtils.isEmpty(key) && urdAction != null) {
            KCTaskExecutor.runTaskOnUiThread(new Runnable() {
                @Override
                public void run() {
                    actionsWithKey.put(key.toLowerCase(), urdAction);
                }
            });
            return true;
        }
        return false;
    }

    /**
     * 取消一个urd action
     *
     * @param key key值
     * @return
     */
    @Override
    public boolean unregisterAction(final String key) {
        if (!TextUtils.isEmpty(key) && actionsWithKey.containsKey(key)) {

            KCTaskExecutor.runTaskOnUiThread(new Runnable() {
                @Override
                public void run() {
                    actionsWithKey.remove(key.toLowerCase());
                }
            });
            return true;
        }
        return false;
    }

    /**
     * 分发urd,交给匹配的urd action去处理
     *
     * @param urlData urd对象模型
     * @param objects 可以带上自己想要的参数
     */
    @Override
    public boolean dispatcher(final KCURI urlData, final Object... objects) {
        boolean isSupported = false;
        /**
         * 匹配注册的urd
         */
        if (!TextUtils.isEmpty(urlData.getHost()) &&
                actionsWithKey.containsKey(urlData.getHost().toLowerCase()) &&
                actionsWithKey.get(urlData.getHost()).accept(urlData.getHost(), urlData.getPath(), urlData.getQueries())) {
            isSupported = true;
            KCTaskExecutor.runTaskOnUiThread(new Runnable() {
                @Override
                public void run() {
                    actionsWithKey.get(urlData.getHost()).invokeAction(urlData.getQueries(), objects);
                }
            });
        }
        //执行默认动作
        if (!isSupported && defaultUrdAction != null &&
                defaultUrdAction.accept(urlData.getHost(),
                        urlData.getPath(),
                        urlData.getQueries())) {
            isSupported = true;
            KCTaskExecutor.runTaskOnUiThread(new Runnable() {
                @Override
                public void run() {
                    defaultUrdAction.invokeAction(urlData.getQueries());
                }
            });
        }
        return isSupported;
    }
}
