package skp.collage.mapview;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private Context context;
	
	public MyItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		
		super(boundCenterBottom(defaultMarker), mapView);
		context = mapView.getContext();
	}

	public void addOverlay(OverlayItem overlay) {
		m_overlays.add(overlay);
		this.populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index) {
		Toast.makeText(context, "¸»Ç³¼±ÅÍÄ¡" + index, Toast.LENGTH_SHORT).show();
		return true;
	}
	
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, false);
	}
	
}
