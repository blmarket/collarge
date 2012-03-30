package skp.collage.mapview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import skp.collarge.R;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MyMapView extends MapActivity {

	private ArrayList<String> imagePathList = new ArrayList<String>();
	private MapView mapView;
	
	ExifInterface exif, exif2;
	private int latitude;
	private int longitude;
	private Boolean buttonAction=true; // true 면 on, false 면 off
	
	Drawable drawable;
	MyItemizedOverlay itemizedOverlay;
	BalloonOverlayView balloonView;
	List<Overlay> mapOverlays;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_mapview);
		
		mapView = (MapView)findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapOverlays = mapView.getOverlays();
		imagePathList.add("/mnt/sdcard/DCIM/Camera/20120329_123720.jpg");
		imagePathList.add("/mnt/sdcard/DCIM/Camera/20120329_125341.jpg");
	
		//사진 GEO 태그 찾음
		try {
			exif = new ExifInterface("/mnt/sdcard/DCIM/Camera/20120329_123720.jpg");
			exif2 = new ExifInterface("/mnt/sdcard/DCIM/Camera/20120329_125341.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 티타워를 중심으로...
		GeoPoint point = new GeoPoint(37566417,126985133);
		MapController mapController = mapView.getController();
		mapController.setCenter(point);
		mapController.setZoom(17);
		
		drawable = getResources().getDrawable(R.drawable.bubble_point);
		itemizedOverlay = new MyItemizedOverlay(drawable, mapView);
		
		// 1. 마커(말풍선) 생성
		GeoPoint basePoint = new GeoPoint(getLatitude(exif), getLongitude(exif));
		OverlayItem overlayItem = new OverlayItem(basePoint, "", "");
		itemizedOverlay.addOverlay(overlayItem);
		
		GeoPoint basePoint2 = new GeoPoint(getLatitude(exif2), getLongitude(exif2));
		OverlayItem overlayItem2 = new OverlayItem(basePoint2, "", "");
		itemizedOverlay.addOverlay(overlayItem2);		
		
		// 2 .itemizedOverlay 순회하면서 마커(말풍선) 띄우기
		for(int i=0;i<itemizedOverlay.size();i++) {
			itemizedOverlay.onTap(i);
		}
		
		// 3. mapView의 Overlays에 추가
		mapOverlays.add(itemizedOverlay);
		
		final ImageView markerControlButton = (ImageView) findViewById(R.id.balloon_control);
		markerControlButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if(buttonAction) {
					for (int i = 0; i < itemizedOverlay.size(); i++) {
						itemizedOverlay.closeBalloon();
						markerControlButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_on));
						buttonAction = false;
					}
				} else {
					for (int i = 0; i < itemizedOverlay.size(); i++) {
						itemizedOverlay.onBalloonOverlay();
						markerControlButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_off));
						buttonAction = true;
					}
				}
			}
		});
				
				/* 
		 * 내 위치를 찾고 싶다면 이 코드를 살리면 
		mLocation = new MyLocationOverlay2(this, mapView);
		mapOverlays.add(mLocation);
		mLocation.runOnFirstFix(new Runnable() {
            public void run() {
                 mapView.getController().animateTo(mLocation.getMyLocation());
            }
        });
        */
		
	}
	
	private int getLatitude(ExifInterface exif) {
		latitude = Integer.parseInt(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE).split("/")[0]);
		return latitude/10;
	}
	
	private int getLongitude(ExifInterface exif) {
		longitude = Integer.parseInt(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE).split("/")[0]);
		return longitude/10;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}


	/*
	 * 나침반 실행하는 부분
	@Override
	protected void onPause() {
		super.onPause();
		//mLocation.disableMyLocation();
        //mLocation.disableCompass();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//mLocation.enableMyLocation();
        //mLocation.enableCompass();
	}

	// 나의 위치를 표시해주는-
	class MyLocationOverlay2 extends MyLocationOverlay {
        public MyLocationOverlay2(Context context, MapView mapView) {
            super(context, mapView);
        }
   }
   */

}
