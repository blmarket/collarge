package skp.collage.mapview;

import java.io.IOException;
import java.util.List;

import skp.collarge.R;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MyMapView extends MapActivity {

	private String imagePath = "/mnt/sdcard/DCIM/Camera/20120328_151059.jpg";
	private MapView mapView;
	//MyLocationOverlay2 mLocation;
	
	ExifInterface exif;
	private int latitude;
	private int longitude;
	
	Drawable drawable;
	MyOverlay myOverlay;
	MyItemizedOverlay itemizedOverlay;
	BalloonOverlayView balloonView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_mapview);
		
		mapView = (MapView)findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		List<Overlay> mapOverlays = mapView.getOverlays();
		
		try {
			exif = new ExifInterface(imagePath.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// 티타워를 중심으로...
		GeoPoint point = new GeoPoint(37566417,126985133);
		GeoPoint point2 = new GeoPoint(getLatitude(exif), getLongitude(exif));
		MapController mapController = mapView.getController();
		mapController.setCenter(point);
		mapController.setZoom(17);
		
		
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
		
		
		drawable = getResources().getDrawable(R.drawable.bubblepoint);
		itemizedOverlay = new MyItemizedOverlay(drawable, mapView);
		OverlayItem overlayItem = new OverlayItem(point2, "", "");
		itemizedOverlay.addOverlay(overlayItem);
		mapOverlays.add(itemizedOverlay);
		
		
		/*
		mapOverlays = mapView.getOverlays();
		drawable = getResources().getDrawable(R.drawable.bubblepoint);
		itemizedOverlay = new MyItemizedOverlay(drawable, mapView);
		OverlayItem overlayItem = new OverlayItem(point, "", "");
		itemizedOverlay.addOverlay(overlayItem);
		//mapOverlays.add(itemizedOverlay);
		
		try {
			balloonView = new BalloonOverlayView(mapView.getContext());
			balloonView.setVisibility(View.GONE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MapView.LayoutParams lp;
		lp = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT, MapView.LayoutParams.WRAP_CONTENT,
				10, 10, MapView.LayoutParams.TOP_LEFT);
		mapView.addView(balloonView, lp);
		
		
		
		overLayList = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.bubblepoint);
		myOverlay = new MyOverlay(drawable);
		OverlayItem overlayItem = new OverlayItem(point, "", "");
		myOverlay.addOverlay(overlayItem);
		overLayList.add(myOverlay);
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
