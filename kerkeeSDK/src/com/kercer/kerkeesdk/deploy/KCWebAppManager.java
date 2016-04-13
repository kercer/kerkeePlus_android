package com.kercer.kerkeesdk.deploy;

import android.content.Context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zihong on 16/3/14.
 */
public class KCWebAppManager
{

    private KCDeploy mDeploy = null;
    private KCDeployAssert mDeployAssert = null;
    private KCDeployUpgrade mDeployUpgrade = null;
    private Map<Integer, KCWebApp> mWebApps = new HashMap<Integer, KCWebApp>();

    public KCWebAppManager(Context aContext, KCDeployFlow aDeployFlow)
    {
        setup(aContext,KCDeployAssert.kDefaultAssetFileName, aDeployFlow);
    }

    public KCWebAppManager(Context aContext, String aAssetFileName, KCDeployFlow aDeployFlow)
    {
        setup(aContext, aAssetFileName, aDeployFlow);
    }

    private synchronized void setup(Context aContext, String aAssetFileName, KCDeployFlow aDeployFlow)
    {
        if (mDeploy == null)
            mDeploy = new KCDeploy(aContext, aDeployFlow);

        if (mDeployAssert == null)
        {
            mDeployAssert = new KCDeployAssert(mDeploy);
            mDeployAssert.setAssetFileName(aAssetFileName);
        }

        if (mDeployUpgrade == null)
            mDeployUpgrade = new KCDeployUpgrade(mDeploy);

        //upgrade from Assert if app is first lauch after version changed and local has not html dir
        //don't compare RequiredVersion
        if (mDeploy.getMainBundle().isFirstLaunchAfterVersionChanged() || !mDeploy.checkHtmlDir(aContext))
        {
            mDeployAssert.deployFromAssert(aContext);
        }
    }

    public synchronized void addWebApps(Collection<KCWebApp> aWebApps)
    {
        Iterator iterator = aWebApps.iterator();
        while (iterator.hasNext())
        {
            KCWebApp webapp = (KCWebApp) iterator.next();
            addWebApp(webapp);
        }
    }
    public synchronized boolean addWebApp(KCWebApp aWebApp)
    {
        if (aWebApp == null) return false;
        mWebApps.put(aWebApp.mID, aWebApp);
        return true;
    }


    public void setManifestFileName(String aManifestFileName)
    {
        if (mDeployUpgrade != null)
            mDeployUpgrade.setManifestFileName(aManifestFileName);
    }

    public void upgradeWebApps(Collection<KCWebApp> aWebApps)
    {
        if (mDeployUpgrade != null)
            mDeployUpgrade.upgradeWebApps(aWebApps);
    }

    public void upgradeWebApp(final KCWebApp aWebApp)
    {
        if (mDeployUpgrade != null)
            mDeployUpgrade.upgradeWebApp(aWebApp);
    }


}
