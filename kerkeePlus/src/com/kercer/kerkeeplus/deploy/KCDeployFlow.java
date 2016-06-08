package com.kercer.kerkeeplus.deploy;

import java.io.File;

/**
 * Created by zihong on 16/3/16.
 */
public interface KCDeployFlow
{
    File decodeFile(File aSrcFile, KCDek aDek);
    void onComplete(KCDek aDek);
    void onDeployError(KCDeployError aError, KCDek aDek);
}
