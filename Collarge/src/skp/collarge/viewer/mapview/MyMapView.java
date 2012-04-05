package skp.collarge.viewer.mapview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import skp.collarge.R;
import skp.collarge.event.EventManager;
import skp.collarge.event.IEvent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MyMapView extends MapActivity {

	private ArrayList<String> imagePathList = new ArrayList<String>();
	private ArrayList<ExifInterface> exifList = new ArrayList<ExifInterface>();
	private MapView mapView;

	ExifInterface exif, exif2;
	private int latitude;
	private int longitude;
	private Boolean buttonAction = true; // true 면 on, false 면 off
	private int eventNum;
	
	
	Drawable drawable;
	MyItemizedOverlay itemizedOverlay;
	BalloonOverlayView balloonView;
	List<Overlay> mapOverlays;
	IEvent event;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main_mapview);

			// intent로 이벤트 번호를 얻어옴
			Intent intent = getIntent();
			eventNum = intent.getExtras().getInt("EventNumber");
			
			//event에서 받아옴
			event = EventManager.getInstance().getEvent(eventNum); 
			
			Log.e("MapView", "" + eventNum + " " + event.getEventPhotoList().size());
			
			// mapView setting
			mapView = (MapView) findViewById(R.id.mapview);
			mapView.setBuiltInZoomControls(true);
			mapOverlays = mapView.getOverlays();
			
			GeoPoint point = new GeoPoint(37566417, 126985133);
			MapController mapController = mapView.getController();
			mapController.setCenter(point);
			mapController.setZoom(17);
			
			for (int i = 0; i < event.getEventPhotoList().size(); i++) {
				imagePathList.add(getRealPathFromURI(event.getEventPhotoList().get(i)));
			}

			// 사진 GEO 태그 찾음
			try {
				for(int i=0; i<imagePathList.size(); i++) {
					exifList.add(new ExifInterface(imagePathList.get(i)));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			

			drawable = getResources().getDrawable(R.drawable.bubble_point);
			itemizedOverlay = new MyItemizedOverlay(drawable, mapView);

			// 1. 마커(말풍선) 생성
			for(int i=0; i<imagePathList.size(); i++) {
				GeoPoint basePoint = new GeoPoint(getLatitude(exifList.get(i)), getLongitude(exifList.get(i)));
				System.out.println("BasePoint : " + basePoint);
				OverlayItem overlayItem = new OverlayItem(basePoint, "", "");
				itemizedOverlay.addOverlay(overlayItem);
				
			}

			// 2 .itemizedOverlay 순회하면서 마커(말풍선) 띄우기
			for (int i = 0; i < itemizedOverlay.size(); i++) {
				itemizedOverlay.onTap(i, imagePathList.get(i));
			}

			// 3. mapView의 Overlays에 추가
			mapOverlays.add(itemizedOverlay);

			final ImageView markerControlButton = (ImageView) findViewById(R.id.balloon_control);
			markerControlButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (buttonAction) {
						itemizedOverlay.closeBalloon();
						markerControlButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_on));
						buttonAction = false;
					} else {
						itemizedOverlay.onBalloonOverlay();
						markerControlButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_off));
						buttonAction = true;
					}
				}
			});

			
		} catch (Exception E) {
			E.printStackTrace();
		}

	}

	private int getLatitude(ExifInterface exif) {
		latitude = Integer.parseInt(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE).split("/")[0]);
		return latitude / 10;
	}

	private int getLongitude(ExifInterface exif) {
		longitude = Integer.parseInt(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE).split("/")[0]);
		return longitude / 10;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	// Uri로 String path 얻어내기
	public String getRealPathFromURI(Uri contentUri) 
	{
	    // can post image
	    String [] proj={MediaStore.Images.Media.DATA};
	    
	    Cursor cursor = managedQuery( contentUri,
	        proj, // Which columns to return
	        null, // WHERE clause; which rows to return (all rows)
	        null, // WHERE clause selection arguments (none)
	        null); // Order-by clause (ascending by name)
	    int i=0;
	    cursor.moveToFirst();
	    while(cursor.moveToNext()) {
	    	i++;
	    	System.out.println(""+i);
	    }
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	 
	    cursor.moveToFirst();
	 
	    return cursor.getString(column_index);
	}
	

}
