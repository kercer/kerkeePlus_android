package com.kercer.kerkee.urd;

import com.kercer.kerkee.activity.LoginActivity;
import com.kercer.kerkeeplus.urd.KCBaseUrdAction;

/**
 * native实现的登录
 * Created by liwei on 16/5/13.
 */
public class LoginUrd extends KCBaseUrdAction<LoginActivity> {

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
