package skp.collage.mapview;

import java.util.ArrayList;

import android.R.array;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> arraylist = new ArrayList<OverlayItem>();
	
	@Override
	public void draw(Canvas arg0, MapView arg1, boolean arg2) {
		// TODO Auto-generated method stub
		super.draw(arg0, arg1, arg2);
		
		//arg0.drawRect(new Rect(5,5,100,100), new Paint());
	}

	public MyOverlay(Drawable drawable) {
		super(boundCenterBottom(drawable));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected OverlayItem createItem(int i) {
		return arraylist.get(i);
	}

	@Override
	public int size() {
		return arraylist.size();
	}
	
	void addOverlay (OverlayItem overlayItem) {
		arraylist.add(overlayItem);
		this.populate();
	}

}
