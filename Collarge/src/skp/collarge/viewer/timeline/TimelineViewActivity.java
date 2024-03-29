package skp.collarge.viewer.timeline;

import java.security.InvalidParameterException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;

import skp.collarge.R;
import skp.collarge.event.EventManager;
import skp.collarge.thumbnail.DBCacheThumbnailBuilder;
import skp.collarge.thumbnail.IThumbnailBuilder;
import skp.collarge.thumbnail.MySimpleThumbnailBuilder;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class TimelineViewActivity extends Activity implements OnTouchListener, OnClickListener, OnItemSelectedListener {

	ViewFlipper flipper;
	ArrayList<ScrollView> scroller = new ArrayList<ScrollView>();
	// 터치 이벤트 발생 지점의 x좌표 저장
	float xAtDown, yAtDown;
	float xAtUp, yAtUp;
	ArrayList<ArrayList<Float>> levelpos;
	Gallery gallery;

	class ImageWithTime implements Comparable<ImageWithTime> {
		public ImageWithTime(Uri uri) {
			this.uri = uri;
			Cursor cursor = MediaStore.Images.Media.query(getContentResolver(), uri, null);
			while (cursor.moveToNext()) {
				this.date = new Long(cursor.getLong(cursor.getColumnIndex(Images.Media.DATE_ADDED)));
				System.out.println("" + this.date + " " + DateFormat.format("MM/dd/yy", this.date * 1000));
				// DateFormat.format("MM/dd/yy h:mmaa", date);
			}
		}

		Uri uri;
		Long date;

		@Override
		public int compareTo(ImageWithTime another) {
			return date.compareTo(another.date);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		int eventNum = 0;
		System.out.println(getIntent());
		System.out.println(getIntent().getExtras());
		if (getIntent().getExtras() != null)
			eventNum = getIntent().getExtras().getInt("EventNumber");

		ArrayList<ImageWithTime> uris = new ArrayList<TimelineViewActivity.ImageWithTime>();

		if (EventManager.getInstance().getEventSize() <= eventNum) {
			throw new RuntimeException("invalid eventNum");
		} else {
			for (Uri uri : EventManager.getInstance().getEvent(eventNum).getEventPhotoList()) {
				uris.add(new ImageWithTime(uri));
			}
		}
		Collections.sort(uris);

		flipper = ((ViewFlipper) findViewById(R.id.timelineflipper));
		flipper.setOnTouchListener(this);

		levelpos = new ArrayList<ArrayList<Float>>();

		int pow = 1;
		for (int i = 0; i < 3; i++) {
			View tmp = getLayoutInflater().inflate(R.layout.timeline_scroll, null);
			ScrollView scrollView = (ScrollView) (tmp.findViewById(R.id.timeline_scrollview));

			flipper.addView(tmp);
			levelpos.add(setImages(scrollView.findViewById(R.id.timeline), uris, pow, i));
			pow *= 5;
			scrollView.setOnTouchListener(this);
			scroller.add(scrollView);
		}

		gallery = (Gallery) (findViewById(R.id.timeline_footer));
		gallery.setAdapter(new GalleryAdapter(this));
		gallery.setSpacing(30);
		gallery.setOnItemSelectedListener(this);
	}

	private ArrayList<Float> setImages(View v, AbstractList<ImageWithTime> uris, int step, int level) {
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
				result.add(new Float((nextvpos * mi + vpos * (step - mi)) / step));
				continue;
			}
			result.add(new Float(nextvpos));

			Uri item = uris.get(i).uri;
			Bitmap image;
			try {
				image = builder.build(item);
			} catch (Exception e) {
				image = null;
			}

			if (image == null)
				continue;
			ImageView imtv = new ImageView(this);
			imtv.setId(i);
			imtv.setTag(R.string.timeline_level, new Integer(level));
			imtv.setTag(R.string.vpos, new Integer(vpos));

			vpos = nextvpos;
			nextvpos = vpos + image.getHeight();
			imtv.setImageBitmap(image);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(240,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.gravity = Gravity.CENTER;
			imtv.setLayoutParams(layoutParams);

			imtv.setOnTouchListener(this);
			// imv.setOnClickListener(this);
			ll.addView(imtv);

			TextView tv = new TextView(this);
			tv.setText(DateFormat.format("MM/dd/yy h:mmaa", uris.get(i).date * 1000));
			tv.setLayoutParams(layoutParams);
			tv.setTextColor(getResources().getColor(R.color.film_yellow));
			ll.addView(tv);
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
		System.out.println(v);
		int level = ((Integer) v.getTag(R.string.timeline_level)).intValue();
		if (level > 0) {
			scroller.get(level - 1).setScrollY(levelpos.get(level - 1).get(v.getId()).intValue());
			setFlipperPage(-1);
			gallery.setSelection(flipper.getDisplayedChild());
		}
	}

	class GalleryAdapter extends BaseAdapter {
		private Context mContext;
		private int[] items = { R.drawable.timeview_btn1, R.drawable.timeview_btn2, R.drawable.timeview_btn3 };

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
			ImageView imv = new ImageView(mContext);
			imv.setImageDrawable(mContext.getResources().getDrawable(items[position]));
			return imv;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
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
			flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
			// 다음 view 보여줌
			flipper.showNext();
		} else if (direction == -1) {
			// 오른쪽 방향 에니메이션 지정
			flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
			// 전 view 보여줌
			flipper.showPrevious();
		} else {
			return false;
		}
		return true;
	}
}
