package skp.collarge.event;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;

import skp.collarge.AllTheEvil;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images;

public class EventManager {

	private static EventManager instance;

	private EventManager() {
	}

	public static EventManager getInstance() {
		if (instance == null) {
			instance = new EventManager();
		}
		return instance;
	}

	public IEvent getEvent(int eventId) {
		AllTheEvil ate = AllTheEvil.getInstance();
		Cursor c = ate
				.getContext()
				.getContentResolver()
				.query(Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
						null); // Image데이터
		for (int i = 0; i < eventId * 10; i++) {
			if (c.moveToNext() == false)
				return new Event(ate.getContext(), new ArrayList<Uri>());
		}
		ArrayList<Uri> ret = new ArrayList<Uri>();
		for (int i = 0; i < 10; i++) {
			if (c.moveToNext() == false)
				return new Event(ate.getContext(), ret);
			ret.add(ContentUris.withAppendedId(
					Images.Media.EXTERNAL_CONTENT_URI,
					c.getLong(c.getColumnIndex(Images.Media._ID))));
		}
		return new Event(ate.getContext(), ret);
	}

	public int getEventSize() {
		Cursor c = AllTheEvil
				.getInstance()
				.getContext()
				.getContentResolver()
				.query(Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
						null); // Image데이터
		return (c.getCount() + 9) / 10;
	}
}
