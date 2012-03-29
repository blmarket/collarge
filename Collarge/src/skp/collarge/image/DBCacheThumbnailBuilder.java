package skp.collarge.image;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import skp.collarge.db.MyDB;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class DBCacheThumbnailBuilder implements IThumbnailBuilder {

	IThumbnailBuilder localBuilder;
	MyDB db;

	public DBCacheThumbnailBuilder(Context context, IThumbnailBuilder builder) {
		this.localBuilder = builder;
		db = new MyDB(context);

	}

	@Override
	public Bitmap build(Uri uri) {
		String key = uri.toString();
		byte[] cache = db.get(key);
		Bitmap bmp = null;

		if (cache != null) {
			System.out.println("Trying to decode cache : " + cache.length);
			bmp = BitmapFactory.decodeByteArray(cache, 0, cache.length);
		}

		if (bmp == null) {
			bmp = localBuilder.build(uri);
			if (bmp == null)
				return null;
			System.out.println("Bitmap Created : " + bmp.getWidth() + " "
					+ bmp.getHeight());

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(CompressFormat.JPEG, 50, stream);
			System.out.println("Stream size : " + stream.size());
			db.put(key, stream.toByteArray());
			return bmp;
		}
		System.out.println("Bitmap Loaded : " + bmp.getWidth() + " "
				+ bmp.getHeight());
		return bmp;
	}
}
