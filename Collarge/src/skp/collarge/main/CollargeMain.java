package skp.collarge.main;

import skp.collarge.AllTheEvil;
import skp.collarge.R;
import skp.collarge.event.EventManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class CollargeMain extends Activity {

	ImageView leftImageButton;
	ImageView rightImageButton;
	ImageView groupImageView;
	
	
	private boolean leftImageButton_action = true;
	private boolean rightImageButton_action = true;
	
/*	AlertDialog.Builder builder;
	static final int DIALOG_EPISODE_MAKE = 0;*/
	

	@Override
	protected void onDestroy() {
		EventManager.close();
		// should be closed after all other singletons closed
		AllTheEvil.close();
		super.onDestroy();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_collage);

		
		// Gridview & Listener
		GridView gridview = (GridView) findViewById(R.id.gridview_collarge);
		gridview.setAdapter(new ImageAdapter(this, null));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if (position == 0) {
					Intent intent = new Intent(CollargeMain.this, EventMakeMain.class);
					startActivity(intent);
					//addNewEpisode();
				} else {
					Toast.makeText(CollargeMain.this, "Enter Eposode", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(CollargeMain.this, EventView.class);
					intent.putExtra("eventNumber", position - 1);
					startActivity(intent);
					overridePendingTransition(R.anim.fade_out, R.anim.hold);
				}
			}
		}); // end GridView

		// menu animation
		final Animation animation_moveRight = AnimationUtils.loadAnimation(
				this, R.anim.push_right_in);
		final Animation animation_moveLeft = AnimationUtils.loadAnimation(this,
				R.anim.push_left_out);

		leftImageButton = (ImageView) findViewById(R.id.table_leftbutton);
		rightImageButton = (ImageView) findViewById(R.id.table_rightbutton);
		groupImageView = (ImageView) findViewById(R.id.group_menu);

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
						groupImageView.startAnimation(animation_moveRight);
						leftImageButton_action = false;
					}
				} else {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						leftImageButton.setImageDrawable(getResources()
								.getDrawable(R.drawable.top_btn_left_normal));
						groupImageView.startAnimation(animation_moveLeft);
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
						rightImageButton.setImageDrawable(getResources()
								.getDrawable(R.drawable.top_btn_right_on));
					} else {
						rightImageButton.setImageDrawable(getResources()
								.getDrawable(R.drawable.top_btn_right_over));

						// 즐겨찾기 메뉴 활성화
						rightImageButton_action = false;
					}
				} else {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						rightImageButton.setImageDrawable(getResources()
								.getDrawable(R.drawable.top_btn_right_normal));
						// 즐겨찾기 버튼 비활성화
						rightImageButton_action = true;
					}
				}
				return true;
			}
		});
		
		
	}

/*	private void addNewEpisode() {
		EventManager.getInstance().createEvent();
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));
	}*/

	/*@Override
	protected Dialog onCreateDialog(int id) {
		
	    switch(id) {
	    case DIALOG_EPISODE_MAKE:
	    	break;
	    }
	    Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.episode_dialog,(ViewGroup) findViewById(R.id.layout_root));
	
		Button confirmButton = (Button) findViewById(R.id.episode_dia_button1);
		Button cancleButton = (Button) findViewById(R.id.episode_dia_button2);
		
		confirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addNewEpisode();
				builder.create().cancel();
			}
		});
		
		cancleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		builder = new AlertDialog.Builder(this);
		builder.setView(layout);
		
	    return builder.create();
	}
	*/

	
}
