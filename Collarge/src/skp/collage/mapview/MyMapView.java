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
	
	ExifInterface exif;
	private int latitude;
	private int longitude;
	
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
		
		//���� GEO �±� ã��
		try {
			exif = new ExifInterface(imagePath.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// ƼŸ���� �߽�����...
		GeoPoint point = new GeoPoint(37566417,126985133);
		GeoPoint basePoint = new GeoPoint(getLatitude(exif), getLongitude(exif));
		MapController mapController = mapView.getController();
		mapController.setCenter(point);
		mapController.setZoom(17);
		
		drawable = getResources().getDrawable(R.drawable.bubble_point);
		itemizedOverlay = new MyItemizedOverlay(drawable, mapView);
		
		OverlayItem overlayItem = new OverlayItem(basePoint, "Tomorrow Never Dies (1997)", "(M gives Bond his mission in Daimler car)");
		itemizedOverlay.addOverlay(overlayItem);
		mapOverlays.add(itemizedOverlay);
		
		
		/* 
		 * �� ��ġ�� ã�� �ʹٸ� �� �ڵ带 �츮�� 
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
	 * ��ħ�� �����ϴ� �κ�
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

	// ���� ��ġ�� ǥ�����ִ�-
	class MyLocationOverlay2 extends MyLocationOverlay {
        public MyLocationOverlay2(Context context, MapView mapView) {
            super(context, mapView);
        }
   }
   */

}
