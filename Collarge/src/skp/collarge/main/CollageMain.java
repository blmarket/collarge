package skp.collarge.main;

import skp.collage.mapview.MyMapView;
import skp.collarge.MainActivity;
import skp.collarge.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class CollageMain extends Activity {
	
	ImageView leftImageButton;
	ImageView rightImageButton;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_collage);
        
        leftImageButton = (ImageView)findViewById(R.id.table_leftbutton);
        rightImageButton = (ImageView)findViewById(R.id.table_rightbutton);
        
        leftImageButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				leftImageButton.setImageDrawable(getResources().getDrawable(R.drawable.top_btn_left_on));
				return false;
			}
		});
        
        rightImageButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				rightImageButton.setImageDrawable(getResources().getDrawable(R.drawable.top_btn_right_on));
				Intent intent = new Intent(CollageMain.this, MyMapView.class);
    			startActivity(intent);
				return false;
			}
		});
    }
}
