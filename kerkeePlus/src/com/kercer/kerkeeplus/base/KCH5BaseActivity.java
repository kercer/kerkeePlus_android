package com.kercer.kerkeeplus.base;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.kercer.kercore.task.KCTaskExecutor;

public abstract class KCH5BaseActivity extends FragmentActivity
{
  public KCH5BaseActivity()
  {
  }
  public abstract KCBaseWebView getCurrentWebView();

  public abstract String getLoadUrl();


  /**
   * 加载当前h5页面
   */
  public void loadH5()
  {
    if (getCurrentWebView() != null)
    {
      final String url = getLoadUrl();
      KCTaskExecutor.runTaskOnUiThread(new Runnable()
      {
        @Override
        public void run()
        {
          getCurrentWebView().loadUrl(url);
        }
      });
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (getCurrentWebView() != null)
    {
      if (resultCode == RESULT_OK)
      {
        switch (requestCode)
        {
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
      if (getCurrentWebView().getUploadMessage() != null && (resultCode != RESULT_OK || data == null))
      {
        getCurrentWebView().getUploadMessage().onReceiveValue(null);
      }
    }
  }
}
