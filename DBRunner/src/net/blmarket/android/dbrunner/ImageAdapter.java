package net.blmarket.android.dbrunner;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

	public static final int THUMBNAIL_HEIGHT = 150;
	public static final int THUMBNAIL_WIDTH = 150;

	Context mContext;
	Cursor cursor;
	ContentResolver contentResolver;
	ArrayList<Uri> uris;

	public ImageAdapter(Context context, Cursor cursor,
			ContentResolver contentResolver) {
		mContext = context;
		this.cursor = cursor;
		this.contentResolver = contentResolver;

		uris = new ArrayList<Uri>();
		while (cursor.moveToNext()) {
			long id = cursor.getLong(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
			uris.add(ContentUris.withAppendedId(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id));

			/*
			 * String fileName = cursor.getString(2);
			 * 
			 * Bitmap bmp = BitmapFactory.decodeFile(fileName); Float width =
			 * new Float(bmp.getWidth()); Float height = new
			 * Float(bmp.getHeight()); Float ratio = width / height; bmp =
			 * Bitmap.createScaledBitmap(bmp, (int) (THUMBNAIL_HEIGHT * ratio),
			 * THUMBNAIL_HEIGHT, false);
			 * 
			 * bitmaps.add(bmp);
			 */
		}
	}

	public int getCount() {
		return uris.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (position >= uris.size() || position < 0)
			return null;
		
		ImageView imageView;
		if (convertView != null && convertView instanceof ImageView)
			imageView = (ImageView) convertView;
		else
			imageView = new ImageView(mContext);

		Bitmap bmp = Thumbnails.getThumbnail(contentResolver,
				ContentUris.parseId(uris.get(position)), Thumbnails.MICRO_KIND,
				null);
		if(bmp == null)
			bmp = Bitmap.createBitmap(10, 10, null);
		int padding = (THUMBNAIL_WIDTH - bmp.getWidth()) / 2;

		imageView.setPadding(padding, 0, padding, 0);
		imageView.setImageBitmap(bmp);

		return imageView;

	}
}
