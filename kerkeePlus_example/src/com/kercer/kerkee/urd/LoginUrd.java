package com.kercer.kerkee.urd;

import com.kercer.kerkee.activity.LoginActivity;
import com.kercer.kerkeeplus.urd.KCBaseUrdAction;
import com.kercer.kernet.uri.KCNameValuePair;

import java.util.List;

/**
 * native实现的登录
 * Created by liwei on 16/5/13.
 */
public class LoginUrd extends KCBaseUrdAction<LoginActivity> {
    @Override
    public void invokeAction(List<KCNameValuePair> params, Object... objects) {
        execAction();
        //or
        //execActionForResult(activity,1);
    }

    /**
     * 因是native页面，所以返回空
     *
     * @return
     */
    @Override
    public String defaultH5Path() {
        return null;
    }
}
