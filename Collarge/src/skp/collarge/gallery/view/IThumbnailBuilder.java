package skp.collarge.gallery.view;

import android.graphics.Bitmap;
import android.net.Uri;

public interface IThumbnailBuilder {
	Bitmap build(Uri uri);
}
