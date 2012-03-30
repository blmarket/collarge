package net.blmarket.timeline;

import java.util.ArrayList;

import skp.collarge.R;
import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

public class TimelineViewActivity extends Activity implements OnTouchListener {

	ViewFlipper flipper;
	ScrollView scroller;
	// ��ġ �̺�Ʈ �߻� ������ x��ǥ ����
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

		((TimelineView) findViewById(R.id.timeline_1)).setImages(
				getContentResolver(), uris);
		flipper = ((ViewFlipper) findViewById(R.id.timelineflipper));
		flipper.setOnTouchListener(this);
		scroller = (ScrollView) findViewById(R.id.timeline_scroll);
		scroller.setOnTouchListener(this);
	}

	// View.OnTouchListener�� abstract method
	// flipper ���� �̺�Ʈ ������
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// ��ġ �̺�Ʈ�� �Ͼ �䰡 ViewFlipper�� �ƴϸ� return
		// if(v != flipper) return false;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			xAtDown = event.getX(); // ��ġ �������� x��ǥ ����
			System.out.println("xAtDown : " + xAtDown);
			v.onTouchEvent(event);
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			xAtUp = event.getX(); // ��ġ �������� x��ǥ ����
			System.out.println("xAtUp : " + xAtUp);

			if (Math.abs(xAtUp - xAtDown) < 80)
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
}
