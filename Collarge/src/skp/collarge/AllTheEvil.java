package skp.collarge;

import android.content.ContentResolver;
import android.content.Context;

public class AllTheEvil {
	private static AllTheEvil instance = null;

	private Context context;

	private AllTheEvil() {
	}

	public static void initialize(Context context) {
		instance = new AllTheEvil();
		instance.context = context;
	}

	public static AllTheEvil getInstance() {
		if (instance == null)
			throw new RuntimeException("call initialize plz");
		return instance;
	}

	public Context getContext() { 
		return context;
	}
}
