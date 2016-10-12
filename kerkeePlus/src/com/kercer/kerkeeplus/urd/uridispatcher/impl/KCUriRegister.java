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
    private HashMap<String, Class> actionsClassWithKey = new HashMap<String, Class>();
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
     * 通过class方式注册，降低urd过多时的初始化时间，对象延迟创建（什么时候用什么时候创建）
     * @param <T> 实现IUriAction的类
     * @return 是否注册成功
     */
    public <T extends IUriAction> boolean registerAction(final String key,final Class<T> urdActionClass){
        if (!TextUtils.isEmpty(key) && urdActionClass != null) {
            KCTaskExecutor.runTaskOnUiThread(new Runnable() {
                @Override
                public void run() {
                    actionsClassWithKey.put(key.toLowerCase(), urdActionClass);
                    actionsWithKey.put(key.toLowerCase(), null);
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
                    actionsClassWithKey.remove(key.toLowerCase());
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
        if (!TextUtils.isEmpty(urlData.getHost())&&
                actionsWithKey.containsKey(urlData.getHost().toLowerCase())&&
                actionsWithKey.get(urlData.getHost().toLowerCase()) == null&&
                actionsClassWithKey.containsKey(urlData.getHost().toLowerCase())){
            try {
                actionsWithKey.put(urlData.getHost().toLowerCase(),
                        (IUriAction) actionsClassWithKey.get(urlData.getHost().toLowerCase()).newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        /**
         * 匹配注册的urd
         */
        if (!TextUtils.isEmpty(urlData.getHost()) &&
                actionsWithKey.containsKey(urlData.getHost().toLowerCase()) &&
                actionsWithKey.get(urlData.getHost().toLowerCase()).accept(urlData.getHost(), urlData.getPath(), urlData.getQueries())) {
            isSupported = true;
            KCTaskExecutor.runTaskOnUiThread(new Runnable() {
                @Override
                public void run() {
                    actionsWithKey.get(urlData.getHost().toLowerCase()).invokeAction(urlData.getQueries(), objects);
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
