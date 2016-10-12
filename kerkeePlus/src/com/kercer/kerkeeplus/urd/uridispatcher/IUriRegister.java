package com.kercer.kerkeeplus.urd.uridispatcher;

import com.kercer.kernet.uri.KCURI;

/**
 *
 * Created by liwei on 16/5/12.
 */
public interface IUriRegister {
    /**
     * 设置默认urd,即无匹配urd时，则调用defaultUrd
     * @param defaultUrdAction 默认urd
     */
    boolean setDefaultAction(IUriAction defaultUrdAction);

    /**
     * 注册一个urd
     * @param key key
     * @param urdAction urd动作
     * @return
     */
    boolean registerAction(String key,IUriAction urdAction);

    <T extends IUriAction> boolean registerAction(final String key,final Class<T> urdActionClass);

    /**
     * 取消一个urd
     * @param key key值
     * @return 是否取消成功
     */
    boolean unregisterAction(String key);

    /**
     * 分发urd
     * @param urlData urd对象模型
     * @param objects 可以带上自己想要的参数
     * @return
     */
    boolean dispatcher(KCURI urlData, Object... objects);
}
