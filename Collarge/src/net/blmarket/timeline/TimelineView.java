package net.blmarket.timeline;

import java.util.ArrayList;
import java.util.Collection;

import skp.collarge.image.DBCacheThumbnailBuilder;
import skp.collarge.image.IThumbnailBuilder;
import skp.collarge.image.MySimpleThumbnailBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

public class TimelineView extends View {

	private class UriImage {
		public UriImage(Uri uri) {
			this.uri = uri;
		}

		public Uri uri;
		public Bitmap image;
	}

	private ContentResolver contentResolver;
	private ArrayList<UriImage> uriImages;

	private void init() {
		contentResolver = null;
	}

	public TimelineView(Context context) {
		super(context);
		init();
	}

	public TimelineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TimelineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void setImages(ContentResolver contentResolver,
			Collection<Uri> imageUris) {
		this.contentResolver = contentResolver;
		this.uriImages = new ArrayList<TimelineView.UriImage>();

		IThumbnailBuilder builder = new DBCacheThumbnailBuilder(getContext(),
				new MySimpleThumbnailBuilder(contentResolver));

		for (Uri item : imageUris) {
			UriImage ui = new UriImage(item);
			try {
				ui.image = builder.build(item);
			} catch (Exception e) {
				ui.image = null;
			}
			uriImages.add(ui);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint p = new Paint();
		// p.setColor(0x77777777);

		float pos = 5;
		for (UriImage item : uriImages) {
			canvas.drawBitmap(item.image, 5, pos, p);
			pos += item.image.getHeight();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(500, 5000);
	}
}
