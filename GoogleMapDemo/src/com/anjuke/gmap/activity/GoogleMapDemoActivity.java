package com.anjuke.gmap.activity;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;

import com.anjuke.gmap.R;
import com.anjuke.gmap.mapoverlay.LocationsItemizedOverlay;
import com.anjuke.gmap.mapoverlay.MyLocationMarkOverlay;
import com.anjuke.gmap.mapoverlay.RouteOverLay;
import com.anjuke.gmap.model.StaticValue;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class GoogleMapDemoActivity extends MapActivity implements OnClickListener {

    private MapView mMapView;
    private Button mBtnFix;
    private Button mBtnMark;
    private MapController mMapController;
    private Boolean mIfRoute = false;

    private LocationsItemizedOverlay mItemizedoverlay;
    private MyLocationMarkOverlay mMyLocationMarkOverlay;
    private RouteOverLay mOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_google_map);

        initMapView();
        initBtns();
    }

    @Override
    protected void onResume() {
        if (StaticValue.sIfMapMarkerEditModel) {
            mBtnMark.setText("Done");
        } else {
            mBtnMark.setText("Mark");
        }
        super.onResume();
    }

    protected void onPause() {
        mItemizedoverlay.hidPopWindow();
        mItemizedoverlay.hidPopMarkView();
        super.onPause();
    }

    private void initBtns() {
        mBtnFix = (Button) findViewById(R.id.activity_google_map_btn_location);
        mBtnMark = (Button) findViewById(R.id.activity_google_map_btn_add_mark);

        mBtnFix.setOnClickListener(this);
        mBtnMark.setOnClickListener(this);
    }

    private void initMapView() {
        ViewStub mapViewStub = (ViewStub) this.findViewById(R.id.activity_google_map_vs_gmap);
        mapViewStub.setLayoutResource(R.layout.view_google_map_debug);
        mMapView = (MapView) mapViewStub.inflate();

        mMapView.setClickable(true);
        mMapView.setSatellite(false);
        mMapView.setBuiltInZoomControls(true);

        GeoPoint geoPoint = new GeoPoint((int) (31.2350 * 1000000), (int) (121.5016 * 1000000));

        mMapController = mMapView.getController();
        mMapController.setCenter(geoPoint);
        mMapController.setZoom(17);

        Drawable drawable = this.getResources().getDrawable(R.drawable.marked);
        mMyLocationMarkOverlay = new MyLocationMarkOverlay(GoogleMapDemoActivity.this, mMapView);
        mItemizedoverlay = new LocationsItemizedOverlay(drawable, GoogleMapDemoActivity.this, mMapView, mMyLocationMarkOverlay);

        mMapView.getOverlays().add(mItemizedoverlay);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.activity_google_map_btn_add_mark:

            if (StaticValue.sIfMapMarkerEditModel) {
                StaticValue.sIfMapMarkerEditModel = false;
                mBtnMark.setText("Mark");
                if (StaticValue.sMarkerGeo != null) {
                    if (StaticValue.sListGeo == null) {
                        StaticValue.sListGeo = new ArrayList<GeoPoint>();
                    }
                    StaticValue.sListGeo.add(StaticValue.sMarkerGeo);
                    mItemizedoverlay.loadNewData();
                }

                mMapView.getOverlays().remove(mMyLocationMarkOverlay);

            } else {
                StaticValue.sIfMapMarkerEditModel = true;
                mBtnMark.setText("Done");

                mMapView.getOverlays().add(mMyLocationMarkOverlay);
            }

            break;
        case R.id.activity_google_map_btn_location:
            mIfRoute = !mIfRoute;
            if (mIfRoute) {
                mBtnFix.setText("End");
                if (StaticValue.sListGeo == null) {
                    StaticValue.sListGeo = new ArrayList<GeoPoint>();
                }
                mOverlay = new RouteOverLay(StaticValue.sListGeo);
                mMapView.getOverlays().add(mOverlay);
                mMapView.invalidate();
            } else {
                mBtnFix.setText("Route");
                mMapView.getOverlays().remove(mOverlay);
                mMapView.invalidate();
            }
            break;

        default:
            break;
        }
    }
}