package net.blmarket.android.dbrunner;

import android.graphics.Bitmap;
import android.net.Uri;

public interface IThumbnailBuilder {
	Bitmap build(Uri uri);
}
