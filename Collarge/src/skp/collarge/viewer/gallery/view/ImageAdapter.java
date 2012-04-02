package skp.collarge.viewer.gallery.view;

import java.util.ArrayList;

import skp.collarge.image.DBCacheThumbnailBuilder;
import skp.collarge.image.IThumbnailBuilder;
import skp.collarge.image.MySimpleThumbnailBuilder;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

// FIXME: implements onDestroy things to close thumbnailBuilder
public class ImageAdapter extends BaseAdapter {

	public static final int THUMBNAIL_HEIGHT = 100;
	public static final int THUMBNAIL_WIDTH = 100;

	Context mContext;
	Cursor cursor;
	ArrayList<Uri> uris;
	IThumbnailBuilder thumbnailBuilder;

	public ImageAdapter(Context context, Cursor cursor,
			ContentResolver contentResolver) {
		mContext = context;
		this.cursor = cursor;
		// thumbnailBuilder = new MySimpleThumbnailBuilder(contentResolver);
		thumbnailBuilder = new DBCacheThumbnailBuilder(context,
				new MySimpleThumbnailBuilder(contentResolver));

		uris = new ArrayList<Uri>();
		while (cursor.moveToNext()) {
			long id = cursor.getLong(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
			uris.add(ContentUris.withAppendedId(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id));
		}

		for (Uri i : uris) {
			System.out.println(i);
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

		Bitmap bmp = thumbnailBuilder.build(uris.get(position));
		if (bmp == null)
			bmp = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
		int padding = (THUMBNAIL_WIDTH - bmp.getWidth()) / 2;

		imageView.setPadding(padding, 0, padding, 0);
		imageView.setImageBitmap(bmp);
		imageView.setTag(uris.get(position));

		return imageView;

	}
}
