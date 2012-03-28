package skp.collarge.maptest;

import java.io.File;
import java.io.IOException;

import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.android.maps.MapActivity;

public class MainActivity extends MapActivity {

	private String imagePath = "/sdcard/DCIM/Camera/20120327_172630.jpg";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		File imageFile = new File(imagePath);
		
		try {
			ExifInterface exif = new ExifInterface(imageFile.toString());
			EditText editText1 = (EditText)findViewById(R.id.editText1);
			editText1.setText(getExif(exif));
		} catch (IOException e) {
			// image 파일이 아니라면
			e.printStackTrace();
		}
		
	}
	
	
	private String getExif(ExifInterface exif) {
		String myGeoInfo ="";
		Log.i("info", getTagString(ExifInterface.TAG_DATETIME, exif));
		Log.i("info", getTagString(ExifInterface.TAG_IMAGE_LENGTH, exif));
		Log.i("info", getTagString(ExifInterface.TAG_IMAGE_WIDTH, exif));
		
		myGeoInfo += getTagString(ExifInterface.TAG_GPS_ALTITUDE, exif);
		myGeoInfo += getTagString(ExifInterface.TAG_GPS_LATITUDE, exif);
		return myGeoInfo;
	}
	
	
	private String getTagString(String tag, ExifInterface exif) {
		return (tag + " : " + exif.get.getAttribute(tag) + "\n");
	}

	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
