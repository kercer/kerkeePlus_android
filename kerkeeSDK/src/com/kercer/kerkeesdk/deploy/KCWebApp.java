package com.kercer.kerkeesdk.deploy;

import com.kercer.kernet.uri.KCURI;

import java.io.File;

/**
 * Created by zihong on 16/3/18.
 */
public class KCWebApp
{
    //If ID = 0, that means the Webapp that contains all of the Webapps, and these all webapps in a file
    protected int mID;
    protected KCURI mManifestURI; //webapp's root manifest url
//    public String mFileHash;
    protected File mRootPath;
    private KCDek mDekSelf;

    public KCWebApp(int aID, File aRootPath, KCURI aManifestUri)
    {
        mID = aID;
        mRootPath = aRootPath;
        mManifestURI = aManifestUri;

        mDekSelf = new KCDek(aRootPath);
        mDekSelf.mManifestUri = aManifestUri;
        mDekSelf.mWebApp = this;
        if (aManifestUri != null)
        {
            mDekSelf.mManifestFileName = aManifestUri.getLastPathSegment();
        }
    }

    public String getVersion()
    {
        String version = null;
        if (mDekSelf != null)
        {
            version = mDekSelf.getLocalDekVersion();
        }
        return version;
    }

    public int getID()
    {
        return mID;
    }
    public File getRootPath()
    {
        return mRootPath;
    }

    public KCURI getManifestURI()
    {
        return mManifestURI;
    }
}
