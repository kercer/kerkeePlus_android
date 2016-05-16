package com.kercer.kerkee.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kercer.kerkee_example.R;
import com.kercer.kerkeesdk.base.KCBaseActivity;


/**
 *
 */
public class LoginActivity extends KCBaseActivity {
    /**
     * sample 代码，建议客户端构建自己的用户中心数据模型，此处代码，只是用来标示用户是否登录.
     *
     */
    public static boolean IS_LOGIN = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View loginButton = findViewById(R.id.email_sign_in_button);
        String test = getIntent().getStringExtra("test");
        Toast.makeText(KCApplication.instance,test,Toast.LENGTH_SHORT).show();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IS_LOGIN = true;
                Toast.makeText(KCApplication.instance,"登录成功",Toast.LENGTH_SHORT).show();
                finish();
                /**
                 * 如果是拦截urd跳转到登录界面，则继续分发用户意图的，urd
                 */
                invokeAfterUrd();
            }
        });
    }
}

