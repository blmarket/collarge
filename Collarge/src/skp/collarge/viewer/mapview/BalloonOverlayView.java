package skp.collarge.viewer.mapview;

import skp.collarge.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.maps.OverlayItem;

public class BalloonOverlayView extends FrameLayout {

	private LinearLayout layout;
	private ImageView image;
	Bitmap bMap;

	public BalloonOverlayView(Context context, int balloonBottomOffset, String path) {
		super(context);

		setPadding(10, 0, 10, balloonBottomOffset);
		layout = new LinearLayout(context);
		layout.setVisibility(VISIBLE);

		// TODO
		// 최적화 해야 함(썸네일을 이용해야할 듯)
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 5;
		bMap = BitmapFactory.decodeFile(path, options);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.mapview_balloon_overlay, layout);
		image = (ImageView) v.findViewById(R.id.balloon_item_image);
		image.setImageBitmap(bMap);

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.NO_GRAVITY;

		addView(layout, params);
	}

	public void setData(OverlayItem item) {
		layout.setVisibility(VISIBLE);
	}

	public void setImagePath(String imagePath) {
		//this.imagePath = imagePath;
	}

	// 말풍선 Visibility 조절
	public void closeBalloonOverlayView() {
		layout.setVisibility(GONE);
	}

	public void onBalloonOverlayView() {
		layout.setVisibility(VISIBLE);
	}

}
