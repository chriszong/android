package com.zone17s.android.maplib.data;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.zone17s.android.maplib.util.MathDataUtil;

public class DataMaker {

    private static final int DIGITAL_NUMBER = 6;

    public static LatLng createMapPoint(double latLowerLimit, double latUpperLimit, 
                                        double lngLowerLimit, double lngUpperLimit, 
                                        int pointNumber) {
        double lat = MathDataUtil.getRandomDou(latLowerLimit, latUpperLimit, DIGITAL_NUMBER);
        double lng = MathDataUtil.getRandomDou(lngLowerLimit, lngUpperLimit, DIGITAL_NUMBER);

        LatLng geoPoint = new LatLng(lat, lng);
        return geoPoint;
    }

    public static ArrayList<LatLng> createMapPointArray(double latLowerLimit, double latUpperLimit,
                                                        double lngLowerLimit, double lngUpperLimit, 
                                                        int pointNumber, int arrayNumber) {
        ArrayList<LatLng> points = new ArrayList<LatLng>();

        for (int i = 0; i < arrayNumber; i++) {
            double lat = MathDataUtil.getRandomDou(latLowerLimit, latUpperLimit, DIGITAL_NUMBER);
            double lng = MathDataUtil.getRandomDou(lngLowerLimit, lngUpperLimit, DIGITAL_NUMBER);
            LatLng geoPoint = new LatLng(lat, lng);
            points.add(geoPoint);
        }

        return points;
    }

}
