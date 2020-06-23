package com.kercer.kerkee.activity;

import android.os.Bundle;

import com.kercer.kerkee_example.R;
import com.kercer.kerkeeplus.base.KCBaseWebView;
import com.kercer.kerkeeplus.base.KCH5BaseActivity;


public class UserCenterActivity extends KCH5BaseActivity {
    KCBaseWebView kcBaseWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        kcBaseWebView = (KCBaseWebView)findViewById(R.id.defaultWebView);
        loadH5();
    }

    @Override
    public KCBaseWebView getCurrentWebView() {
        return kcBaseWebView;
    }

  @Override
  public String getLoadUrl()
  {
    return null;
  }
}
