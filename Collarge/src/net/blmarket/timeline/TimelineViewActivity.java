package net.blmarket.timeline;

import skp.collarge.R;
import java.util.ArrayList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

public class TimelineViewActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		String contents[] = { "content://media/external/images/media/19",
				"content://media/external/images/media/20",
				"content://media/external/images/media/21",
				"content://media/external/images/media/22",
				"content://media/external/images/media/23",
				"content://media/external/images/media/24",
				"content://media/external/images/media/25",
				"content://media/external/images/media/26" };

		ArrayList<Uri> uris = new ArrayList<Uri>();

		for (String item : contents) {
			Uri ii = Uri.parse(item);
			uris.add(ii);
		}
		
		((TimelineView)findViewById(R.id.time)).setImages(getContentResolver(), uris);
	}
}
