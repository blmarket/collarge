package skp.collarge.main;

import skp.collarge.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EventView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_event);
		
		// Gridview ºÎºÐ
		GridView gridview = (GridView)findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {				
				Toast.makeText(EventView.this, "" + position, Toast.LENGTH_SHORT).show();
			}
		}); // GridView ³¡

	}

	
}
