package skp.collarge;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.test);

		/*
		ImageSwitcher sw = new ImageSwitcher(this);
		ImageView v1 = new ImageView(this);
		ImageView v2 = new ImageView(this);
		v1.setImageDrawable(getResources().getDrawable(R.drawable.episode1));
		v2.setImageDrawable(getResources().getDrawable(R.drawable.episode2));
		this.addContentView(sw, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		*/
	}

}
