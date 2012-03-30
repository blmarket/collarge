package skp.collage.mapview;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private Context context;
	private String imagePath;
	
	public MyItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		context = mapView.getContext();
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public void addOverlay(OverlayItem overlay) {
		m_overlays.add(overlay);
		populate();
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
		//��ǳ���� Ŭ�� ���� �� Activity ��ȯ�ϱ�
		Toast.makeText(context, "��ǳ����ġ", Toast.LENGTH_SHORT).show();
		return true;
	}
	
}
