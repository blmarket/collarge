package skp.collarge.event;

import java.util.AbstractList;

import skp.collarge.thumbnail.DBCacheThumbnailBuilder;
import skp.collarge.thumbnail.IThumbnailBuilder;
import skp.collarge.thumbnail.MySimpleThumbnailBuilder;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

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
		IThumbnailBuilder builder = new DBCacheThumbnailBuilder(mContext,
				new MySimpleThumbnailBuilder(mContext.getContentResolver()));
		Bitmap thumbnail = builder.build(tmp);
		
		/*
		int mOutputX = 240, mOutputY = 200;
		
		Bitmap croppedImage = Bitmap.createBitmap(mOutputX, mOutputY, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(croppedImage);

		Rect srcRect = new Rect(0, 0, thumbnail.getWidth(), thumbnail.getHeight());
		Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);

		int dx = (srcRect.width() - dstRect.width()) / 2;
		int dy = (srcRect.height() - dstRect.height()) / 2;
		
		System.out.println(srcRect + " asdf " + dstRect);

		// If the srcRect is too big, use the center part of it.
		srcRect.inset(Math.max(0, dx), Math.max(0, dy));

		// If the dstRect is too big, use the center part of it.
		//dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

		// Draw the cropped bitmap in the center
		canvas.drawBitmap(thumbnail, srcRect, dstRect, null);
		
		canvas.save();
		*/
		
		imv.setImageBitmap(thumbnail);
		imv.setScaleType(ScaleType.CENTER_CROP);
		imv.setLayoutParams(new AbsListView.LayoutParams(240, 200));
		//imv.setMinimumHeight(200);
		//imv.setMinimumWidth(240);
		return imv;
	}
}
