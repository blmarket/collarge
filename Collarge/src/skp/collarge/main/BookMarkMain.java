package skp.collarge.main;

import skp.collarge.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class BookMarkMain extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmark_main);
		
		ImageView img = (ImageView)findViewById(R.id.bookmark_imageView);
		
		img.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Toast.makeText(BookMarkMain.this, "구현을 못했습니다!", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		
	}

}
