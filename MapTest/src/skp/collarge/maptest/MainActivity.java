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
		myMapView.setBuiltInZoomControls(true); //������ zoom �����ϰ� ����
		
		File imageFile = new File(imagePath);
		
		try {
			ExifInterface exif = new ExifInterface(imageFile.toString());
			TextView editText1 = (TextView)findViewById(R.id.textView1);
			editText1.setText(getExif(exif));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GeoPoint imagePoint = new  GeoPoint(latitude, longitude); // image�� GEO Tag ��ġ
		mapController = myMapView.getController();
		mapController.animateTo(imagePoint);	// imagePoint�� �̵� 
		mapController.setZoom(16);
		ZoomButtonsController zoomController = myMapView.getZoomButtonsController();
		
		// zoomController Ȯ�� ���� ��
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
		
		// ��Ƽ��ġ Zoom ����
		// ���� ������- ��Ƽ��ġ�� ���ƾ��ϳ�? 
		
		
		// ��Ŀ ���� ��
		mapOverlaysList = myMapView.getOverlays(); //mapView�� Overlay list �� ����-
		Drawable drawable = this.getResources().getDrawable(R.drawable.marker_2);
		testOverlay = new TestOverlay(drawable);
		OverlayItem overLayItem = new OverlayItem(imagePoint, "", ""); //imagePoint�� ��Ŀ����-
		OverlayItem overLayItem2 = new OverlayItem(new  GeoPoint(latitude+1000, longitude-5000), "", ""); //imagePoint�� ��Ŀ����-
		testOverlay.addOverlay(overLayItem);  //overLayItem ��ü�� itemizedOverlay�� �߰�-
		testOverlay.addOverlay(overLayItem2);
		mapOverlaysList.add(testOverlay); //mapOverlayList�� �߰�-
				
	}
	
	
	// ����, �浵 ��� �Լ� 
	private String getExif(ExifInterface exif) {
		
		String myGeoInfo ="";
		latitude = Integer.parseInt(getTagString(ExifInterface.TAG_GPS_LATITUDE, exif))/10;	//���� ���
		longitude = Integer.parseInt(getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif))/10;	//�浵 ��� 
		myGeoInfo += "����: " + latitude + " / �浵: " + longitude; 
		
		
		return myGeoInfo;
	}
	
	
	// Tag �������� �ʿ��� ���� �Ľ��ϱ� 
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
