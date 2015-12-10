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
package com.googleplex.adwords.app.update.tracker;

import android.net.Uri;
import android.util.Log;

import static com.googleplex.adwords.app.update.tracker.Constants.*;

public class ConversionParameters
{
    private String conversionId;
    private String label;
    private String adid;
    private String bundleId;
    private String appVersion;
    private String osversion;
    private String sdkversion;
    private String refferer;

    public ConversionParameters(String conversionId, String label)
    {
        this.conversionId = conversionId;
        this.label = label;
    }

    public String getConversionId() {
        return conversionId;
    }

    public String getLabel() {
        return label;
    }

    public String getAdid() {
        return adid;
    }

    public void setAdid(String adid) {
        this.adid = adid;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getOsversion() {
        return osversion;
    }

    public void setOsversion(String osversion) {
        this.osversion = osversion;
    }

    public String getSdkversion() {
        return sdkversion;
    }

    public void setSdkversion(String sdkversion) {
        this.sdkversion = sdkversion;
    }

    public String getRefferer() {
        return refferer;
    }

    public void setRefferer(String refferer) {
        this.refferer = refferer;
    }

        /*
        public static String adwordsURL = "https://www.googleadservices.com/pagead/conversion/%s/?label=%s" +
            "&rdid=%s&idtype=advertisingid&lat=0&bundleid=%s&appversion=%s&osversion=%s&sdkversion=%s&referrer=%s";
         */


    @Override
    public String toString()
    {
        if(conversionId == null || label == null)
        {
            Log.i(LOG_TAG, "Null conversionId or Label");
            return null;
        }

        Uri.Builder awUri = Uri.parse(ADWORD_URL).buildUpon();

        awUri.appendPath(conversionId).appendQueryParameter("label", label);

        if (adid != null) awUri.appendQueryParameter("rdid", adid).appendQueryParameter("idtype", "advertisingid");
        if (bundleId != null) awUri.appendQueryParameter("bundleid", bundleId);
        if (appVersion != null) awUri.appendQueryParameter("appversion", appVersion);
        if (osversion != null) awUri.appendQueryParameter("osversion", osversion);
        if (sdkversion != null) awUri.appendQueryParameter("sdkversion", sdkversion);
        if (refferer != null) awUri.appendQueryParameter("referrer", refferer);

        return awUri.build().toString();
    }
}