package skp.collarge;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images;

public class EventManager {

	private static EventManager instance;

	private ContentResolver contentResolver;

	private EventManager() {
	}

	public static void Initialize(ContentResolver contentResolver) {
		instance = new EventManager();
		instance.contentResolver = contentResolver;
	}

	public static EventManager getInstance() {
		if (instance == null) {
			throw new RuntimeException("getInstance하시기 전에 꼭 Initialize해주세0");
		}
		return instance;
	}

	public AbstractList<Uri> getEvent(int eventId) {
		Cursor c = contentResolver.query(Images.Media.EXTERNAL_CONTENT_URI,
				null, null, null, null); // Image데이터
		for (int i = 0; i < eventId * 10; i++) {
			if (c.moveToNext() == false)
				return new ArrayList<Uri>();
		}
		ArrayList<Uri> ret = new ArrayList<Uri>();
		for (int i = 0; i < 10; i++) {
			if (c.moveToNext() == false)
				return ret;
			ret.add(ContentUris.withAppendedId(
					Images.Media.EXTERNAL_CONTENT_URI,
					c.getLong(c.getColumnIndex(Images.Media._ID))));
		}
		return ret;
	}
	
	public int getEventSize() {
		Cursor c = contentResolver.query(Images.Media.EXTERNAL_CONTENT_URI,
				null, null, null, null); // Image데이터
		return (c.getCount()+9) / 10;
	}
}
