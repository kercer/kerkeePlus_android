package com.kercer.kerkeeplus.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

//import com.gegejia.urd.UrdDispatch;

public class KCDefaultDispatcher implements IDispatcher
{

  @Override
  public boolean dispatcher(Context context, String aUrl, String aOriginalUrl, boolean aIshandleHttp)
  {
    return false;
  }


  protected boolean noUrdHandle(Context context, String aUrl)
  {
    boolean handled;
    try
    {
      if (!aUrl.endsWith(".apk") && !aUrl.startsWith("smsto:") && !aUrl.startsWith("mailto:"))
      {
        if (aUrl.startsWith("tel:"))
        {
          Uri uri = Uri.parse(aUrl);
          Intent intent = new Intent("android.intent.action.CALL", uri);
          context.startActivity(intent);
          handled = true;
        }
        else
        {
          handled = false;
        }
      }
      else
      {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(aUrl));
        context.startActivity(intent);
        handled = true;
      }
    }
    catch (Exception var5)
    {
      handled = false;
    }

    return handled;
  }

}
