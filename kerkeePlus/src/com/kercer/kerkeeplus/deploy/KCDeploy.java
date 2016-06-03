package com.kercer.kerkeeplus.deploy;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.kercer.kercore.debug.KCLog;
import com.kercer.kercore.io.KCZip;
import com.kercer.kercore.util.KCUtilMd5;
import com.kercer.kercore.io.KCUtilFile;
import com.kercer.kerkee.webview.KCWebPath;
import com.kercer.kercore.util.KCMainBundle;

import java.io.File;

/**
 * Created by zihong on 16/3/16.
 */
public class KCDeploy
{
    public static KCMainBundle mMainBundle = null;
    private KCWebPath mWebPath;
    protected Context mContext;
    protected KCDeployFlow mDeployFlow;

    protected KCDeploy(Context aContext)
    {
        this(aContext, null);
    }

    protected KCDeploy(Context aContext, KCDeployFlow aDeployFlow)
    {
        mContext = aContext;
        mWebPath = new KCWebPath(aContext);
        if (mMainBundle == null)
            mMainBundle = new KCMainBundle(aContext);
        if (aDeployFlow != null)
            mDeployFlow = aDeployFlow;
        else
            mDeployFlow = new KCDeployFlowDefault();
    }

    public void setDeployFlow(KCDeployFlow aDeployFlow)
    {
        mDeployFlow = aDeployFlow;
    }

    public KCMainBundle getMainBundle()
    {
        return mMainBundle;
    }

    protected String getRootPath()
    {
        return mWebPath.getRootPath();
    }

    protected String getResRootPath()
    {
        return mWebPath.getResRootPath();
    }

    protected boolean deploy(File aSrcFile, KCDek aDek)
    {
        if (aSrcFile.exists())
        {
            File tmpZipFile = mDeployFlow.decodeFile(aSrcFile);
            if (tmpZipFile != null && tmpZipFile.exists())
            {
                File dirDek = aDek.mRootPath;
                KCUtilFile.deleteRecyle(dirDek);
                dirDek.mkdirs();

                try
                {
                    if (!dirDek.exists())
                    {
                        dirDek.mkdirs();
                    }
                    KCZip.unZipToDir(tmpZipFile, dirDek);
                    tmpZipFile.delete();
                }
                catch (Exception e)
                {
                    KCUtilFile.deleteRecyle(dirDek);
                    KCLog.e(e);
                    mDeployFlow.onDeployError(new KCDeployError(e));
                }
                KCUtilFile.deleteRecyle(tmpZipFile);

                mDeployFlow.onComplete(aDek);

                return true;
            }
            mDeployFlow.onDeployError(new KCDeployError("zip file is null or the file is not exist"));
        }
        else
        {
            mDeployFlow.onDeployError(new KCDeployError(aSrcFile.getAbsolutePath()+": is not exists"));
            Log.e("decodeDek", aSrcFile.getAbsolutePath()+": is not exists");
        }

        return false;
    }

    public static boolean checkFileHash(File aFile, String hash)
    {
        String newHash = KCUtilMd5.md5sum(aFile);
        return !TextUtils.isEmpty(hash) && !TextUtils.isEmpty(newHash) && newHash.equalsIgnoreCase(hash);
    }

    protected boolean checkHtmlDir(Context context)
    {
        File file = new File(getRootPath() + "/html");
        return file.exists();
    }
}
