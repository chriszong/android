package com.zone17s.android.maplib.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationGmsUtil implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private Context           mContext;
    private LocationClient    mLocationClient;
    private LocationRequest   REQUEST;
    private ILocationListener mGetLocationListener;

    public LocationGmsUtil(Context context, ILocationListener locationListener) {
        mContext = context;
        mGetLocationListener = locationListener;
        REQUEST = LocationRequest.create().setInterval(5000) // 5 seconds
        .setFastestInterval(16) // 16ms = 60fps
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void connect() {
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
    }

    public void disconnect() {
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }

    }

    private void setUpLocationClientIfNeeded() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(mContext, //
                                                 this, // ConnectionCallbacks
                                                 this); // OnConnectionFailedListener
        }
    }

    public void setGeoFence() {
        // mLocationClient.addGeofences(geofences, pendingIntent, listener);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do nothing
    }

    @Override
    public void onConnected(Bundle arg0) {
        mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
    }

    @Override
    public void onDisconnected() {
        // Do nothing
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if (mGetLocationListener != null) {
            mGetLocationListener.getLocation(location);
        }
    }

}
