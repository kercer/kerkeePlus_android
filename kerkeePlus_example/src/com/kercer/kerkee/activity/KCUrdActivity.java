package com.kercer.kerkee.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kercer.kerkee_example.R;
import com.kercer.kerkeeplus.base.KCBaseActivity;
import com.kercer.kerkeeplus.urd.uridispatcher.impl.KCUriDispatcher;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * urd测试页面
 * Created by liweisu on 16/5/13.
 */
public class KCUrdActivity extends KCBaseActivity {
    Button loginBtn;
    Button userCenterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urd);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        userCenterBtn = (Button) findViewById(R.id.userCenterBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 分发登录，urd，要传递的参数，直接坠在最后，参考url
                 */
                KCUriDispatcher.dispatcher(UrdSample.loginUrdStr + "?test=100");
            }
        });


        userCenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 分发个人中心页面,urd
                 */
                KCUriDispatcher.dispatcher(UrdSample.userCenterUrdStr);
            }
        });

        View userCenterBtn1 = findViewById(R.id.userCenterBtn1);
        userCenterBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 跳转到个人中心，使用相对路径,相对路径需要,使用urlencode一下
                 */
                try {
                    KCUriDispatcher.dispatcher(UrdSample.userCenterUrdStr + "/" + URLEncoder.encode("modules/test/test.html","utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        View userCenterBtn2 = findViewById(R.id.userCenterBtn2);
        userCenterBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 跳转到个人中心，使用绝对路径,绝对路径需要,使用urlencode一下
                 */
                try {
                    KCUriDispatcher.dispatcher(UrdSample.userCenterUrdStr + "/" + URLEncoder.encode("http://www.baidu.com","utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }





}
