package collague.gallery.viewgroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class GalleryViewActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        //1. View
        setContentView(R.layout.main);
        
        
        //2. 이벤트 발생
        actionEvent();
    }

	private void actionEvent() {
		// TODO Auto-generated method stub
		findViewById(R.id.image_button1).setOnClickListener(this);
		findViewById(R.id.image_button2).setOnClickListener(this);
		findViewById(R.id.image_button3).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.image_button1:
			buttonHandling1();
			break;

		case R.id.image_button2:
			buttonHandling2();			
			break;

		case R.id.image_button3:
			buttonHandling3();			
			break;			
		}
		
	}


	private void buttonHandling1() {
		getMessage("첫 번째 이미지");
		
		Intent i = new Intent(this, PhotoView.class);
		i.putExtra("상태", true);
		startActivity(i);
		
	}
	
	
	private void getMessage(String message) {
		
		Log.i(message + "이미지를 선택","0");
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	private void buttonHandling2() {
		
		
	}	
	
	
	private void buttonHandling3() {
		
		
	}		
	
	
}