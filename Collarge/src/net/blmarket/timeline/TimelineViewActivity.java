package net.blmarket.timeline;

import java.util.ArrayList;

import skp.collarge.R;
import skp.collarge.image.DBCacheThumbnailBuilder;
import skp.collarge.image.IThumbnailBuilder;
import skp.collarge.image.MySimpleThumbnailBuilder;
import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

public class TimelineViewActivity extends Activity implements OnTouchListener,
		OnClickListener {

	ViewFlipper flipper;
	ArrayList<ScrollView> scroller = new ArrayList<ScrollView>();
	// 터치 이벤트 발생 지점의 x좌표 저장
	float xAtDown;
	float xAtUp;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		String[] projection = { MediaStore.Images.Media._ID };
		Cursor cursor = MediaStore.Images.Media.query(getContentResolver(),
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection);
		ArrayList<Uri> uris = new ArrayList<Uri>();

		while (cursor.moveToNext()) {
			uris.add(ContentUris.withAppendedId(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					cursor.getLong(cursor
							.getColumnIndex(MediaStore.Images.Media._ID))));
		}

		setImages(findViewById(R.id.timeline_1), uris, 1, 0);
		setImages(findViewById(R.id.timeline_2), uris, 5, 1);

		flipper = ((ViewFlipper) findViewById(R.id.timelineflipper));
		flipper.setOnTouchListener(this);

		scroller.add((ScrollView) findViewById(R.id.timeline_scroll_1));
		scroller.add((ScrollView) findViewById(R.id.timeline_scroll_2));
		for (ScrollView view : scroller) {
			view.setOnTouchListener(this);
		}
	}

	private void setImages(View v, ArrayList<Uri> uris, int step, int level) {
		LinearLayout ll = (LinearLayout) v;

		IThumbnailBuilder builder = new DBCacheThumbnailBuilder(this,
				new MySimpleThumbnailBuilder(getContentResolver()));

		for (int i = 0; i < uris.size(); i += step) {
			Uri item = uris.get(i);
			Bitmap image;
			try {
				image = builder.build(item);
			} catch (Exception e) {
				image = null;
			}

			if (image == null)
				continue;
			ImageView imv = new ImageView(this);
			imv.setTag(R.string.timeline_level, new Integer(level));
			imv.setImageBitmap(image);
			//imv.setOnClickListener(this);
			ll.addView(imv);
		}
		builder.close();
	}

	// View.OnTouchListener의 abstract method
	// flipper 터지 이벤트 리스너
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			xAtDown = event.getX(); // 터치 시작지점 x좌표 저장
			System.out.println("xAtDown : " + xAtDown);
			v.onTouchEvent(event);
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			xAtUp = event.getX(); // 터치 끝난지점 x좌표 저장
			System.out.println("xAtUp : " + xAtUp);

			if (Math.abs(xAtUp - xAtDown) < 80)
				return false;

			if (xAtUp < xAtDown) {
				// 왼쪽 방향 에니메이션 지정
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_out));

				// 다음 view 보여줌
				flipper.showNext();
				return true;
			} else if (xAtUp > xAtDown) {
				// 오른쪽 방향 에니메이션 지정
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_out));
				// 전 view 보여줌
				flipper.showPrevious();
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println(v);
		int level = ((Integer) v.getTag(R.string.timeline_level)).intValue();
		if (level > 0) {
			scroller.get(level - 1).setScrollY(0);
			flipper.showPrevious();
		}
	}
}
