package skp.collarge.thumbnail;

import java.io.ByteArrayOutputStream;

import skp.collarge.AllTheEvil;
import skp.collarge.db.MyDB.Thumb;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.Images;

public class DBCacheThumbnailBuilder implements IThumbnailBuilder {

	Context context;
	IThumbnailBuilder localBuilder;

	public DBCacheThumbnailBuilder(Context context, IThumbnailBuilder builder) {
		this.context = context;
		this.localBuilder = builder;
	}
	
	public DBCacheThumbnailBuilder(Context context) {
		this.context = context;
		localBuilder = new MySimpleThumbnailBuilder(context.getContentResolver());
	}

	@Override
	public Bitmap build(Uri uri) {
		String key = uri.toString();
		Thumb cache = AllTheEvil.getInstance().getDB().getThumb(key);
		Bitmap bmp = null;

		if (cache != null) {
			bmp = BitmapFactory.decodeByteArray(cache.data, 0, cache.data.length);
		}

		if (bmp == null) {
			bmp = localBuilder.build(uri);
			String path = "";
			Cursor c = Images.Media.query(context.getContentResolver(), uri, null);
			if(c.moveToNext())
			{
				path = c.getString(c.getColumnIndex(Images.Media.DATA));
				System.out.println("Resolved path : " + path);
			}
			c.close();
			if (bmp == null)
				return null;
			System.out.println("Bitmap Created : " + bmp.getWidth() + " "
					+ bmp.getHeight());

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(CompressFormat.JPEG, 80, stream);
			System.out.println("Stream size : " + stream.size());
			AllTheEvil.getInstance().getDB().putThumb(key, stream.toByteArray(), path);
			return bmp;
		}
		return bmp;
	}

	@Override
	public void close() {
		localBuilder.close();
	}
}
