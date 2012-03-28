package skp.collarge.maptest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;
import android.widget.ZoomButtonsController.OnZoomListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity {

	private String imagePath = "/sdcard/DCIM/Camera/20120328_151059.jpg";
	private MapView myMapView;
	private int latitude;
	private int longitude;
	private List<Overlay> mapOverlaysList;
	private TestOverlay testOverlay;
	private MapController mapController;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		myMapView = (MapView)findViewById(R.id.mapView);
		myMapView.setBuiltInZoomControls(true); //지도의 zoom 가능하게 만들
		
		File imageFile = new File(imagePath);
		
		try {
			ExifInterface exif = new ExifInterface(imageFile.toString());
			TextView editText1 = (TextView)findViewById(R.id.textView1);
			editText1.setText(getExif(exif));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GeoPoint imagePoint = new  GeoPoint(latitude, longitude); // image의 GEO Tag 위치
		mapController = myMapView.getController();
		mapController.animateTo(imagePoint);	// imagePoint로 이동 
		mapController.setZoom(16);
		ZoomButtonsController zoomController = myMapView.getZoomButtonsController();
		
		// zoomController 확대 범위 제
		zoomController.setOnZoomListener(new OnZoomListener() {
			public void onZoom(boolean zoomIn) {
				if(zoomIn) {
					if(myMapView.getZoomLevel() > 17) {
						Toast.makeText(MainActivity.this, "MaxZoom", Toast.LENGTH_SHORT).show();
						mapController.setZoom(18);
					} else {
						mapController.zoomIn();
					}
				} else {
					mapController.zoomOut();
				}
			}

			@Override
			public void onVisibilityChanged(boolean visible) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// 멀티터치 Zoom 제어
		// 구현 못했음- 멀티터치를 막아야하나? 
		
		
		// 마커 관련 부
		mapOverlaysList = myMapView.getOverlays(); //mapView의 Overlay list 를 얻음-
		Drawable drawable = this.getResources().getDrawable(R.drawable.marker_2);
		testOverlay = new TestOverlay(drawable);
		OverlayItem overLayItem = new OverlayItem(imagePoint, "", ""); //imagePoint에 마커생성-
		OverlayItem overLayItem2 = new OverlayItem(new  GeoPoint(latitude+1000, longitude-5000), "", ""); //imagePoint에 마커생성-
		testOverlay.addOverlay(overLayItem);  //overLayItem 객체를 itemizedOverlay에 추가-
		testOverlay.addOverlay(overLayItem2);
		mapOverlaysList.add(testOverlay); //mapOverlayList에 추가-
				
	}
	
	
	// 위도, 경도 얻는 함수 
	private String getExif(ExifInterface exif) {
		
		String myGeoInfo ="";
		latitude = Integer.parseInt(getTagString(ExifInterface.TAG_GPS_LATITUDE, exif))/10;	//위도 얻기
		longitude = Integer.parseInt(getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif))/10;	//경도 얻기 
		myGeoInfo += "위도: " + latitude + " / 경도: " + longitude; 
		
		
		return myGeoInfo;
	}
	
	
	// Tag 정보에서 필요한 값만 파싱하기 
	private String getTagString(String tag, ExifInterface exif) {
		return (exif.getAttribute(tag).split("/")[0]);
	}

	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}




/*
public boolean onTouchEvent(MotionEvent event) {
	int pointerCount = event.getPointerCount();
	if(event.getAction() != MotionEvent.ACTION_UP) {
		if(pointerCount==2) {
			if(myMapView.getZoomLevel() > 17) {
				Toast.makeText(MainActivity.this, "MaxZoom", Toast.LENGTH_SHORT).show();
				mapController.setZoom(18);
			}
		}
	}
	return super.onTouchEvent(event);
}
*/
