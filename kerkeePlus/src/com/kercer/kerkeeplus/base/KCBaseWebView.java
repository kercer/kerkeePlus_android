package com.kercer.kerkeeplus.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

public class KCBaseWebView extends KCWebView
{
  private ValueCallback<Uri> mUploadMessage;
  private KCBaseWebView.OnPageFinished onPageFinished;
  private KCBaseWebView.OnDataDownloadFinished onDataDownloadFinished;
  private KCBaseWebView.OnLoadRes onLoadRes;
  private KCBaseWebView.OnKeyBoardChanged onKeyBoardChanged;
  private KCBaseWebView.OnTitleChanged onTitleChanged;
  private KCBaseWebView.OnProgressChanged onProgressChanged;
  private KCBaseWebView.OnPageStarted onPageStarted;
  private KCBaseWebView.OnReceivedError onReceivedError;
  private KCBaseImageChooser mImageChooser;
  public static final int KEYBOARD_STATE_HIDE = 0;
  public static final int KEYBOARD_STATE_SHOW = 1;
  private Activity activity;
  private boolean keyBoardShow = false;
  private boolean handleHttp = false;
  private KCBaseWebView.OnWebViewScrollChanged onWebViewScrollChanged;
  private IDispatcher mDispatcher;

  public KCBaseWebView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.initClient();
  }

  public KCBaseWebView(Context context, WebViewClient webViewClient, WebChromeClient webChromeClient)
  {
    super(context, webViewClient, webChromeClient);
    this.initClient();
  }

  //    public KCBaseWebView(Context context, boolean init) {
  //        super(context, init);
  //        this.initClient();
  //    }

  public KCBaseWebView(Context context)
  {
    super(context);
    this.initClient();
  }

  public KCBaseWebView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.initClient();
  }

  public void setDispatcher(IDispatcher aDispatcher)
  {
    mDispatcher = aDispatcher;
  }

  public KCBaseImageChooser getImageChooser()
  {
    return this.mImageChooser;
  }

  public void setImageChooser(KCBaseImageChooser mImageChooser)
  {
    this.mImageChooser = mImageChooser;
  }

  public ValueCallback<Uri> getUploadMessage()
  {
    return this.mUploadMessage;
  }

  public KCBaseWebView.OnPageStarted getOnPageStarted()
  {
    return this.onPageStarted;
  }

  public void setOnPageStarted(KCBaseWebView.OnPageStarted onPageStarted)
  {
    this.onPageStarted = onPageStarted;
  }

  public KCBaseWebView.OnReceivedError getOnReceivedError()
  {
    return this.onReceivedError;
  }

  public void setOnReceivedError(KCBaseWebView.OnReceivedError onReceivedError)
  {
    this.onReceivedError = onReceivedError;
  }

  public KCBaseWebView.OnProgressChanged getOnProgressChanged()
  {
    return this.onProgressChanged;
  }

  public void setOnProgressChanged(KCBaseWebView.OnProgressChanged onProgressChanged)
  {
    this.onProgressChanged = onProgressChanged;
  }

  public void setOnTitleChanged(KCBaseWebView.OnTitleChanged onTitleChanged)
  {
    this.onTitleChanged = onTitleChanged;
  }

  public boolean isHandleHttp()
  {
    return this.handleHttp;
  }

  public void setHandleHttp(boolean handleHttp)
  {
    this.handleHttp = handleHttp;
  }

  public void setOnPageFinished(KCBaseWebView.OnPageFinished onPageFinished)
  {
    this.onPageFinished = onPageFinished;
  }

  public void setOnDataDownloadFinished(KCBaseWebView.OnDataDownloadFinished onDataDownloadFinished)
  {
    this.onDataDownloadFinished = onDataDownloadFinished;
  }

  public void setOnKeyBoardChanged(KCBaseWebView.OnKeyBoardChanged onKeyBoardChanged)
  {
    this.onKeyBoardChanged = onKeyBoardChanged;
  }

  public void onDataDownloadFinished(WebView aWebView, String url)
  {
    if (this.onDataDownloadFinished != null)
    {
      this.onDataDownloadFinished.onDataDownloadFinished(aWebView, url);
    }

  }

  public void setOnLoadRes(KCBaseWebView.OnLoadRes onLoadRes)
  {
    this.onLoadRes = onLoadRes;
  }

  public void setOnWebViewScrollChanged(KCBaseWebView.OnWebViewScrollChanged onWebViewScrollChanged)
  {
    this.onWebViewScrollChanged = onWebViewScrollChanged;
  }

  protected void initClient()
  {
    this.activity = (Activity) this.getContext();
    this.setWebViewClient(new KCBaseWebView.DefaultWebViewClient());
    this.setWebChromeClient(new KCBaseWebView.DefaultWebChromeClient());
    WebSettings webSettings = this.getSettings();
    webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
  }


  protected void onSizeChanged(int w, int h, int ow, int oh)
  {
    if (this.activity != null)
    {
      InputMethodManager imm = (InputMethodManager) this.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
      if (imm.isActive())
      {
        if (!this.keyBoardShow && h - oh < -100)
        {
          if (this.onKeyBoardChanged != null)
          {
            this.onKeyBoardChanged.onChanged(1);
          }

          this.keyBoardShow = true;
        }

        if (this.keyBoardShow && h - oh > 100)
        {
          if (this.onKeyBoardChanged != null)
          {
            this.onKeyBoardChanged.onChanged(0);
          }

          this.keyBoardShow = false;
        }
      }
    }

    super.onSizeChanged(w, h, ow, oh);
  }


  protected void onScrollChanged(int l, int t, int oldl, int oldt)
  {
    super.onScrollChanged(l, t, oldl, oldt);
    if (this.onWebViewScrollChanged != null)
    {
      this.onWebViewScrollChanged.onWebViewScrollChanged(l, t, oldl, oldt);
    }

  }

  public interface OnReceivedError
  {
    void onReceivedError(int var1, String var2, String var3);
  }

  public interface OnKeyBoardChanged
  {
    void onChanged(int var1);
  }

  public interface OnLoadRes
  {
    void onLoadRes(WebView var1, String var2);
  }

  public interface OnDataDownloadFinished
  {
    void onDataDownloadFinished(WebView var1, String var2);
  }

  public interface OnPageFinished
  {
    void onPageFinished(WebView var1, String var2);
  }

  public interface OnPageStarted
  {
    void OnPageStarted(WebView var1, String var2);
  }

  public interface OnTitleChanged
  {
    void onTitleChanged(String var1);
  }

  public interface OnProgressChanged
  {
    void onProgressChanged(int var1);
  }

  public interface OnWebViewScrollChanged
  {
    void onWebViewScrollChanged(int var1, int var2, int var3, int var4);
  }

  public class DefaultWebViewClient extends KCWebViewClient
  {
    public DefaultWebViewClient()
    {
    }

    public void onPageStarted(WebView aWebView, String aUrl, Bitmap aFavicon)
    {
      if (Build.VERSION.SDK_INT >= 19)
      {
        aWebView.getSettings().setLoadsImagesAutomatically(true);
      }
      else
      {
        aWebView.getSettings().setLoadsImagesAutomatically(false);
      }

      super.onPageStarted(aWebView, aUrl, aFavicon);
      if (KCBaseWebView.this.onPageStarted != null)
      {
        KCBaseWebView.this.onPageStarted.OnPageStarted(aWebView, aUrl);
      }

    }

    public boolean shouldOverrideUrlLoading(WebView aWebView, String aUrl)
    {
      return (mDispatcher != null && mDispatcher.dispatcher(getContext(), aUrl, getOriginalUrl(), isHandleHttp())) ? true : super.shouldOverrideUrlLoading(aWebView, aUrl);
    }

    public void onPageFinished(WebView aWebView, String aUrl)
    {
      if (!aWebView.getSettings().getLoadsImagesAutomatically())
      {
        aWebView.getSettings().setLoadsImagesAutomatically(true);
      }

      super.onPageFinished(aWebView, aUrl);
      if (KCBaseWebView.this.onPageFinished != null)
      {
        KCBaseWebView.this.onPageFinished.onPageFinished(aWebView, aUrl);
      }

    }

    public void onLoadResource(WebView aWebView, String aUrl)
    {
      super.onLoadResource(aWebView, aUrl);
      if (KCBaseWebView.this.onLoadRes != null)
      {
        KCBaseWebView.this.onLoadRes.onLoadRes(aWebView, aUrl);
      }

    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
    {
      super.onReceivedError(view, errorCode, description, failingUrl);
      if (KCBaseWebView.this.onReceivedError != null)
      {
        KCBaseWebView.this.onReceivedError.onReceivedError(errorCode, description, failingUrl);
      }

      Log.e(this.getClass().getSimpleName(), String.format("webview error !!!!errorCode=%d,failingUrl=%s,description=%s", errorCode, failingUrl, description));
    }
  }

  public class DefaultWebChromeClient extends KCWebChromeClient
  {
    public DefaultWebChromeClient()
    {
    }

    public void onReceivedTitle(WebView view, String title)
    {
      if (KCBaseWebView.this.onTitleChanged != null)
      {
        KCBaseWebView.this.onTitleChanged.onTitleChanged(title);
      }

    }

    public void onProgressChanged(WebView view, int newProgress)
    {
      super.onProgressChanged(view, newProgress);
      if (KCBaseWebView.this.onProgressChanged != null)
      {
        KCBaseWebView.this.onProgressChanged.onProgressChanged(newProgress);
      }

    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg)
    {
      KCBaseWebView.this.mUploadMessage = uploadMsg;
      if (KCBaseWebView.this.mImageChooser == null)
      {
        KCBaseWebView.this.mImageChooser = new KCBaseImageChooser();
      }

      KCBaseWebView.this.mImageChooser.showImageAndCameraChooser((Activity) KCBaseWebView.this.getContext(), new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface dialog)
        {
          if (KCBaseWebView.this.mUploadMessage != null)
          {
            KCBaseWebView.this.mUploadMessage.onReceiveValue(null);
          }

        }
      });
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType)
    {
      this.openFileChooser(uploadMsg);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
    {
      this.openFileChooser(uploadMsg);
    }
  }
}
