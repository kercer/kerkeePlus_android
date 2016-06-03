package com.kercer.kerkeeplus.base;

import android.content.Intent;

import com.kercer.kercore.task.KCTaskExecutor;

/**
 * h5模版框基础类
 * Created by liweisu on 16/5/12.
 */
public abstract class KCH5BaseActivity extends KCBaseActivity {
    public static final String EXTRA_LOAD_URL = "extraLoadUrl";

    /**
     * 返回当前的webview
     */
    public abstract KCBaseWebView getCurrentWebView();

    /**
     * 获取需要加载的html模版url
     *
     * @return load url 路径
     */
    public String getLoadUrl() {
        String loadUrl = "";
        Intent intent = getIntent();
        if (intent != null) {
            loadUrl = intent.getStringExtra(EXTRA_LOAD_URL);
        }
        return loadUrl;
    }

    /**
     * 加载当前h5页面
     */
    public void loadH5() {
        if (getCurrentWebView() != null) {
            final String url = getLoadUrl();
            KCTaskExecutor.runTaskOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getCurrentWebView().loadUrl(url);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理web view 图片选择
         */
        if (getCurrentWebView() != null) {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case KCBaseImageChooser.Image_Pick_RequestCode:
                        getCurrentWebView().getImageChooser().getImagePickResult(this, data);
                        break;
                    case KCBaseImageChooser.Camera_Capture_RequestCode:
                        getCurrentWebView().getImageChooser().getCameraPickResult(this, data);
                        break;
                    default:
                        break;
                }
            }
            if (getCurrentWebView().getUploadMessage() != null && (resultCode != RESULT_OK || data == null)) {
                getCurrentWebView().getUploadMessage().onReceiveValue(null);
            }
        }
    }
}
