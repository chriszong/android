package com.anjuke.gmap.mapoverlay;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.anjuke.gmap.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.google.android.maps.MapView.LayoutParams;

public class AnimationRouteOverlay extends Overlay {

    private Context mContext;
    private MapView mMapView;
    private List<GeoPoint> points;

    private View mMarkerView;

    public AnimationRouteOverlay(Context context, MapView mapView, List<GeoPoint> points) {
        mContext = context;
        mMapView = mapView;
        this.points = points;

        mMarkerView = View.inflate(mContext, R.layout.view_map_marker, null);
        mMarkerView.setVisibility(View.GONE);
        mMapView.addView(mMarkerView);

        Projection projection = mapView.getProjection();

        if (points != null && points.size() >= 2) {
            Point start = new Point();
            projection.toPixels(points.get(0), start);
            AnimationSet clickAnimationSet = new AnimationSet(true);
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.marking);
            int width = mMapView.getWidth();

            for (int i = 1; i < points.size(); i++) {
                Point end = new Point();
                projection.toPixels(points.get(i), end);

                MapView.LayoutParams markerViewLayoutParams = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, points.get(i), 0, 0,
                        MapView.LayoutParams.BOTTOM_CENTER);
                markerViewLayoutParams.point = points.get(i);
                mMapView.updateViewLayout(mMarkerView, markerViewLayoutParams);
                mMarkerView.bringToFront();

                // TranslateAnimation placeOutTA = new TranslateAnimation(
                // //Animation.ABSOLUTE, Math.round(1.0F * (start.x - end.x) /
                // width),
                // Animation.ABSOLUTE, 1.0f,
                // Animation.ABSOLUTE, 0.0f,
                // //Animation.ABSOLUTE, Math.round(1.0F * (start.y - end.y) /
                // width),
                // Animation.ABSOLUTE, 1.0f,
                // Animation.ABSOLUTE, 0.0f);

                TranslateAnimation placeOutTA = new TranslateAnimation(start.x - end.x, 0, start.y - end.y, 0);
                placeOutTA.setDuration(200);
                placeOutTA.setStartOffset(200 * (i - 1));
                clickAnimationSet.addAnimation(placeOutTA);

                start = end;
            }

            mMarkerView.startAnimation(clickAnimationSet);
            mMarkerView.setVisibility(View.VISIBLE);
        }

    }

    public void clearView() {
        mMapView.removeView(mMarkerView);
    }
}
