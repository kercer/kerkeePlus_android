package com.kercer.kerkeeplus.deploy;

import com.kercer.kerkee.manifest.KCManifestObject;
import com.kercer.kerkee.manifest.KCManifestParser;
import com.kercer.kernet.uri.KCURI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by zihong on 16/4/6.
 */
public class KCDek
{
    protected static String kDefaultManifestName = "cache.manifest";
    protected String mManifestFileName = KCDek.kDefaultManifestName;

    //contain manifest dir path
    protected KCManifestObject mManifestObject;
    //if net,is a url; if local, is a full path
    //it is dir path
    protected KCURI mManifestUri;
    //dek root path
    protected File mRootPath;
    //the dek belongs to a webapp
    protected KCWebApp mWebApp;

    protected Boolean mIsFromAssert;


    protected KCDek(File aRootPath)
    {
        mRootPath = aRootPath;
        mIsFromAssert = false;
    }

    protected KCManifestObject loadLocalManifest()
    {
        String deployManifest = mRootPath + File.separator + mManifestFileName;
        KCManifestObject manifestObject = null;
        try
        {
            manifestObject = KCManifestParser.ParserManifest(new FileInputStream(deployManifest));
        }
        catch (FileNotFoundException e)
        {
        }
        return manifestObject;
    }

    public KCManifestObject getManifestObject()
    {
        return mManifestObject;
    }
    public KCURI getManifestUri()
    {
        return mManifestUri;
    }
    public File getRootPath()
    {
        return mRootPath;
    }
    public KCWebApp getWebApp()
    {
        return mWebApp;
    }

    public boolean isFromAssert()
    {
        return mIsFromAssert;
    }

}
