package skp.collage.mapview;

import java.io.File;
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
	
	List<Overlay> overLayList;
	Drawable drawable;
	MyOverlay myOverlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_mapview);
		
		mapView = (MapView)findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		File imageFile = new File(imagePath);
		try {
			exif = new ExifInterface(imagePath.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GeoPoint point = new GeoPoint(getLatitude(exif), getLongitude(exif));
		MapController mapController = mapView.getController();
		mapController.animateTo(point);
		mapController.setZoom(17);
		
		overLayList = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.bubble_back);
		myOverlay = new MyOverlay(drawable);
		OverlayItem overlayItem = new OverlayItem(point, "", "");
		myOverlay.addOverlay(overlayItem);
		overLayList.add(myOverlay);
		
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

}
