package net.blmarket.timeline;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import skp.collarge.R;
import skp.collarge.image.DBCacheThumbnailBuilder;
import skp.collarge.image.IThumbnailBuilder;
import skp.collarge.image.MySimpleThumbnailBuilder;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class TimelineViewActivity extends Activity implements OnTouchListener,
		OnClickListener, OnItemSelectedListener {

	ViewFlipper flipper;
	ArrayList<ScrollView> scroller = new ArrayList<ScrollView>();
	// 터치 이벤트 발생 지점의 x좌표 저장
	float xAtDown, yAtDown;
	float xAtUp, yAtUp;
	ArrayList<ArrayList<Float>> levelpos;
	Gallery gallery;

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

		gallery = (Gallery) (findViewById(R.id.timeline_footer));
		gallery.setAdapter(new GalleryAdapter(this));
		gallery.setSpacing(30);
		gallery.setOnItemSelectedListener(this);
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
			ImageTextView imtv = new ImageTextView(this);
			imtv.setId(i);
			imtv.setTag(R.string.timeline_level, new Integer(level));
			imtv.setTag(R.string.vpos, new Integer(vpos));

			vpos = nextvpos;
			nextvpos = vpos + image.getHeight();
			imtv.setImageBitmap(image);
			imtv.setOnTouchListener(this);
			// imv.setOnClickListener(this);
			ll.addView(imtv);
		}
		builder.close();
		return result;
	}

	// FIXME: 좌우 플링할때 보고있는 View에 최대한 근접하게 보여주기
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			xAtDown = event.getX(); // 터치 시작지점 x좌표 저장
			yAtDown = event.getY();
			v.onTouchEvent(event);
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			xAtUp = event.getX(); // 터치 끝난지점 x좌표 저장
			yAtUp = event.getY();

			float xdiff = Math.abs(xAtUp - xAtDown);
			float ydiff = Math.abs(yAtUp - yAtDown);

			// 터치할 때 어딘가로 슬라이드 하지 않았다면...
			if (xdiff < 20 && ydiff < 20) {
				// 그리고 터치한 곳이 이미지 위라면...
				if (v instanceof ImageView)
					onClick(v); // 그 이미지를 클릭한 것처럼 작동하도록 함.
			}

			if (xdiff < 40)
				return false;
			if (ydiff > xdiff)
				return false;

			if (xAtUp < xAtDown) {
				if (flipper.getDisplayedChild() == flipper.getChildCount() - 1)
					return true;
				setFlipperPage(1);
				gallery.setSelection(flipper.getDisplayedChild());
				return true;
			} else if (xAtUp > xAtDown) {
				if (flipper.getDisplayedChild() == 0)
					return true;
				setFlipperPage(-1);
				gallery.setSelection(flipper.getDisplayedChild());
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
			setFlipperPage(-1);
			gallery.setSelection(flipper.getDisplayedChild());
		}
	}

	class GalleryAdapter extends BaseAdapter {
		private Context mContext;
		private String[] items = { "사진순", "5장단위", "25장단위" };

		public GalleryAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return 3; // FIXME: 아 싫다 이런 하드코딩
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(mContext);
			tv.setText(items[position]);
			tv.setMinimumHeight(50);
			tv.setGravity(Gravity.CENTER);
			return tv;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		flipper.setInAnimation(null);
		flipper.setOutAnimation(null);
		flipper.setDisplayedChild(arg2);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// do nothing
	}

	boolean setFlipperPage(int direction) {
		int target = flipper.getDisplayedChild() + direction;
		if (target < 0 || target >= flipper.getChildCount())
			return false;

		if (direction == 1) {
			// 왼쪽 방향 에니메이션 지정
			flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			// 다음 view 보여줌
			flipper.showNext();
		} else if (direction == -1) {
			// 오른쪽 방향 에니메이션 지정
			flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			// 전 view 보여줌
			flipper.showPrevious();
		} else {
			return false;
		}
		return true;
	}
}
