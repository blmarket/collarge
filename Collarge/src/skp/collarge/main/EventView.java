package skp.collarge.main;

import com.google.android.maps.MapActivity;

import skp.collarge.R;
import skp.collarge.event.EventManager;
import skp.collarge.viewer.mapview.MyMapView;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EventView extends Activity {

/*	ImageView imageView1;
	BitmapFactory.Options options = new BitmapFactory.Options();
	Animation animation_moveRight;
	Bitmap bMap;*/
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_event);
		
		Intent intent = getIntent();
		eventNum = intent.getExtras().getInt("eventNumber");
		
		
		// Gridview 부분
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new EventImageAdapter(this, EventManager.getInstance().getEvent(eventNum)));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Toast.makeText(EventView.this, "" + position, Toast.LENGTH_SHORT).show();
			}
		}); // GridView 끝

		// title bar 부분
		
		// 메뉴 애니매이션 효과
		final Animation animation_moveLeft_in = AnimationUtils.loadAnimation(this, R.anim.push_right_in);
		final Animation animation_moveLeft_out = AnimationUtils.loadAnimation(this, R.anim.push_left_out);
		final Animation animation_moveRight_in = AnimationUtils.loadAnimation(this, R.anim.push_left_in);
		final Animation animation_moveRight_out = AnimationUtils.loadAnimation(this, R.anim.push_right_out);
		final Animation animation_bookmark_out = AnimationUtils.loadAnimation(this, R.anim.push_bookmark_out);

		
		leftImageButton = (ImageView) findViewById(R.id.table_leftbutton);
		rightImageButton = (ImageView) findViewById(R.id.table_rightbutton);
		bookmarkButton = (ImageView) findViewById(R.id.bookmark_menu);
		mapViewButton = (ImageView) findViewById(R.id.map_view_menu);
		timeViewButton = (ImageView) findViewById(R.id.time_view_menu);
		bookmarkButton.scrollTo(200, 0);
		
		groupImageView = (ImageView) findViewById(R.id.group_menu);
		viewImageView = (LinearLayout)findViewById(R.id.view_menu);

		leftImageButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (leftImageButton_action) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						leftImageButton.setImageDrawable(getResources().getDrawable(R.drawable.top_btn_left_on));
					} else {
						leftImageButton.setImageDrawable(getResources().getDrawable(R.drawable.top_btn_left_over));
						groupImageView.setVisibility(View.VISIBLE);
						groupImageView.startAnimation(animation_moveLeft_in);
						leftImageButton_action = false;
					}
				} else {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						leftImageButton.setImageDrawable(getResources().getDrawable(R.drawable.top_btn_left_normal));
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
						rightImageButton.setImageDrawable(getResources().getDrawable(R.drawable.event_top_btn_right_on));
					} else {
						rightImageButton.setImageDrawable(getResources().getDrawable(R.drawable.event_top_btn_right_over));
						viewImageView.setVisibility(View.VISIBLE);
						viewImageView.startAnimation(animation_moveRight_in);
						rightImageButton_action = false;
					}
				} else {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						rightImageButton.setImageDrawable(getResources().getDrawable(R.drawable.event_top_btn_right_normal));
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
				if(bookmarkButton_action) {
					bookmarkButton.scrollTo(0, 0);
					bookmarkButton.startAnimation(animation_moveLeft_in);
					bookmarkButton_action=false;
				} else {
					bookmarkButton.scrollTo(200, 0);
					bookmarkButton.startAnimation(animation_bookmark_out);
					bookmarkButton_action=true;
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
				// TODO Auto-generated method stub
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

}
