package skp.collarge;

import skp.collarge.event.EventManager;
import skp.collarge.main.CollargeMain;
import skp.collarge.main.EventView;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_start);
        
        Handler mHandler = new Handler();
    	mHandler.postDelayed(new Runnable() {
    		public void run() { 
    			// 1.5초 뒤에 Menu 액티비티 전환
    			Intent intent = new Intent(MainActivity.this, CollargeMain.class);
    			startActivity(intent);
    			overridePendingTransition(R.anim.fade_out, R.anim.hold);
    			finish();
    		}
    	}, 1500);
    	
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
	            + Environment.getExternalStorageDirectory()))); 		
		
        AllTheEvil.initialize(getApplicationContext());
        EventManager.getInstance();
    }
}
