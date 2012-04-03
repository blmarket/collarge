package skp.collarge.thumbnail;

import android.graphics.Bitmap;
import android.net.Uri;

public interface IThumbnailBuilder {
	Bitmap build(Uri uri);
	void close();
}
