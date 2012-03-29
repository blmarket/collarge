package net.blmarket.timeline;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
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
	private ArrayList<UriImage> contentUris;

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
		this.contentUris = new ArrayList<TimelineView.UriImage>();
		for (Uri item : imageUris) {
			UriImage ui = new UriImage(item);
			try {
				ui.image = MediaStore.Images.Media.getBitmap(contentResolver, item);
			} catch (Exception e) {
				ui.image = null;
			}
			contentUris.add(ui);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		Paint p = new Paint();
		p.setColor(0x77777777);

		canvas.drawRect(new Rect(5, 5, 100, 100), new Paint());
	}
}
