package skp.collarge.main;

import skp.collarge.AllTheEvil;
import skp.collarge.R;
import skp.collarge.event.EventManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

public class CollargeMain extends Activity {

	ImageView leftImageButton;
	ImageView rightImageButton;
	ImageView groupImageView;

	private boolean leftImageButton_action = true;

	/*
	 * AlertDialog.Builder builder; static final int DIALOG_EPISODE_MAKE = 0;
	 */

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
		gridview.setAdapter(new ImageAdapter(this));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if (position == 0) {
					Intent intent = new Intent(CollargeMain.this,
							EventMakeMain.class);
					startActivityForResult(intent, 0);
					// addNewEpisode();
				} else {
					Toast.makeText(CollargeMain.this, "Enter Eposode",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(CollargeMain.this,
							EventView.class);
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

		rightImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CollargeMain.this, CollargeMain.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade_out, R.anim.hold);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// FIXME: add condition if requested activity succeeded or canceled.
		GridView gridview = (GridView) findViewById(R.id.gridview_collarge);
		gridview.setAdapter(new ImageAdapter(this));
		super.onActivityResult(requestCode, resultCode, data);
	}



}
