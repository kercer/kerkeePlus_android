package com.kercer.kerkeeplus.deploy;

import android.content.Context;

import com.kercer.kercore.debug.KCLog;
import com.kercer.kercore.task.KCTaskExecutor;
import com.kercer.kercore.util.KCUtilText;
import com.kercer.kerdb.KCDB;
import com.kercer.kerdb.KerDB;
import com.kercer.kerdb.jnibridge.KCIterator;
import com.kercer.kerdb.jnibridge.KCSnapshot;
import com.kercer.kerdb.jnibridge.exception.KCDBException;
import com.kercer.kernet.uri.KCURI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
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
    private KCDeployInstall mDeployInstall = null;
    private Map<Integer, KCWebApp> mWebApps = new HashMap<Integer, KCWebApp>();
    private final static String kDBName = "WebappsDB";
    KCDB mDB;

    public KCWebAppManager(Context aContext, KCDeployFlow aDeployFlow)
    {
        setup(aContext, KCDeployAssert.kDefaultAssetFileName, aDeployFlow);
    }

    public KCWebAppManager(Context aContext, String aAssetFileName, KCDeployFlow aDeployFlow)
    {
        setup(aContext, aAssetFileName, aDeployFlow);
    }

    private synchronized void setup(final Context aContext, String aAssetFileName, KCDeployFlow aDeployFlow)
    {
        if (mDeploy == null) mDeploy = new KCDeploy(aContext, aDeployFlow);
        if (mDeployAssert == null)
        {
            mDeployAssert = new KCDeployAssert(mDeploy);
            mDeployAssert.setAssetFileName(aAssetFileName);
        }
        if (mDeployInstall == null) mDeployInstall = new KCDeployInstall(mDeploy);


        try
        {
            mDB = KerDB.open(aContext, kDBName);
        }
        catch (KCDBException e)
        {
            KCLog.e(e);
        }

        loadWebAppsFromDB(aContext);

        //upgrade from Assert if app is first lauch after version changed and local has not html dir
        //don't compare RequiredVersion
        if (mDeploy.getMainBundle().isFirstLaunchAfterVersionChanged() || !mDeploy.checkHtmlDir(aContext))
        {
            mDeployAssert.deployFromAssert(aContext);
            loadWebappsCfg();
        }

    }

    private static String readFileText(File path)
    {
        String content = "";
        if (path.isDirectory())
        {
            KCLog.d("kerkeePlus", "The File doesn't not exist.");
        }
        else
        {
            try
            {
                InputStream instream = new FileInputStream(path);
                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    while ((line = buffreader.readLine()) != null)
                    {
                        content += line + "\n";
                    }
                    instream.close();
                }
            }
            catch (java.io.FileNotFoundException e)
            {
                KCLog.d("kerkeePlus", "The File doesn't not exist.");
            }
            catch (IOException e)
            {
                KCLog.d("kerkeePlus", e.getMessage());
            }
        }
        return content;
    }

    private void loadWebappsCfg()
    {
        File webappsJsonPath = new File(mDeploy.getResRootPath()+"/webapps.json");
        String str = readFileText(webappsJsonPath);

        if (!KCUtilText.isEmpty(str))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(str);
                KCLog.d(jsonObject.toString());
                JSONArray jsonWebapps = jsonObject.getJSONArray("webapps");
                int length = jsonWebapps.length();
                for (int i = 0; i<length; ++i)
                {
                    JSONObject jsonWebapp = jsonWebapps.getJSONObject(i);
                    int id = jsonWebapp.getInt("id");
                    String root = jsonWebapp.getString("root");
                    String manifestUrl = jsonWebapp.getString("manifestUrl");

                    if (id == 0) continue;

                    File fileRoot = new File(mDeploy.getResRootPath());
                    if (!KCUtilText.isEmpty(root))
                    {
                        String rootPath = root.replace("./", KCUtilText.EMPTY_STR);
                        fileRoot = new File(fileRoot+File.separator+rootPath);
                    }
                    KCURI manifestUri = null;
                    if (!KCUtilText.isEmpty(manifestUrl))
                    {
                        manifestUri = KCURI.parse(manifestUrl);
                    }

                    final KCWebApp webApp = new KCWebApp(id,fileRoot, manifestUri);
                    addWebApp(webApp);
                }
            }
            catch (JSONException e)
            {
                KCLog.e(e);
            }
            catch (URISyntaxException e1)
            {
                KCLog.e(e1);
            }
            catch (Exception exc)
            {
                KCLog.e(exc);
            }
        }
    }

    private void loadWebAppsFromDB(Context aContext)
    {
        try
        {
            KCSnapshot snapshot = mDB.createSnapshot();
            KCIterator iterator = mDB.iterator();
            for (iterator.seekToFirst(); iterator.isValid(); iterator.next())
            {
//                String key = new String(iterator.getKey());
//                String value = new String(iterator.getValue());
                KCWebApp webApp =  KCWebApp.webApp(iterator.getValue());
                mWebApps.put(webApp.mID, webApp);
            }
            iterator.close();
            snapshot.close();

        }
        catch (KCDBException e)
        {
            KCLog.e(e);
        }
        catch (Exception e)
        {
            KCLog.e(e);
        }
    }

    private void updateToDB(KCWebApp aWebApp)
    {
        try
        {
//            mDB.putString(String.valueOf(aWebApp.getID()), aWebApp.toString());
            mDB.putDBObject(String.valueOf(aWebApp.getID()), aWebApp);
        }
        catch (KCDBException e)
        {
            KCLog.e(e);
        }
    }

    private void updateToDBAsyn(final KCWebApp aWebApp)
    {
        KCTaskExecutor.executeTask(new Runnable()
        {
            @Override
            public void run()
            {
                updateToDB(aWebApp);
            }
        });
    }

    public synchronized void addWebApps(Collection<KCWebApp> aWebApps)
    {
        Iterator iterator = aWebApps.iterator();
        while (iterator.hasNext())
        {
            KCWebApp webapp = (KCWebApp) iterator.next();
            addWebApp(webapp);
            updateToDBAsyn(webapp);
        }
    }

    public synchronized boolean addWebApp(KCWebApp aWebApp)
    {
        if (aWebApp == null) return false;
        mWebApps.put(aWebApp.mID, aWebApp);
        updateToDBAsyn(aWebApp);
        return true;
    }


    public void setManifestFileName(String aManifestFileName)
    {
        if (mDeployInstall != null) mDeployInstall.setManifestFileName(aManifestFileName);
    }

    public void upgradeWebApps(Collection<KCWebApp> aWebApps)
    {
        if (mDeployInstall != null) mDeployInstall.installWebApps(aWebApps);
    }

    public void upgradeWebApp(final KCWebApp aWebApp)
    {
        if (mDeployInstall != null) mDeployInstall.installWebApp(aWebApp);
    }

    @Override
    protected void finalize() throws Throwable
    {
        if (mDB != null) mDB.close();
    }

}
