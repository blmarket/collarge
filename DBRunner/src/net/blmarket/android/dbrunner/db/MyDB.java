package net.blmarket.android.dbrunner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDB extends SQLiteOpenHelper {
	
	private static final int VERSION  = 4;
	
	public MyDB(Context context) {
		super(context, "myDB.db", null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("Database creating");
		db.execSQL("create table thumbs (_id INTEGER PRIMARY KEY, file TEXT UNIQUE, data BLOB);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("Database drop");
		db.execSQL("drop table thumbs");
		onCreate(db);
	}
	
	public void put(String key, byte[] data) {
		ContentValues values = new ContentValues();
		values.put("file", key);
		values.put("data", data);
		SQLiteDatabase db = getWritableDatabase();
		db.insertWithOnConflict("thumbs", null, values, SQLiteDatabase.CONFLICT_REPLACE);
		db.close();
		System.out.println("Done");
	}
	
	public void count() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from thumbs", null);
		String[] cols = cursor.getColumnNames();
		for(String val : cols)
			System.out.println(val);
		System.out.println("cursors : " + cursor.getCount());
	}
	
	public byte[] get(String key)
	{
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from thumbs where file='" + key + "'", null);
		
		if(cursor.moveToNext())
		{
			return cursor.getBlob(cursor.getColumnIndex("data"));
		}
		
		return null;		
	}
}
