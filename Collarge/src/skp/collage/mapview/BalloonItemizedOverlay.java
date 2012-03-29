package skp.collage.mapview;

import android.content.ClipData.Item;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class BalloonItemizedOverlay<item> extends ItemizedOverlay<OverlayItem> {

	private MapView mapView;
	private BalloonOverlayView balloonView;
	private int viewOffset;
	final MapController mapController;
	
	
	public BalloonItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(defaultMarker);
		this.mapView = mapView;
		viewOffset = 0;
		mapController = mapView.getController();
		// TODO Auto-generated constructor stub
	}

	public void setBalloonBottomOffset(int pixels) {
		viewOffset = pixels;
	}
	
	@Override
	protected OverlayItem createItem(int arg0) {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	protected boolean onBalloonTap(int index) {
		return false;
	}

}
