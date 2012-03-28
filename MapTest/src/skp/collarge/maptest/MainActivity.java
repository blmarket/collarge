package skp.collarge.maptest;

import android.media.ExifInterface;
import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class MainActivity extends MapActivity {

	private String imageString= "/sdcard/DCIM/Camera/20120327_172630.jpg";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//ExifInterface exif = new ExifInterface(filename);
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
