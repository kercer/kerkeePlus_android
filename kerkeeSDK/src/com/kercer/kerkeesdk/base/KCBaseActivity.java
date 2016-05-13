package com.kercer.kerkeesdk.base;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.kercer.kerkeesdk.urd.uridispatcher.impl.KCUriDispatcher;

/**
 * activity 基础类
 * Created by liweisu on 16/5/12.
 */
public class KCBaseActivity extends Activity {
    /**
     * key,被拦截的urd key
     */
    public static final String AFTER_URD = "afterUrd";
    /**
     * 被拦截的urd
     */
    private String afterUrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        afterUrd = getIntent().getStringExtra(AFTER_URD);
    }

    /**
     * 是否存在urd
     * @return true 存在，否则false
     */
    public boolean hasAfterUrd(){
        return !TextUtils.isEmpty(afterUrd);
    }

    /**
     * 触发afterUrd动作
     */
    public void invokeAfterUrd(){
        if (hasAfterUrd()){
            KCUriDispatcher.dispatcher(afterUrd,this);
            /**
             * 消费型，一次销毁
             */
            afterUrd = null;
        }
    }
}
