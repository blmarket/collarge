package skp.collarge;

import skp.collarge.event.EventManager;
import skp.collarge.main.CollargeMain;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

public class MainActivity extends Activity implements MediaScannerConnectionClient {
	MediaScannerConnection mConnection;
	Handler mHandler = new Handler();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_start);
        
        Handler mHandler = new Handler();
        AllTheEvil.initialize(getApplicationContext());
        EventManager.getInstance();
        
    	mConnection = new MediaScannerConnection(this, this);
    	mConnection.connect();
    }

	@Override
	public void onMediaScannerConnected() {
		mConnection.scanFile("/mnt/sdcard/Collarge", "*/*");
		
	}

	@Override
	public void onScanCompleted(String arg0, Uri arg1) {
		System.out.println(arg0 + " " + arg1);
		mConnection.disconnect();
		
    	mHandler.postDelayed(new Runnable() {
    		public void run() { 
    			// 1.5초 뒤에 Menu 액티비티 전환
    			Intent intent = new Intent(MainActivity.this, CollargeMain.class);
    			startActivity(intent);
    			overridePendingTransition(R.anim.fade_out, R.anim.hold);
    			finish();
    		}
    	}, 1500);
	}
}
