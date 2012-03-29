package skp.collage.mapview;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

public class BalloonOverlayView extends FrameLayout {

	private LinearLayout layout;
	
	public BalloonOverlayView(Context context, int balloonBottomOffset) throws FileNotFoundException {
		super(context);
		
		setPadding(0, 0, 0, balloonBottomOffset);
		layout = new LinearLayout(context);
		layout.setVisibility(VISIBLE);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.mapview_balloon_overlay, layout);
		
		FileInputStream in = new FileInputStream("/mnt/sdcard/DCIM/Camera/20120328_151059.jpg");
		BufferedInputStream buf = new BufferedInputStream(in);
		Bitmap bMap = BitmapFactory.decodeStream(buf);
		ImageView picImage = (ImageView)findViewById(R.id.picture_image);
		picImage.setImageBitmap(bMap);
		
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.NO_GRAVITY;
		addView(layout, params);
	
		}
}
