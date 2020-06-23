package com.kercer.kerkeeplus.base;

import android.content.Context;

public interface IDispatcher
{
  boolean dispatcher(Context context, String aUrl, String aOriginalUrl, boolean aIshandleHttp);
}
