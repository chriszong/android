package com.zone17s.android.maplib.location;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

public class locationAndroidUitl implements LocationListener {

    private Context           mContext;
    private ILocationListener mGetLocationListener;
    private LocationManager   mgr;

    public locationAndroidUitl(Context context, ILocationListener locationListener) {
        mContext = context;
        mGetLocationListener = locationListener;
        mgr = (LocationManager) mContext.getSystemService(Activity.LOCATION_SERVICE);
    }

    private LocationProvider getBestProvider() {
        Criteria criteria = new Criteria();
        // criteria不能填null，否则出现异常
        String providerName = mgr.getBestProvider(criteria, true /* enabledOnly */);
        LocationProvider provider = mgr.getProvider(providerName);
        return provider;
    }

    private List<String> getProviders() {
        List<String> list = mgr.getAllProviders();
        for (Iterator<String> i = list.iterator(); i.hasNext();) {
            System.out.println("\t" + i.next());
        }

        return list;
    }

    public void stop() {
        mgr.removeUpdates(this);
    }

    public void requestUpdate() {
        LocationProvider provider = getBestProvider();
        mgr.requestLocationUpdates(provider.getName(), // <br>
                                   10000,/* 10秒，为测试方便 */
                                   1000,/* 1公里 */
                                   this);/* 位置监听器 */

    }

    public void setGeoFence() {
        // mgr.addProximityAlert(latitude, longitude, radius, expiration, intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if (mGetLocationListener != null) {
            mGetLocationListener.getLocation(location);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}
