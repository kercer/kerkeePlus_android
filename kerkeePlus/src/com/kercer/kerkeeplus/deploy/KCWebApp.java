package com.kercer.kerkeeplus.deploy;

import com.kercer.kercore.debug.KCLog;
import com.kercer.kerkee.manifest.KCManifestObject;
import com.kercer.kernet.uri.KCURI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by zihong on 16/3/18.
 */
public class KCWebApp //implements KCDBObject
{
    //If ID = 0, that means the Webapp that contains all of the Webapps, and these all webapps in a file
    protected int mID;
    protected KCURI mManifestURI; //webapp's root manifest url
    //    public String mFileHash;
    protected File mRootPath;
    private KCDek mDekSelf;
    private Object mTag;

    public KCWebApp(int aID, File aRootPath, KCURI aManifestUri)
    {
        mID = aID;
        mRootPath = aRootPath;
        mManifestURI = aManifestUri;

        mDekSelf = new KCDek(aRootPath);
        mDekSelf.mManifestUri = aManifestUri;
        mDekSelf.mWebApp = this;
        if (aManifestUri != null && aManifestUri.getPath() != null)
        {
            mDekSelf.mManifestFileName = aManifestUri.getLastPathSegment();
        }
    }

    private KCWebApp()
    {
    }

//    public static KCWebApp webApp(byte[] aBytes)
//    {
//        return (KCWebApp)new KCWebApp().toObject(aBytes);
//    }

    public void setTag(Object aObject)
    {
        mTag = aObject;
    }

    public Object getTag()
    {
        return mTag;
    }

    public String getVersion()
    {
        String version = null;
        if (mDekSelf != null)
        {
            KCManifestObject manifestObject = mDekSelf.loadLocalManifest();
            if (manifestObject != null) version = manifestObject.getVersion();
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


    public String toString()
    {
        JSONObject object = new JSONObject();
        try
        {
            object.put("id", mID);
            String manifestUrl = mManifestURI != null ? mManifestURI.toString() : "";
            object.put("manifestUrl", manifestUrl == null ? "" : manifestUrl);
            object.put("rootPath", mRootPath != null ? mRootPath.getAbsolutePath() : "");
        }
        catch (JSONException e)
        {
            KCLog.e(e);
        }
        return object.toString();
    }


//    @Override
//    public byte[] toBytes()
//    {
//        return toString().getBytes();
//    }
//
//    @Override
//    public KCDBObject toObject(byte[] aBytes)
//    {
//        try
//        {
//            if (aBytes != null)
//            {
//                String jsonString = new String(aBytes);
//                JSONObject objectJSON = new JSONObject(jsonString);
//
//                int id = objectJSON.getInt("id");
//                String manifestUrl = objectJSON.getString("manifestUrl");
//                String rootPath = objectJSON.getString("rootPath");
//
//                KCURI manifestURI = KCURI.parse(manifestUrl);
//                File rootPathFile = new File(rootPath);
//
//                this.mID = id;
//                this.mManifestURI = manifestURI;
//                this.mRootPath = rootPathFile;
//            }
//        }
//        catch (Exception e)
//        {
//            KCLog.e(e);
//        }
//
//        return this;
//    }
}
