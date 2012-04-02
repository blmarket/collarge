package skp.collarge.event;

import java.util.AbstractList;
import java.util.ArrayList;

import skp.collarge.AllTheEvil;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.widget.ImageView;

public class Event implements IEvent {
	private Context mContext;
	private AbstractList<Uri> imageList;

	public Event(Context context, AbstractList<Uri> imageList) {
		mContext = context;
		this.imageList = imageList;
	}

	@Override
	public AbstractList<Uri> getEventPhotoList() {
		return imageList;
	}

	@Override
	public View getThumbnailView() {
		ImageView imv = new ImageView(mContext);

		if (imageList == null || imageList.size() == 0)
			return imv;

		Uri tmp = imageList.get(0);
		Bitmap thumbnail = Thumbnails.getThumbnail(AllTheEvil.getInstance()
				.getContext().getContentResolver(), ContentUris.parseId(tmp),
				Thumbnails.MICRO_KIND, null);
		imv.setImageBitmap(thumbnail);
		return imv;
	}
}
