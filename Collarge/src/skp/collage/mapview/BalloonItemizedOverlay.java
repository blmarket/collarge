package skp.collage.mapview;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import skp.collarge.R;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public abstract class BalloonItemizedOverlay<Item> extends
		ItemizedOverlay<OverlayItem> {

	private class BooooomHeadshot {
		BalloonOverlayView balloonView;
		View clickRegion;
	}
	

	private MapView mapView;
	private ArrayList<BooooomHeadshot> boombs = new ArrayList<BalloonItemizedOverlay<Item>.BooooomHeadshot>();
	private int viewOffset;
	final MapController mc;

	public BalloonItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(defaultMarker);
		this.mapView = mapView;
		viewOffset = 0;
		mc = mapView.getController();
	}

	public void setBalloonBottomOffset(int pixels) {
		viewOffset = pixels;
	}

	protected boolean onBalloonTap(int index) {
		return false;
	}

	/*
	 * public void closeBalloon() { Toast.makeText(mapView.getContext(),
	 * "close balloonOverlay" , Toast.LENGTH_SHORT).show();
	 * balloonView.closeBalloonOverlayView(); }
	 * 
	 * public void onBalloonOverlay() { Toast.makeText(mapView.getContext(),
	 * "Open balloonOverlay" , Toast.LENGTH_SHORT).show();
	 * balloonView.onBalloonOverlayView(); }
	 */

	protected final boolean onTap(int index) {

		Toast.makeText(mapView.getContext(), "point ÅÍÄ¡", Toast.LENGTH_SHORT)
				.show();

		final int thisIndex;
		GeoPoint point;

		thisIndex = index;
		point = createItem(index).getPoint();

		BooooomHeadshot shot = new BooooomHeadshot();
		shot.balloonView = new BalloonOverlayView(mapView.getContext(),
				viewOffset);
		shot.clickRegion = (View) shot.balloonView
				.findViewById(R.id.balloon_inner_layout);

		shot.balloonView.setVisibility(View.GONE);

		List<Overlay> mapOverlays = mapView.getOverlays();

		shot.balloonView.setData(createItem(index));

		MapView.LayoutParams params = new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, point,
				MapView.LayoutParams.BOTTOM_CENTER);
		params.mode = MapView.LayoutParams.MODE_MAP;

		setBalloonTouchListener(shot.clickRegion, thisIndex);

		shot.balloonView.setVisibility(View.VISIBLE);

		mapView.addView(shot.balloonView, params);

		return true;
	}

	/*
	 * private void hideBalloon() { if (balloonView != null) {
	 * balloonView.setVisibility(View.GONE); } }
	 */

	/*
	 * private void hideOtherBalloons(List<Overlay> overlays) {
	 * 
	 * for (Overlay overlay : overlays) { if (overlay instanceof
	 * BalloonItemizedOverlay<?> && overlay != this) {
	 * ((BalloonItemizedOverlay<?>) overlay).hideBalloon(); } }
	 * 
	 * }
	 */

	private void setBalloonTouchListener(View clickRegion, final int thisIndex) {
		try {
			@SuppressWarnings("unused")
			Method m = this.getClass().getDeclaredMethod("onBalloonTap",
					int.class);

			clickRegion.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {

					View l = ((View) v.getParent())
							.findViewById(R.id.balloon_main_layout);
					Drawable d = l.getBackground();

					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						int[] states = { android.R.attr.state_pressed };
						if (d.setState(states)) {
							d.invalidateSelf();
						}
						return true;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						int newStates[] = {};
						if (d.setState(newStates)) {
							d.invalidateSelf();
						}
						// call overridden method
						onBalloonTap(thisIndex);
						return true;
					} else {
						return false;
					}

				}
			});

		} catch (SecurityException e) {
			Log.e("BalloonItemizedOverlay",
					"setBalloonTouchListener reflection SecurityException");
			return;
		} catch (NoSuchMethodException e) {
			// method not overridden - do nothing
			return;
		}

	}

}