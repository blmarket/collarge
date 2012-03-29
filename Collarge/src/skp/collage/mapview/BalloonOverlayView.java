package skp.collage.mapview;

import java.io.BufferedInputStream;
import java.io.File;
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
	ImageView picImage;
	Bitmap bitmap;

	public BalloonOverlayView(Context context) throws FileNotFoundException {
		super(context);
		
		picImage = (ImageView) findViewById(R.id.picture_image);
		setPadding(5, 5, 5, 5);
		
		layout = new LinearLayout(context); // mapView�� LinearLayout ����-
		layout.setVisibility(LinearLayout.VISIBLE); // VISIBLE-
		layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bubble_back));

		// mapview_balloon_overlay.xml�� �̹����� �����ش�.
		// FileInputStream in = new FileInputStream(new
		// File("/mnt/sdcard/DCIM/Camera/20120328_151059.jpg"));
		// BufferedInputStream buf = new BufferedInputStream(in);


		// ��ǳ���� view�� ���� ����ش�.
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.mapview_balloon_overlay, layout);
		bitmap = BitmapFactory
				.decodeFile("/mnt/sdcard/DCIM/Camera/20120328_151059.jpg");
		// picImage.setImageBitmap(bitmap);

		// FrameLayout ����-
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.NO_GRAVITY;

		addView(layout, params);
	}
}
