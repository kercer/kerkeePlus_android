package com.kercer.kerkeesdk.deploy;

import com.kercer.kercore.io.KCUtilFile;

import java.io.File;

/**
 * Created by zihong on 16/3/18.
 */
public class KCDeployFlowDefault implements KCDeployFlow
{
    @Override
    public File decodeFile(File aSrcFile)
    {
        File dirPath = KCUtilFile.getPathWithoutFilename(aSrcFile);
        File tmpZipFile = new File(dirPath, "tmp.zip");
        KCUtilFile.rename(aSrcFile, tmpZipFile);
        return tmpZipFile;
    }

    @Override
    public void onComplete(KCDek aDek)
    {
    }

    @Override
    public void onDeployError(KCDeployError aError)
    {
    }

}
