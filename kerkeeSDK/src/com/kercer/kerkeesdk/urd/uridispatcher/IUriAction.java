package com.kercer.kerkeesdk.urd.uridispatcher;

import com.kercer.kernet.uri.KCNameValuePair;

import java.util.List;

/**
 * urd基础接口类
 * Created by liweisu on 15/9/6.
 */

public interface IUriAction {

    /**
     * 是否接受当前action协议
     *
     * @param action host部分
     * @param path   path部分
     * @param params 当前urd中所带参数
     * @return 是否接受此协议 true接受，false不接受，false则不会触发action中的动作;
     */
    boolean accept(String action, String path, List<KCNameValuePair> params);

    /**
     * 执行动作
     *
     * @param params 从协议中解析出来的参数
     */
    void invokeAction(List<KCNameValuePair> params, Object... objects);
}
