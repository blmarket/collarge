package skp.collarge.gallery.view;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class MySimpleThumbnailBuilder implements IThumbnailBuilder {

	private static final int THUMBNAIL_HEIGHT = 150;

	ContentResolver contentResolver;

	public MySimpleThumbnailBuilder(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	@Override
	public Bitmap build(Uri uri) {
		try {
			Bitmap ret = MediaStore.Images.Media
					.getBitmap(contentResolver, uri);

			Float width = new Float(ret.getWidth());
			Float height = new Float(ret.getHeight());
			Float ratio = width / height;
			ret = Bitmap.createScaledBitmap(ret,
					(int) (THUMBNAIL_HEIGHT * ratio), THUMBNAIL_HEIGHT, true);
			return ret;
		} catch (Exception e) {
			return null;
		}
	}

}
