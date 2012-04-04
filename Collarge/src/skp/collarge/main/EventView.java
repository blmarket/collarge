package skp.collarge.main;

import skp.collarge.R;
import skp.collarge.event.EventManager;
import skp.collarge.event.IEvent;
import skp.collarge.viewer.mapview.MyMapView;
import skp.collarge.viewer.timeline.TimelineViewActivity;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EventView extends Activity {

	/*
	 * ImageView imageView1; BitmapFactory.Options options = new
	 * BitmapFactory.Options(); Animation animation_moveRight; Bitmap bMap;
	 */

	private static final int PICK_GALLERY = 0;

	protected static final ContextWrapper context = null;
	ImageView leftImageButton;
	ImageView rightImageButton;
	ImageView bookmarkButton;
	ImageView groupImageView;
	ImageView mapViewButton;
	ImageView timeViewButton;

	int eventNum;

	LinearLayout viewImageView;

	private boolean leftImageButton_action = true;
	private boolean rightImageButton_action = true;
	private boolean bookmarkButton_action = true;

	class OnItemClickHandler implements OnItemClickListener {
		
		private Uri tmp;
		private IEvent event;

		public OnItemClickHandler(IEvent event) {
			this.event = event;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			Toast.makeText(EventView.this, "Click" + position,
					Toast.LENGTH_SHORT).show();
		
			if (position == 0) // do add
			{
				addImage();
			} else {
			
			//Log.d("aaa", position + "" + event.getEventPhotoList().get(position-1));
			// PictureView로 Intent 넘겨주는 부분
			tmp = event.getEventPhotoList().get(position-1);
			
			Intent intent = new Intent(EventView.this,
					skp.collarge.pictureview.PictureView.class);
			intent.putExtra("dir", tmp.toString());
			startActivity(intent);			
			
			}
			
			
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_event);

		Intent intent = getIntent();
		eventNum = intent.getExtras().getInt("eventNumber");
		IEvent event = EventManager.getInstance().getEvent(eventNum);

		// Gridview 부분
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new EventImageAdapter(this, event));

		gridview.setOnItemClickListener(new OnItemClickHandler(event));

		gridview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ImageView starImage;
				starImage = (ImageView) arg1
						.findViewById(R.id.gridview_item_star);
				Boolean flag = (Boolean) starImage.getTag();
				boolean nextFlag = false;
				if (flag == null || flag.booleanValue() == false) {
					starImage.setImageDrawable(getResources().getDrawable(
							R.drawable.star_select));
					nextFlag = true;
				} else {
					starImage.setImageDrawable(getResources().getDrawable(
							R.drawable.star));
				}
				starImage.setTag(new Boolean(nextFlag));
				return false;
			}

		});

		// title bar 부분

		// 메뉴 애니매이션 효과
		final Animation animation_moveLeft_in = AnimationUtils.loadAnimation(
				this, R.anim.push_right_in);
		final Animation animation_moveLeft_out = AnimationUtils.loadAnimation(
				this, R.anim.push_left_out);
		final Animation animation_moveRight_in = AnimationUtils.loadAnimation(
				this, R.anim.push_left_in);
		final Animation animation_moveRight_out = AnimationUtils.loadAnimation(
				this, R.anim.push_right_out);

		leftImageButton = (ImageView) findViewById(R.id.table_leftbutton);
		rightImageButton = (ImageView) findViewById(R.id.table_rightbutton);
		bookmarkButton = (ImageView) findViewById(R.id.bookmark_menu);
		mapViewButton = (ImageView) findViewById(R.id.map_view_menu);
		timeViewButton = (ImageView) findViewById(R.id.time_view_menu);

		groupImageView = (ImageView) findViewById(R.id.group_menu);
		viewImageView = (LinearLayout) findViewById(R.id.view_menu);

		leftImageButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (leftImageButton_action) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						leftImageButton.setImageDrawable(getResources()
								.getDrawable(R.drawable.top_btn_left_on));
					} else {
						leftImageButton.setImageDrawable(getResources()
								.getDrawable(R.drawable.top_btn_left_over));
						groupImageView.setVisibility(View.VISIBLE);
						groupImageView.startAnimation(animation_moveLeft_in);
						leftImageButton_action = false;
					}
				} else {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						leftImageButton.setImageDrawable(getResources()
								.getDrawable(R.drawable.top_btn_left_normal));
						groupImageView.startAnimation(animation_moveLeft_out);
						groupImageView.setVisibility(View.INVISIBLE);
						leftImageButton_action = true;
					}
				}
				return true;
			}
		});

		rightImageButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (rightImageButton_action) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						rightImageButton
								.setImageDrawable(getResources().getDrawable(
										R.drawable.event_top_btn_right_on));
					} else {
						rightImageButton.setImageDrawable(getResources()
								.getDrawable(
										R.drawable.event_top_btn_right_over));
						viewImageView.setVisibility(View.VISIBLE);
						viewImageView.startAnimation(animation_moveRight_in);
						rightImageButton_action = false;
					}
				} else {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						rightImageButton.setImageDrawable(getResources()
								.getDrawable(
										R.drawable.event_top_btn_right_normal));
						viewImageView.startAnimation(animation_moveRight_out);
						viewImageView.setVisibility(View.INVISIBLE);
						rightImageButton_action = true;
					}
				}
				return true;
			}
		});

		bookmarkButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (bookmarkButton_action) {
					bookmarkButton.startAnimation(animation_moveLeft_out);
					bookmarkButton
							.setImageDrawable(getResources().getDrawable(R.drawable.bookmark));
					bookmarkButton.startAnimation(animation_moveLeft_in);
					bookmarkButton_action = false;
				} else {
					bookmarkButton.startAnimation(animation_moveLeft_out);
					bookmarkButton
					.setImageDrawable(getResources().getDrawable(R.drawable.bookmark_off));
					bookmarkButton.startAnimation(animation_moveLeft_in);
					bookmarkButton_action = true;
				}

			}
		});

		mapViewButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = new Intent(EventView.this, MyMapView.class);
				intent.putExtra("eventNuber", eventNum);
				startActivity(intent);
				return false;
			}
		});

		timeViewButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = new Intent(EventView.this, TimelineViewActivity.class);
				intent.putExtra("eventNumber", eventNum);
				startActivity(intent);
				return false;
			}
		});
		/*
		 * animation_moveRight = AnimationUtils.loadAnimation( this,
		 * R.anim.push_right_in); imageView1 =
		 * (ImageView)findViewById(R.id.imageview1); options.inSampleSize = 10;
		 * bMap = BitmapFactory.decodeFile(
		 * "/mnt/sdcard/DCIM/Camera/collarge/20120329_124707.jpg", options);
		 * Log.i("handler", ""+bMap); imageView1.setImageBitmap(bMap);
		 * 
		 * handler.sendEmptyMessage(0);
		 */

	}

	/*
	 * // 이미지 변환 핸들러 public Handler handler = new Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) { Log.d("myHandler",
	 * "tiktok"); imageView1.startAnimation(animation_moveRight); bMap =
	 * BitmapFactory
	 * .decodeFile("/mnt/sdcard/DCIM/Camera/collarge/20120329_122943.jpg",
	 * options); handler.sendEmptyMessageDelayed(0, 1000); }
	 * 
	 * };
	 */

	private void addImage() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK);
		startActivityForResult(intent, PICK_GALLERY);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_GALLERY:
			if (resultCode == RESULT_OK) {
				Uri resultData = data.getData();
				IEvent event = EventManager.getInstance().getEvent(eventNum);
				event.addData(resultData);

				// reset gridview...
				GridView gridview = (GridView) findViewById(R.id.gridview);
				gridview.setAdapter(new EventImageAdapter(this, event));
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
