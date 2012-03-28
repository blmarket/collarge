package collague.gallery.viewgroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class PhotoView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.photo_view);
		
		

		
		findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buttonHandling1();
				
			}
			
			
			
		});
		
		findViewById(R.id.comment_button1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buttonHandling2();
				
			}

		});
		
		
	}//on Create end
	

	
	private void buttonHandling1() {
		
		if(getIntent().getBooleanExtra("ป๓ลย", false))
			finish();
		
	}
	
	private void buttonHandling2() {
		
		
	}
	
}
