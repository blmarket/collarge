package skp.collarge.event;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import skp.collarge.AllTheEvil;
import skp.collarge.db.MyDB;
import android.content.ContentUris;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore.Images;

public class CopyOfEventManager {

	private static CopyOfEventManager instance;
	
	private ArrayList<IEvent> eventList;

	private CopyOfEventManager() {
		eventList = new ArrayList<IEvent>();
		MyDB mydb = AllTheEvil.getInstance().getDB();
		SQLiteDatabase db = mydb.getReadableDatabase();
		Cursor c = db.rawQuery("select * from events", null);
		while(c.moveToNext())
		{
			String json = c.getString(c.getColumnIndex("json"));
			eventList.add(fromString(json));
		}
	}

	public static CopyOfEventManager getInstance() {
		if (instance == null) {
			instance = new CopyOfEventManager();
		}
		return instance;
	}

	public static void close() {
		if (instance == null)
			return;
		for (int i = 0; i < instance.getEventSize(); i++) {
			IEvent e = instance.getEvent(i);
			String json = instance.serializeEvent(e);
			AllTheEvil.getInstance().getDB().putEvent(i, json);			
		}
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

	public String serializeEvent(IEvent event) {
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		for (Uri uri : event.getEventPhotoList()) {
			arr.put(uri);
		}
		try {
			obj.put("Title", "Some Title");
			obj.put("Array", arr);
		} catch (Exception E) {
			E.printStackTrace();
		}
		return obj.toString();
	}

	public IEvent fromString(String jsonString) {
		try {
			JSONObject obj = new JSONObject(jsonString);
			JSONArray arr = (JSONArray) obj.get("Array");
			ArrayList<Uri> uriarr = new ArrayList<Uri>();
			for (int i = 0; i < arr.length(); i++)
				uriarr.add(Uri.parse(arr.getString(i)));
			return new Event(AllTheEvil.getInstance().getContext(), uriarr);
		} catch (Exception E) {
			E.printStackTrace();
			return null;
		}
	}
}
