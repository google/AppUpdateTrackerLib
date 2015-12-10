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