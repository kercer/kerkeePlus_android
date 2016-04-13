package com.kercer.kerkeesdk.deploy;

import android.content.Context;

import com.kercer.kercore.debug.KCLog;
import com.kercer.kercore.io.KCAssetTool;
import com.kercer.kercore.task.KCTaskExecutor;
import com.kercer.kerkee.util.KCUtilFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by zihong on 16/3/18.
 */
public class KCDeployAssert
{
    public static String kDefaultAssetFileName = "html.dek";
    private String mAssetFileName = kDefaultAssetFileName;
    private KCDeploy mDeploy;

    public KCDeployAssert(KCDeploy aDeploy)
    {
        mDeploy = aDeploy;
    }

    private File copyAssetDekFile(Context aContext)
    {
        KCAssetTool assetTool = new KCAssetTool(aContext);
        File tmpDesFile = new File(mDeploy.getRootPath() + "/" + mAssetFileName);
        try
        {
            File fileDir = new File(mDeploy.getRootPath());
            KCUtilFile.deleteRecyle(fileDir);
            assetTool.createDir(fileDir);
            assetTool.copyAssetFile(mAssetFileName, tmpDesFile.getAbsolutePath());
            KCLog.e("KCDeploy", "KCDeploy: h5 copy end...");
        }
        catch (IOException e)
        {
            KCLog.e("DekAssistant", "KCDeploy: h5 copy failed...");
            e.printStackTrace();
        }
        return tmpDesFile;
    }

    public void setAssetFileName(String aAssetFileName)
    {
        mAssetFileName = aAssetFileName;
    }

    public String getAssetFileName()
    {
        return mAssetFileName;
    }

    public boolean deployFromAssert(Context aContext)
    {
        final File srcFile = copyAssetDekFile(aContext);
        KCDek dek = new KCDek();
        File htmlDir = new File(mDeploy.getResRootPath());
        dek.mRootPath = htmlDir;
        KCWebApp webapp = new KCWebApp();
        webapp.mID = 0;
        webapp.mRootPath = htmlDir;
        boolean isOk = mDeploy.deploy(srcFile, dek);
        KCTaskExecutor.executeTask(new Runnable()
        {
            @Override
            public void run()
            {
                KCUtilFile.deleteRecyle(srcFile);
            }
        });
        return isOk;
    }
}
