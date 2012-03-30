package net.blmarket.timeline;

import java.security.InvalidParameterException;
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
	// ��ġ �̺�Ʈ �߻� ������ x��ǥ ����
	float xAtDown, yAtDown;
	float xAtUp, yAtUp;
	ArrayList<ArrayList<Float>> levelpos;

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

		levelpos = new ArrayList<ArrayList<Float>>();
		levelpos.add(setImages(findViewById(R.id.timeline_1), uris, 1, 0));
		levelpos.add(setImages(findViewById(R.id.timeline_2), uris, 5, 1));
		levelpos.add(setImages(findViewById(R.id.timeline_3), uris, 25, 2));

		flipper = ((ViewFlipper) findViewById(R.id.timelineflipper));
		flipper.setOnTouchListener(this);

		scroller.add((ScrollView) findViewById(R.id.timeline_scroll_1));
		scroller.add((ScrollView) findViewById(R.id.timeline_scroll_2));
		scroller.add((ScrollView) findViewById(R.id.timeline_scroll_3));
		for (ScrollView view : scroller) {
			view.setOnTouchListener(this);
		}
	}

	private ArrayList<Float> setImages(View v, ArrayList<Uri> uris, int step,
			int level) {
		ArrayList<Float> result = new ArrayList<Float>();
		if (step == 0)
			throw new InvalidParameterException();
		LinearLayout ll = (LinearLayout) v;

		IThumbnailBuilder builder = new DBCacheThumbnailBuilder(this,
				new MySimpleThumbnailBuilder(getContentResolver()));

		int vpos = 0;
		int nextvpos = 0;
		for (int i = 0; i < uris.size(); i++) {
			int mi = (i % step);

			if (mi != 0) {
				result.add(new Float((nextvpos * mi + vpos * (step - mi))
						/ step));
				continue;
			}
			result.add(new Float(nextvpos));

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
			imv.setId(i);
			imv.setTag(R.string.timeline_level, new Integer(level));
			imv.setTag(R.string.vpos, new Integer(vpos));

			vpos = nextvpos;
			nextvpos = vpos + image.getHeight();
			imv.setImageBitmap(image);
			imv.setOnTouchListener(this);
			// imv.setOnClickListener(this);
			ll.addView(imv);
		}
		builder.close();
		return result;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			xAtDown = event.getX(); // ��ġ �������� x��ǥ ����
			yAtDown = event.getY();
			v.onTouchEvent(event);
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			xAtUp = event.getX(); // ��ġ �������� x��ǥ ����
			yAtUp = event.getY();

			float xdiff = Math.abs(xAtUp - xAtDown);
			float ydiff = Math.abs(yAtUp - yAtDown);

			// ��ġ�� �� ��򰡷� �����̵� ���� �ʾҴٸ�...
			if (xdiff < 20 && ydiff < 20) {
				// �׸��� ��ġ�� ���� �̹��� �����...
				if (v instanceof ImageView)
					onClick(v); // �� �̹����� Ŭ���� ��ó�� �۵��ϵ��� ��.
			}

			if (xdiff < 40)
				return false;
			if (ydiff > xdiff)
				return false;

			if (xAtUp < xAtDown) {
				// ���� ���� ���ϸ��̼� ����
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_out));

				// ���� view ������
				flipper.showNext();
				return true;
			} else if (xAtUp > xAtDown) {
				// ������ ���� ���ϸ��̼� ����
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_out));
				// �� view ������
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
			scroller.get(level - 1).setScrollY(
					levelpos.get(level - 1).get(v.getId()).intValue());
			flipper.showPrevious();
		}
	}
}
