package com.kercer.kerkeeplus.deploy;

import android.content.Context;

import com.kercer.kercore.debug.KCLog;
import com.kercer.kercore.io.KCAssetTool;
import com.kercer.kercore.task.KCTaskExecutor;
import com.kercer.kercore.io.KCUtilFile;
import com.kercer.kercore.util.KCUtilText;

import java.io.File;

/**
 * Created by zihong on 16/3/18.
 */
public class KCDeployAssert
{
    public static String kDefaultAssetFileName = "main.dek";
    private String mAssetFileName = kDefaultAssetFileName;
    private KCDeploy mDeploy;

    public KCDeployAssert(KCDeploy aDeploy)
    {
        mDeploy = aDeploy;
    }

    private File copyAssetDekFile(Context aContext, String aAssetFileName)
    {
        if (KCUtilText.isEmpty(aAssetFileName)) return null;
        KCAssetTool assetTool = new KCAssetTool(aContext);
        File tmpDesFile = new File(mDeploy.getRootPath() + "/" + aAssetFileName);
        try
        {
            File fileDir = new File(mDeploy.getRootPath());
            if (!fileDir.exists()) assetTool.createDir(fileDir);
            if (tmpDesFile.exists())
                KCUtilFile.deleteRecyle(tmpDesFile);
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
        final File srcFile = copyAssetDekFile(aContext, mAssetFileName);
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
