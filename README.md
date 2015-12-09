# UpdateTracker Android Library project

Project Goal
------------
Track App Updates attribution in AdWords.
 
Download
--------
Find latest version of complied library (.aar file) [download here](/trackerLib/dist/).  
 Right click on .aar file and select 'Save Link as...'   
 
Usage
-------
 Copy .aar to folder <android project>/app/libs  
 Also in **build.gradle** add following entry  
 
```
 repositories{  
     flatDir{  
         dirs 'libs'  
     }  
 }  
 dependencies {  
 ....  
     compile(name: 'AppUpdateTracker-release-1.0.1', ext: 'aar')  
     compile 'com.google.android.gms:play-services:8.3.0'  
 ....  
 }  
```
 
API Calls
---------
It has two static api methods:

1. PingAdwordsUtil.recordRefererOnDeepLinkCall(...)  
   It records referer (like gclid and other data).  
   Saves the same in SharedPreferences, which is later used in onResumePingIfAppUpdate(..) call.  
   <br/>

2. PingAdwordsUtil.onResumePingIfAppUpdate(...)  
   This method checks if app update has happened.   
   If so, pings adwords to report conversion.  
   It is intended to be called upon onResume(..) of parent apps main activity.
   
Also a utility android Activity 'DeepLinkTrackerActivity' is created.  
 Upon app update ad-click, if a deeplink is fired then DeepLinkTrackerActivity can be called.  
 It will store referer and forward call to playstore.  
 Later when PingAdwordsUtil.onResumePingIfAppUpdate(..) will be invoked, the referer will be used for adwords attribution.