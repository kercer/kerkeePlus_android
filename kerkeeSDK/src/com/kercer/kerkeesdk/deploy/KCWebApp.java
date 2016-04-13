package com.kercer.kerkeesdk.deploy;

import java.io.File;

/**
 * Created by zihong on 16/3/18.
 */
public class KCWebApp
{
    //If ID = 0, that means the Webapp that contains all of the Webapps, and these all webapps in a file
    public int mID;

    public String mManifestUrl; //webapp's root manifest url
//    public String mFileHash;
    public File mRootPath;
}
