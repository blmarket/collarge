package skp.collarge.gallery.view;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Thumbnails;

// TODO: make it efficient
public class SimpleThumbnailBuilder implements IThumbnailBuilder {

	ContentResolver contentResolver;

	public SimpleThumbnailBuilder(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	@Override
	public Bitmap build(Uri uri) {
		return Thumbnails.getThumbnail(contentResolver, ContentUris.parseId(uri), Thumbnails.MICRO_KIND, null);
	}
}
