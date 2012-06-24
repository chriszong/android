package com.anjuke.gmap.mapoverlay;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.anjuke.gmap.R;
import com.anjuke.gmap.model.StaticValue;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.LayoutParams;
import com.google.android.maps.Overlay;

public class MyLocationMarkOverlay extends Overlay {

    private Context mContext;
    private MapView mMapView;
    private View mMarkerView;

    public MyLocationMarkOverlay(Context context, MapView mapView) {
        mContext = context;
        mMapView = mapView;

        mMarkerView = View.inflate(mContext, R.layout.view_map_marker, null);
        mMarkerView.setVisibility(View.GONE);
        mMapView.addView(mMarkerView);
    }

    @Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
        super.draw(canvas, mapView, shadow);
        return true;
    }

    @Override
    public boolean onTap(GeoPoint p, MapView mapView) {
        System.out.println("x:" + p.getLatitudeE6());
        System.out.println("y:" + p.getLongitudeE6());

        if (StaticValue.sIfMapMarkerEditModel) {
            GeoPoint tapGeoPoint = new GeoPoint(p.getLatitudeE6(), p.getLongitudeE6());
            StaticValue.sMarkerGeo = tapGeoPoint;

            MapView.LayoutParams markerViewLayoutParams = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tapGeoPoint, 0, 0, MapView.LayoutParams.BOTTOM_CENTER);
            markerViewLayoutParams.point = tapGeoPoint;

            mMapView.updateViewLayout(mMarkerView, markerViewLayoutParams);
            mMarkerView.bringToFront();
            mMarkerView.setVisibility(View.VISIBLE);
        }
        return true;
    }

    public void hideMarkerView() {
        mMarkerView.setVisibility(View.GONE);
    }

}
