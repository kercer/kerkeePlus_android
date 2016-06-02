package com.kercer.kerkeesdk.deploy;

import android.content.Context;

import com.kercer.kercore.debug.KCLog;
import com.kercer.kercore.io.KCAssetTool;
import com.kercer.kercore.task.KCTaskExecutor;
import com.kercer.kercore.io.KCUtilFile;

import java.io.File;

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
        catch (Exception e)
        {
            KCLog.e("DekAssistant", "KCDeploy: h5 copy failed...");
            KCLog.e(e);
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
        File htmlDir = new File(mDeploy.getResRootPath());
        KCDek dek = new KCDek(htmlDir);
        dek.mIsFromAssert = true;
//        KCWebApp webapp = new KCWebApp(0, htmlDir, null);
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
