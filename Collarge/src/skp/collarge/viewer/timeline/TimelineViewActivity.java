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

public class TimelineViewActivity extends Activity implements OnTouchListener,
		OnClickListener, OnItemSelectedListener {

	ViewFlipper flipper;
	ArrayList<ScrollView> scroller = new ArrayList<ScrollView>();
	// ��ġ �̺�Ʈ �߻� ������ x��ǥ ����
	float xAtDown, yAtDown;
	float xAtUp, yAtUp;
	ArrayList<ArrayList<Float>> levelpos;
	Gallery gallery;

	class ImageWithTime implements Comparable<ImageWithTime> {
		public ImageWithTime(Uri uri) {
			this.uri = uri;
			Cursor cursor = MediaStore.Images.Media.query(getContentResolver(),
					uri, null);
			while (cursor.moveToNext()) {
				this.date = new Long(cursor.getLong(cursor
						.getColumnIndex(Images.Media.DATE_ADDED)));
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
			for (Uri uri : EventManager.getInstance().getEvent(eventNum)
					.getEventPhotoList()) {
				uris.add(new ImageWithTime(uri));
			}
		}
		Collections.sort(uris);

		flipper = ((ViewFlipper) findViewById(R.id.timelineflipper));
		flipper.setOnTouchListener(this);

		levelpos = new ArrayList<ArrayList<Float>>();

		int pow = 1;
		for (int i = 0; i < 3; i++) {
			View tmp = getLayoutInflater().inflate(R.layout.timeline_scroll,
					null);
			ScrollView scrollView = (ScrollView) (tmp
					.findViewById(R.id.timeline_scrollview));

			flipper.addView(tmp);
			levelpos.add(setImages(scrollView.findViewById(R.id.timeline),
					uris, pow, i));
			pow *= 5;
			scrollView.setOnTouchListener(this);
			scroller.add(scrollView);
		}

		gallery = (Gallery) (findViewById(R.id.timeline_footer));
		gallery.setAdapter(new GalleryAdapter(this));
		gallery.setSpacing(30);
		gallery.setOnItemSelectedListener(this);
	}

	private ArrayList<Float> setImages(View v,
			AbstractList<ImageWithTime> uris, int step, int level) {
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
			imtv.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			imtv.setOnTouchListener(this);
			// imv.setOnClickListener(this);
			ll.addView(imtv);
			
			TextView tv = new TextView(this);
			tv.setText(DateFormat.format("MM/dd/yy h:mmaa", uris.get(i).date));
			tv.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			ll.addView(tv);
		}
		builder.close();
		return result;
	}

	// FIXME: �¿� �ø��Ҷ� �����ִ� View�� �ִ��� �����ϰ� �����ֱ�
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
			scroller.get(level - 1).setScrollY(
					levelpos.get(level - 1).get(v.getId()).intValue());
			setFlipperPage(-1);
			gallery.setSelection(flipper.getDisplayedChild());
		}
	}

	class GalleryAdapter extends BaseAdapter {
		private Context mContext;
		private String[] items = { "������", "5�����", "25�����" };

		public GalleryAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return 3; // FIXME: �� �ȴ� �̷� �ϵ��ڵ�
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
			// ���� ���� ���ϸ��̼� ����
			flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			// ���� view ������
			flipper.showNext();
		} else if (direction == -1) {
			// ������ ���� ���ϸ��̼� ����
			flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			// �� view ������
			flipper.showPrevious();
		} else {
			return false;
		}
		return true;
	}
}
