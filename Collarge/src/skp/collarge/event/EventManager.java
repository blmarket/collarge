package skp.collarge.event;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import skp.collarge.AllTheEvil;
import android.net.Uri;

public class EventManager {

	private static EventManager instance;
	private static final String FILENAME = "eventlist.txt";
	private ArrayList<IEvent> eventList;

	private EventManager() {
		eventList = new ArrayList<IEvent>();
		try {
			File file = AllTheEvil.getInstance().openFile(FILENAME);
			byte[] buffer = new byte[(int) file.length()];
			FileInputStream str = new FileInputStream(file);
			str.read(buffer);
			str.close();

			String string = new String(buffer);

			JSONArray arr = new JSONArray(string);

			System.out.println("File loaded : " + arr.length());

			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				IEvent ev = fromJSONObject(obj);
				eventList.add(ev);
			}
		} catch (Exception e) { // 파일 없으면? 그냥 안하는 거지 뭐...
			System.out.println("File Open Failed TT");
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

		JSONArray arr = new JSONArray();
		for (int i = 0; i < instance.getEventSize(); i++) {
			IEvent e = instance.getEvent(i);
			arr.put(toJSONObject(e));
		}
		System.out.println("JSON Output : " + arr.length());

		try {
			FileOutputStream str = new FileOutputStream(AllTheEvil
					.getInstance().openFile(FILENAME));
			str.write(arr.toString().getBytes());
			str.flush();
			str.close();
			System.out.println("write done");
		} catch (Exception e) {
			System.out.println("EventManager Serialization failed TT");
			e.printStackTrace();
		}
	}

	public IEvent getEvent(int eventId) {
		return eventList.get(eventId);
	}

	public int getEventSize() {
		return eventList.size();
	}

	public static String serializeEvent(IEvent event) {
		return toJSONObject(event).toString();
	}

	public static JSONObject toJSONObject(IEvent event) {
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		for (Uri uri : event.getEventPhotoList()) {
			arr.put(uri);
		}
		try {
			obj.put("Title", event.getEventName());
			obj.put("Array", arr);
		} catch (Exception E) {
			E.printStackTrace();
		}
		return obj;
	}

	public static IEvent fromString(String jsonString) {
		try {
			JSONObject obj = new JSONObject(jsonString);
			return fromJSONObject(obj);
		} catch (Exception E) {
			E.printStackTrace();
			return null;
		}
	}

	public static IEvent fromJSONObject(JSONObject obj) {
		try {
			JSONArray arr = (JSONArray) obj.get("Array");
			ArrayList<Uri> uriarr = new ArrayList<Uri>();
			for (int i = 0; i < arr.length(); i++)
				uriarr.add(Uri.parse(arr.getString(i)));
			IEvent event = new Event(AllTheEvil.getInstance().getContext(),
					uriarr);
			event.setEventName(obj.getString("Title"));
			return event;
		} catch (JSONException E) {
			return null;
		}
	}

	public IEvent createEvent() {
		IEvent ret = new Event(AllTheEvil.getInstance().getContext(),
				new ArrayList<Uri>());
		ret.setEventPeriod("12.4.1 ~ 12.4.4");
		this.eventList.add(ret);
		return ret;
	}
}
