package com.kercer.kerkee.urd;

import com.kercer.kerkee.activity.LoginActivity;
import com.kercer.kerkee.activity.UrdSample;
import com.kercer.kerkee.activity.UserCenterActivity;
import com.kercer.kerkeeplus.urd.KCBaseUrdAction;
import com.kercer.kerkeeplus.urd.KCUrdMetaData;
import com.kercer.kernet.uri.KCNameValuePair;

import java.util.List;

/**
 * h5实现的个人中心页面
 * Created by liweisu on 16/5/13.
 */
public class UserCenterUrd extends KCBaseUrdAction<UserCenterActivity> {
    @Override
    public void invokeAction(List<KCNameValuePair> params, Object... objects) {
        execAction();
    }

    /**
     * 拦截urd使用,因个人中心页面需要登录后才能查看,即如果未登录
     * 则，先跳转到登录界面，登录成功后，会跳转到个人中心页面
     *
     * @param metaData
     * @return
     */
    @Override
    protected boolean shouldRedirect(KCUrdMetaData metaData) {
        super.shouldRedirect(metaData);
        if (!LoginActivity.IS_LOGIN) {
            metaData.loadRedirectUrdString(UrdSample.loginUrdStr);
            return true;
        }
        return false;
    }

    @Override
    public String defaultH5Path() {
        return "/modules/test/test.html";
    }
}
