package com.anjuke.gmap.mapoverlay;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.anjuke.gmap.R;
import com.anjuke.gmap.model.StaticValue;
import com.anjuke.gmap.util.CommonUtil;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.LayoutParams;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class LocationsItemizedOverlay extends ItemizedOverlay<OverlayItem> {

    private Context mContext;
    private MapView mMapView;

    /** 标点框 */
    private View mMarkerView;
    /** 标点详细内容浮框 */
    private View mPopView;
    private final static int POPWIDTH = 280;// 弹出窗宽
    private final static int POPHEIGHT = 80;// 弹出窗高
    private final static int MARKERHEIGHT = 50;// 弹出窗距离 标点高度
    private final static int POPPADDING = 10;// 弹出窗距离屏幕边框的padding

    private ArrayList<OverlayItem> mOverlayItemList = new ArrayList<OverlayItem>();
    private ArrayList<View> mBottomViewList = new ArrayList<View>();// 记录地图标点下方的view列表

    private Point mScreanP = new Point();
    private MyLocationMarkOverlay mMyLocationMarkOverlay;

    public LocationsItemizedOverlay(Drawable defaultMarker, Context context, MapView mapView, MyLocationMarkOverlay myLocationMarkOverlay) {
        super(boundCenterBottom(defaultMarker));
        // TODO Auto-generated constructor stub

        mContext = context;
        mMapView = mapView;
        mMyLocationMarkOverlay = myLocationMarkOverlay;

        mPopView = View.inflate(mContext, R.layout.view_map_marker_pop, null);
        mPopView.setVisibility(View.GONE);
        mMapView.addView(mPopView);

        mMarkerView = View.inflate(mContext, R.layout.view_map_marker_marked, null);
        mMarkerView.setVisibility(View.GONE);
        mMapView.addView(mMarkerView);

        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        // TODO Auto-generated method stub
        return mOverlayItemList.get(i);
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return mOverlayItemList.size();
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        // TODO Auto-generated method stub

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        for (int i = 0; i < mBottomViewList.size(); i++) {
            try {
                View tempView = mBottomViewList.get(i);

                Bitmap temp = tempView.getDrawingCache();

                OverlayItem overlayitem = (OverlayItem) tempView.getTag();
                GeoPoint geo = overlayitem.getPoint();
                Projection projettion = mMapView.getProjection();
                projettion.toPixels(geo, mScreanP);
                canvas.drawBitmap(temp, mScreanP.x - temp.getWidth() / 2, mScreanP.y - temp.getHeight(), paint);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        shadow = false;
        super.draw(canvas, mapView, shadow);
    }

    @Override
    public boolean onTap(GeoPoint p, MapView mapView) {
        boolean tapped = super.onTap(p, mapView);
        if (!tapped) {
            mPopView.setVisibility(View.GONE);
            mMarkerView.setVisibility(View.GONE);
        }

        return true;

    }

    @Override
    protected boolean onTap(int index) {
        OverlayItem item = mOverlayItemList.get(index);
        String strGeoData = item.getSnippet();
        String[] geodata = strGeoData.split(";");

        if (geodata.length == 3) {
            String slat = geodata[0];
            String slng = geodata[1];
            String markerText = geodata[2];

            try {
                int dlat = Integer.parseInt(slat);
                int dlng = Integer.parseInt(slng);
                GeoPoint geoPoint = new GeoPoint(dlat, dlng);

                MapView.LayoutParams popViewLayoutParams = new MapView.LayoutParams(CommonUtil.dip2px(mContext, POPWIDTH), CommonUtil.dip2px(mContext, POPHEIGHT), geoPoint,
                        MapView.LayoutParams.BOTTOM_CENTER);
                popViewLayoutParams.mode = MapView.LayoutParams.MODE_VIEW;// 使用屏幕坐标来显示view模式
                popViewLayoutParams.point = geoPoint;

                MapView.LayoutParams markerViewLayoutParams = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, geoPoint, 0, 0, MapView.LayoutParams.BOTTOM_CENTER);
                markerViewLayoutParams.point = geoPoint;

                setGeoLPxy(popViewLayoutParams);// 转化坐标 不超出屏幕
                mMapView.updateViewLayout(mPopView, popViewLayoutParams);
                mMapView.updateViewLayout(mMarkerView, markerViewLayoutParams);

                mMarkerView.bringToFront();
                mMarkerView.setVisibility(View.VISIBLE);

                TextView tvMarker = (TextView) mMarkerView.findViewById(R.id.view_map_marker_marked_tv_marker);
                tvMarker.setText(markerText);
                tvMarker.setBackgroundResource(R.drawable.marking);

                mPopView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "jump to detail page", Toast.LENGTH_SHORT).show();
                    }
                });

                mPopView.bringToFront();
                mPopView.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                // TODO: handle exception
            }

        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        int action = event.getAction();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            break;
        case MotionEvent.ACTION_MOVE:
            mPopView.setVisibility(View.GONE);
            break;
        case MotionEvent.ACTION_UP:
            break;
        }

        return false;
    }

    /**
     * 添加marker到地图图层
     */
    public void loadNewData() {
        AsynAddMarker asyn = new AsynAddMarker();
        asyn.execute(StaticValue.sMarkerGeo);
    }

    private class AsynAddMarker extends AsyncTask<GeoPoint, Void, GeoPoint> {

        @Override
        protected GeoPoint doInBackground(GeoPoint... params) {
            // TODO Auto-generated method stub
            return params[0];
        }

        protected void onPostExecute(GeoPoint geoPoint) {
            if (geoPoint != null) {
                String markerText = "M" + StaticValue.sListGeo.size();
                String geoData = String.valueOf(geoPoint.getLatitudeE6()) + ";" + String.valueOf(geoPoint.getLongitudeE6()) + ";" + markerText;
                OverlayItem overlayitem = new OverlayItem(geoPoint, null, geoData);
                addOverlay(overlayitem);

                View pointButtomView = View.inflate(mContext, R.layout.view_map_marker_marked, null);
                pointButtomView.setVisibility(View.VISIBLE);
                pointButtomView.setDrawingCacheEnabled(true);
                MapView.LayoutParams pointButtomLayout = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, geoPoint, 0, 0, MapView.LayoutParams.BOTTOM_CENTER);
                TextView tvMarker = (TextView) pointButtomView.findViewById(R.id.view_map_marker_marked_tv_marker);
                tvMarker.setText(markerText);
                pointButtomView.setBackgroundResource(R.drawable.marked);
                mMapView.addView(pointButtomView, pointButtomLayout);
                pointButtomView.setTag(overlayitem);
                mBottomViewList.add(pointButtomView);
                mMyLocationMarkOverlay.hideMarkerView();
            }
        }
    }

    private void setGeoLPxy(MapView.LayoutParams popViewLayoutParams) {
        // 转化
        Projection projettion = mMapView.getProjection();
        Point myp = new Point();
        projettion.toPixels(popViewLayoutParams.point, myp);

        int center_x_dip = CommonUtil.px2dip(mContext, myp.x);
        int center_y_dip = CommonUtil.px2dip(mContext, myp.y);
        // 图标偏移
        center_y_dip = center_y_dip - MARKERHEIGHT;

        int screen_width_pix = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        int screen_height_pix = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        int screen_width_dip = CommonUtil.px2dip(mContext, screen_width_pix);
        int screen_height_dip = CommonUtil.px2dip(mContext, screen_height_pix);
        int popView_width_dip = POPWIDTH;
        int popView_height_dip = POPHEIGHT;
        if (center_x_dip < popView_width_dip / 2) {
            center_x_dip = popView_width_dip / 2 + POPPADDING;
        }
        if (center_x_dip > (screen_width_dip - popView_width_dip / 2)) {
            center_x_dip = screen_width_dip - popView_width_dip / 2 - POPPADDING;
        }
        if (center_y_dip < popView_height_dip) {
            center_y_dip = popView_height_dip + POPPADDING;
        }
        if (center_y_dip > (screen_height_dip - popView_height_dip)) {
            center_y_dip = screen_height_dip - popView_height_dip - POPPADDING;
        }
        popViewLayoutParams.x = CommonUtil.dip2px(mContext, center_x_dip);
        popViewLayoutParams.y = CommonUtil.dip2px(mContext, center_y_dip);
    }

    public void removeViewBelowThePoint() {
        for (int i = 0; i < mBottomViewList.size(); i++) {
            View temp = mBottomViewList.get(i);
            mMapView.removeView(temp);
        }
        mBottomViewList.clear();
    }

    public void addOverlay(OverlayItem overlay) {
        mOverlayItemList.add(overlay);
        populate();
    }

    public void clearOverlay() {
        Iterator<OverlayItem> it = mOverlayItemList.iterator();
        while (it.hasNext()) {
            OverlayItem temp = it.next();
            it.remove();
            mOverlayItemList.remove(temp);
        }
        setLastFocusedIndex(-1);
        populate();
    }

    public void hidPopWindow() {
        mPopView.setVisibility(View.GONE);
    }

    public void hidPopMarkView() {
        mMarkerView.setVisibility(View.GONE);
    }

}
