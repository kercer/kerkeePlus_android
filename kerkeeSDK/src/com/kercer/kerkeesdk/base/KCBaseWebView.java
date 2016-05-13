package com.kercer.kerkeesdk.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kercer.kerkee.webview.KCWebChromeClient;
import com.kercer.kerkee.webview.KCWebView;
import com.kercer.kerkee.webview.KCWebViewClient;
import com.kercer.kerkeesdk.urd.KCUrdConstance;
import com.kercer.kerkeesdk.urd.uridispatcher.impl.KCUriDispatcher;
import com.kercer.kernet.uri.KCNameValuePair;
import com.kercer.kernet.uri.KCURI;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;


/**
 * 基础webView 类,
 * Created by liweisu on 16/5/12.
 */
public class KCBaseWebView extends KCWebView {
    private ValueCallback<Uri> mUploadMessage;
    private OnPageFinished onPageFinished;
    private OnDataDownloadFinished onDataDownloadFinished;
    private OnLoadRes onLoadRes;
    private OnKeyBoardChanged onKeyBoardChanged;
    private OnTitleChanged onTitleChanged;
    private OnProgressChanged onProgressChanged;
    private OnPageStarted onPageStarted;
    private OnReceivedError onReceivedError;
    private KCBaseImageChooser mImageChooser;

    /**
     * 键盘被隐藏
     */
    public static final int KEYBOARD_STATE_HIDE = 0;
    /**
     * 键盘展示
     */
    public static final int KEYBOARD_STATE_SHOW = 1;
    /**
     * 当前的activity
     */
    private Activity activity;
    private boolean keyBoardShow = false;
    /**
     * 是否需要处理http,如果处理则会启动新的浏览器框
     * 否则只在当前模版框中加载
     */
    private boolean handleHttp = false;
    private OnWebViewScrollChanged onWebViewScrollChanged;

    public KCBaseWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initClient();
    }

    public KCBaseWebView(Context context, WebViewClient webViewClient, WebChromeClient webChromeClient) {
        super(context, webViewClient, webChromeClient);
        initClient();
    }

    public KCBaseWebView(Context context, boolean init) {
        super(context, init);
        initClient();
    }

    public KCBaseWebView(Context context) {
        super(context);
        initClient();
    }

    public KCBaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClient();
    }

    public KCBaseImageChooser getImageChooser() {
        return mImageChooser;
    }

    public void setImageChooser(KCBaseImageChooser mImageChooser) {
        this.mImageChooser = mImageChooser;
    }

    public ValueCallback<Uri> getUploadMessage() {
        return mUploadMessage;
    }

    public OnPageStarted getOnPageStarted() {
        return onPageStarted;
    }

    public void setOnPageStarted(OnPageStarted onPageStarted) {
        this.onPageStarted = onPageStarted;
    }

    public OnReceivedError getOnReceivedError() {
        return onReceivedError;
    }

    public void setOnReceivedError(OnReceivedError onReceivedError) {
        this.onReceivedError = onReceivedError;
    }

    public OnProgressChanged getOnProgressChanged() {
        return onProgressChanged;
    }

    public void setOnProgressChanged(OnProgressChanged onProgressChanged) {
        this.onProgressChanged = onProgressChanged;
    }

    public void setOnTitleChanged(OnTitleChanged onTitleChanged) {
        this.onTitleChanged = onTitleChanged;
    }

    public boolean isHandleHttp() {
        return handleHttp;
    }

    public void setHandleHttp(boolean handleHttp) {
        this.handleHttp = handleHttp;
    }

    public void setOnPageFinished(OnPageFinished onPageFinished) {
        this.onPageFinished = onPageFinished;
    }

    public void setOnDataDownloadFinished(OnDataDownloadFinished onDataDownloadFinished) {
        this.onDataDownloadFinished = onDataDownloadFinished;
    }

    public void setOnKeyBoardChanged(OnKeyBoardChanged onKeyBoardChanged) {
        this.onKeyBoardChanged = onKeyBoardChanged;
    }

    public void onDataDownloadFinished(WebView aWebView, String url) {
        if (onDataDownloadFinished != null) {
            onDataDownloadFinished.onDataDownloadFinished(aWebView, url);
        }
    }

    public void setOnLoadRes(OnLoadRes onLoadRes) {
        this.onLoadRes = onLoadRes;
    }

    public void setOnWebViewScrollChanged(OnWebViewScrollChanged onWebViewScrollChanged) {
        this.onWebViewScrollChanged = onWebViewScrollChanged;
    }

    /**
     * 初始化webview 部分设置
     */
    protected void initClient() {
        activity = (Activity) getContext();
        this.setWebViewClient(new DefaultWebViewClient());
        this.setWebChromeClient(new DefaultWebChromeClient());
        WebSettings webSettings = this.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    /**
     * 分发webview url,选文是否拦截
     *
     * @param aUrl
     * @return true 拦截,false 不拦截
     */
    public boolean dispatcher(String aUrl) {
        //urd协议拦截
        if (KCUriDispatcher.isUrdProtocol(aUrl)) {
            //urd拦截时,第一个参数默认传入当前的activity,方便urd action 跳转
            KCUriDispatcher.dispatcher(aUrl, getContext());
            try {
                KCURI kcuri = KCURI.parse(aUrl);
                for (KCNameValuePair pair : kcuri.getQueries()) {
                    /**
                     * urd中如果存在302 ,则需要销毁当前activity
                     */
                    if (pair.mKey.equalsIgnoreCase("statusCode") &&
                            pair.mValue.equalsIgnoreCase("302")) {
                        Activity activity = (Activity) getContext();
                        if (activity != null) {
                            activity.finish();
                        }
                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return true;
        }

        if (noUrdHandle(aUrl))
            return true;
        if ((aUrl.startsWith("https://") || aUrl.startsWith("http://")) &&
                handleHttp &&
                !aUrl.equals(getOriginalUrl())) {
            try {
                String tmpUrl = URLEncoder.encode(aUrl, "utf-8");
                KCUriDispatcher.dispatcher(KCUrdConstance.getDisPatcherBrowser() + tmpUrl, getContext());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    public class DefaultWebChromeClient extends KCWebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (onTitleChanged != null) {
                onTitleChanged.onTitleChanged(title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (onProgressChanged != null)
                onProgressChanged.onProgressChanged(newProgress);
        }

        // Android < 3.0
        @SuppressWarnings("static-access")
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            if (mImageChooser == null) {
                mImageChooser = new KCBaseImageChooser();
            }
            mImageChooser.showImageAndCameraChooser((Activity) getContext(), new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (mUploadMessage != null) {
                        mUploadMessage.onReceiveValue(null);
                    }
                }
            });
        }

        // 3.0 +
        @SuppressWarnings("static-access")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg);
        }

        // Android > 4.1.1
        @SuppressWarnings("static-access")
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            openFileChooser(uploadMsg);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                if (!keyBoardShow && h - oh < -100) {
                    if (onKeyBoardChanged != null) {
                        onKeyBoardChanged.onChanged(KEYBOARD_STATE_SHOW);
                    }
                    keyBoardShow = true;
                }
                if (keyBoardShow && h - oh > 100) {
                    if (onKeyBoardChanged != null) {
                        onKeyBoardChanged.onChanged(KEYBOARD_STATE_HIDE);
                    }
                    keyBoardShow = false;
                }
            }
        }
        super.onSizeChanged(w, h, ow, oh);
    }

    protected boolean noUrdHandle(String aUrl) {
        boolean handled;
        try {
            if (aUrl.endsWith(".apk") || aUrl.startsWith("smsto:") || aUrl.startsWith("mailto:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(aUrl));
                getContext().startActivity(intent);
                handled = true;
            } else if (aUrl.startsWith("tel:")) {
                Uri uri = Uri.parse(aUrl);
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                getContext().startActivity(intent);
                handled = true;
            } else {
                handled = false;
            }
        } catch (Exception e) {
            handled = false;
        }
        return handled;
    }

    public class DefaultWebViewClient extends KCWebViewClient {
        @Override
        public void onPageStarted(WebView aWebView, String aUrl, Bitmap aFavicon) {
            if (Build.VERSION.SDK_INT >= 19) {
                aWebView.getSettings().setLoadsImagesAutomatically(true);
            } else {
                aWebView.getSettings().setLoadsImagesAutomatically(false);
            }
            super.onPageStarted(aWebView, aUrl, aFavicon);
            if (onPageStarted != null)
                onPageStarted.OnPageStarted(aWebView, aUrl);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView aWebView, String aUrl) {
            if (dispatcher(aUrl)) {
                return true;
            }
            return super.shouldOverrideUrlLoading(aWebView, aUrl);
        }

        @Override
        public void onPageFinished(WebView aWebView, String aUrl) {
            if (!aWebView.getSettings().getLoadsImagesAutomatically()) {
                aWebView.getSettings().setLoadsImagesAutomatically(true);
            }
            super.onPageFinished(aWebView, aUrl);
            if (onPageFinished != null)
                onPageFinished.onPageFinished(aWebView, aUrl);
        }

        @Override
        public void onLoadResource(WebView aWebView, String aUrl) {
            super.onLoadResource(aWebView, aUrl);
            if (onLoadRes != null)
                onLoadRes.onLoadRes(aWebView, aUrl);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (onReceivedError != null) {
                onReceivedError.onReceivedError(errorCode, description, failingUrl);
            }
            Log.e(getClass().getSimpleName(), String.format("webview error !!!!errorCode=%d,failingUrl=%s,description=%s", errorCode, failingUrl, description));
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onWebViewScrollChanged != null)
            onWebViewScrollChanged.onWebViewScrollChanged(l, t, oldl, oldt);
    }

    public interface OnWebViewScrollChanged {
        void onWebViewScrollChanged(int l, int t, int oldl, int oldt);
    }

    public interface OnProgressChanged {
        void onProgressChanged(int newProgress);
    }

    public interface OnTitleChanged {
        void onTitleChanged(String title);
    }

    public interface OnPageStarted {
        void OnPageStarted(WebView aWebView, String aUrl);
    }

    public interface OnPageFinished {
        void onPageFinished(WebView aWebView, String aUrl);
    }

    public interface OnDataDownloadFinished {
        void onDataDownloadFinished(WebView aWebView, String aUrl);
    }

    public interface OnLoadRes {
        void onLoadRes(WebView aWebView, String aUrl);
    }

    /**
     * 键盘状态监听
     */
    public interface OnKeyBoardChanged {
        /**
         * {@link #KEYBOARD_STATE_HIDE} and {@link #KEYBOARD_STATE_HIDE}
         *
         * @param state
         */
        void onChanged(int state);
    }

    public interface OnReceivedError {
        void onReceivedError(int errorCode, String description, String failingUrl);
    }
}
