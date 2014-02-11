package com.zone17s.android.maplib;

import android.app.Application;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class maplibApp extends Application {

    // if google play service enable
    private boolean ifGPSEnable = false;

    @Override
    public void onCreate() {
        super.onCreate();
        ifGPSEnable = checkGooglePlayServiceEnable();
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        // TODO Auto-generated method stub
        super.onTrimMemory(level);
    }

    private boolean checkGooglePlayServiceEnable() {
        int GPS_Status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        // SUCCESS,
        // SERVICE_MISSING,
        // SERVICE_VERSION_UPDATE_REQUIRED, SERVICE_DISABLED,
        // SERVICE_INVALID, DATE_INVALID
        switch (GPS_Status) {
            case ConnectionResult.SUCCESS:
                return true;
            default:
                return false;
        }
    }

    public boolean isIfGPSEnable() {
        return ifGPSEnable;
    }

}
