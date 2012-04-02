package skp.collarge.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDB extends SQLiteOpenHelper {

	private static final int VERSION = 6;

	public MyDB(Context context) {
		super(context, "myDB.db", null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("Database creating");
		db.execSQL("create table thumbs (_id INTEGER PRIMARY KEY, file TEXT UNIQUE, data BLOB);");
		db.execSQL("create table events (_id INTEGER PRIMARY KEY, json TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 3:
			db.execSQL("drop table thumbs");
			db.execSQL("create table thumbs (_id INTEGER PRIMARY KEY, file TEXT UNIQUE, data BLOB);");
		case 4:
			db.execSQL("create table events (_id INTEGER PRIMARY KEY, json TEXT);");
		case 5:
			db.execSQL("drop table events");
			db.execSQL("create table events (_id INTEGER PRIMARY KEY, json TEXT);");
		case 6:
		}
	}

	public void putThumb(String key, byte[] data) {
		ContentValues values = new ContentValues();
		values.put("file", key);
		values.put("data", data);
		SQLiteDatabase db = getWritableDatabase();
		db.insertWithOnConflict("thumbs", null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		db.close();
	}

	public void addEvent(String data) {
		putEvent(-1, data);
	}

	public void putEvent(long key, String data) {
		System.out.println("Putting event : " + key + " = " + data);
		ContentValues values = new ContentValues();
		if (key != -1) {
			values.put("_id", new Long(key));
		}
		values.put("json", data);
		SQLiteDatabase db = getWritableDatabase();
		db.insertWithOnConflict("events", null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		db.close();
	}

	public void getThumbCount() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from thumbs", null);
		String[] cols = cursor.getColumnNames();
		for (String val : cols)
			System.out.println(val);
		System.out.println("cursors : " + cursor.getCount());
		cursor.close();
	}

	public byte[] getThumb(String key) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from thumbs where file='" + key
				+ "'", null);
		if (cursor.moveToNext()) {
			byte[] ret = cursor.getBlob(cursor.getColumnIndex("data"));
			cursor.close();
			return ret;
		}
		cursor.close();
		return null;
	}

	public String getEvent(long id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from events where _id='" + id
				+ "'", null);
		String ret = null;
		if (cursor.moveToNext()) {
			ret = cursor.getString(cursor.getColumnIndex("json"));
		}
		cursor.close();
		return ret;
	}
}
