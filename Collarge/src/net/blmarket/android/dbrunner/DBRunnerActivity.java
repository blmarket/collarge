package net.blmarket.android.dbrunner;

import skp.collarge.R;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DBRunnerActivity extends Activity implements
		LoaderManager.LoaderCallbacks<Cursor> {
	public static final int LOADER_ID = 0x1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dbrunner);
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
		GridView view = (GridView) findViewById(R.id.gridView1);
		view.setAdapter(new ImageAdapter(this, arg1, getContentResolver()));
		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Uri uri = (Uri)(arg1.getTag());
				startActivity(new Intent(getApplicationContext(), skp.collarge.TestActivity.class));
			}
		});
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		// do nothing.
	}
}