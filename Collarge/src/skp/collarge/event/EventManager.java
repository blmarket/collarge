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

public class EventManager {

	private static EventManager instance;

	private ArrayList<IEvent> eventList;

	private EventManager() {
		eventList = new ArrayList<IEvent>();
		MyDB mydb = AllTheEvil.getInstance().getDB();
		SQLiteDatabase db = mydb.getReadableDatabase();
		Cursor c = db.rawQuery("select * from events", null);
		while (c.moveToNext()) {
			String json = c.getString(c.getColumnIndex("json"));
			IEvent ev = fromString(json);
			if (ev.getEventPhotoList().size() == 0)
				continue;
			eventList.add(ev);
		}
		c.close();

		// FIXME: remove this bunch of shit
		if (eventList.size() == 0) {
			eventList.clear();
			System.out.println("getit");
			DummyEventManager.getInstance().getEventSize();
			for (int i = 0; i < Math.min(2, DummyEventManager.getInstance()
					.getEventSize()); i++) {
				System.out.println("Creating trash event " + i);
				eventList.add(DummyEventManager.getInstance().getEvent(i));
			}
		}
	}

	public static EventManager getInstance() {
		if (instance == null) {
			instance = new EventManager();
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
		return eventList.get(eventId);
	}

	public int getEventSize() {
		return eventList.size();
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
