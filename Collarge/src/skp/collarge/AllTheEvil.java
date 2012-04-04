package skp.collarge;

import java.io.File;
import java.util.Random;

import skp.collarge.db.MyDB;
import android.content.Context;

public class AllTheEvil {
	private static AllTheEvil instance = null;
	private Context context;
	private MyDB myDB;
	private Random random;

	private AllTheEvil() {
	}

	public static void initialize(Context context) {
		instance = new AllTheEvil();
		instance.context = context;
		instance.myDB = new MyDB(context);
		instance.random = new Random();
	}

	public static void close() {
		instance.myDB.close();
	}

	public static AllTheEvil getInstance() {
		if (instance == null)
			throw new RuntimeException("call initialize plz");
		return instance;
	}

	public Context getContext() {
		return context;
	}

	public MyDB getDB() {
		return myDB;
	}

	public File openFile(String fileName) {
		System.out.println("openFile : " + context.getExternalFilesDir(null));
		File file = new File(context.getExternalFilesDir(null), fileName);
		return file;
	}

	public Random getRandom() {
		return random;
	}
}
