package skp.collarge;

import skp.collarge.main.CollageMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    			// 1.5�� �ڿ� Menu ��Ƽ��Ƽ ��ȯ
    			Intent intent = new Intent(MainActivity.this, CollageMain.class);
    			startActivity(intent);
    			overridePendingTransition(R.anim.fade_out, R.anim.hold);
    			finish();
    		}
    	}, 1500);
   	
    }
}
