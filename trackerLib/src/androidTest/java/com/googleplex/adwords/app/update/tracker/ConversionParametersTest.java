package com.googleplex.adwords.app.update.tracker;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ConversionParametersTest extends TestCase {

    public void testToString() throws Exception {
        ConversionParameters params = new ConversionParameters("conversionId","label");
        params.setAdid("adid");
        params.setAppVersion("appVersion");
        params.setBundleId("bundleId");
        params.setSdkversion("sdkVersion");
        params.setRefferer("gclid=43423");

        String calcualtedUri = "https://www.googleadservices.com/pagead/conversion/conversionId?label=label&rdid=adid&idtype=advertisingid&bundleid=bundleId&appversion=appVersion&sdkversion=sdkVersion&referrer=gclid%3D43423";
        Assert.assertEquals(calcualtedUri, params.toString());

        int response = PingAdwordsUtil.sendAdwordsPing(params);
        Assert.assertEquals(200, response);
    }
}