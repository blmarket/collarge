package net.blmarket.android.dbrunner;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

// œX¶óœX¶ó
public class DBRunnerActivity extends Activity implements
		LoaderManager.LoaderCallbacks<Cursor> {
	public static final int LOADER_ID = 0x1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DISPLAY_NAME,
				MediaStore.Images.Media.DATA };
		return new CursorLoader(getApplicationContext(),
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, null);
	}

	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		TextView tv = (TextView) findViewById(R.id.textView1);
		String[] columnNames = arg1.getColumnNames();

		String tmp = "" + arg1.getCount() + "[ ";

		for (String v : columnNames) {
			tmp = tmp + "," + v;
		}
		tv.setText(tmp);

		GridView view = (GridView) findViewById(R.id.gridView1);
		
		view.setAdapter(new ImageAdapter(this, arg1, getContentResolver()));
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		// do nothing.
	}
}