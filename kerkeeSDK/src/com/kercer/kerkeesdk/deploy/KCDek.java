package com.kercer.kerkeesdk.deploy;

import com.kercer.kerkee.manifest.KCManifestObject;
import com.kercer.kernet.uri.KCURI;

import java.io.File;

/**
 * Created by zihong on 16/4/6.
 */
public class KCDek
{
    //contain manifest dir path
    public KCManifestObject mManifestObject;

    //if net,is a url; if local, is a full path
    //it is dir path
    public KCURI mManifestUri;

    //dek root path
    public File mRootPath;

    //the dek belongs to a webapp
    public KCWebApp mWebApp;

}
