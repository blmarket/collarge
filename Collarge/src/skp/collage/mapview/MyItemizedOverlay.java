package skp.collage.mapview;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

class BalloonItemizedOverlay<T> {
	
}

public class MyItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private Context c;
	
	public MyItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		c = mapView.getContext();
	}

	public void addOverlay(OverlayItem overlay) {
		m_overlays.add(overlay);
	}

	protected OverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	public int size() {
		return m_overlays.size();
	}

	
	
}
