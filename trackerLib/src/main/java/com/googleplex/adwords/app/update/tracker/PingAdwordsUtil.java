package com.googleplex.adwords.app.update.tracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.googleplex.adwords.app.update.tracker.Constants.*;
/*
 * Copyright (c) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

public class PingAdwordsUtil
{
    public static void onResumePingIfAppUpdate(final Context context, final String conversionId, final String label)
    {
        SharedPreferences mPrefs = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);

        final String referer = mPrefs.getString(REFF_PREF, null);

        int oldAppVersion = mPrefs.getInt(APP_UPD_PREF, 0);
        int newAppversion = getAppVersion(context);

        if (oldAppVersion < newAppversion)
        {
            mPrefs.edit().putInt(APP_UPD_PREF, newAppversion);

            AsyncTask.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    ConversionParameters params = makeConversionParameters(context, conversionId, label, referer);
                    sendAdwordsPing(params);
                }
            });
        }
    }

    public static void recordRefererOnDeepLinkCall(Context context, Intent intent)
    {
        if (context == null || intent == null || intent.getData() == null) return;

        Uri uri = intent.getData();
        String referer = uri.getQueryParameter("referrer");
        if (referer == null || referer.trim().equals("")) return;

        SharedPreferences mPrefs = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        mPrefs.edit().putString(REFF_PREF, referer);
        mPrefs.edit().putInt(APP_UPD_PREF, getAppVersion(context));
    }

    static ConversionParameters makeConversionParameters(Context context, String conversionId, String label, String referer)
    {
        String adid = null;
        String bundleId = context.getPackageName();
        String appVersion = getAppVersion(context) > 0 ? ""+getAppVersion(context) : null;
        String osversion = Build.VERSION.RELEASE;
        String sdkversion = "" + Build.VERSION.SDK_INT;

        try
        {
            adid = AdvertisingIdClient.getAdvertisingIdInfo(context).getId();
        }
        catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e)
        {
            //do nothing
        }

        try
        {
            appVersion = ""+context.getPackageManager().getPackageInfo(bundleId, 0).versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.w(LOG_TAG, "Error to retrieve app version", e);
        }

        ConversionParameters params = new ConversionParameters(conversionId, label);
        params.setRefferer(referer);
        params.setBundleId(bundleId);
        params.setSdkversion(sdkversion);
        params.setAppVersion(appVersion);
        params.setAdid(adid);
        params.setOsversion(osversion);

        return params;
    }

    static int getAppVersion(Context context)
    {
        try
        {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.w(LOG_TAG, "Error to retrieve app version", e);
            return 0;
        }
    }

    static int sendAdwordsPing(ConversionParameters params)
    {
        try
        {
            String stringUrl = params.toString();
            Log.i(LOG_TAG, "Calling Ads Url:: "+stringUrl);
            if (stringUrl == null) return 0;

            HttpURLConnection connection = null;
            URL url = new URL(stringUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(HTTP_TIMEOUT_IN_MILLIS);
            connection.setReadTimeout(HTTP_TIMEOUT_IN_MILLIS);
            connection.setUseCaches(false);

            int responseCode = connection.getResponseCode();
            Log.i(LOG_TAG, "Response Status Code: "+responseCode);
            return responseCode;
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Error in calling ads ping", e);
            return -1;
        }
    }
}
