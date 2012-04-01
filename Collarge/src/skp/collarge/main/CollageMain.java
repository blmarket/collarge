package skp.collarge.main;

import skp.collage.mapview.MyMapView;
import skp.collarge.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class CollageMain extends Activity {
	
	ImageView leftImageButton;
	ImageView rightImageButton;
	ImageView groupImageView;
	private boolean leftImageButton_action = true;
	private boolean rightImageButton_action = true;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_collage);
        
        // 메뉴 애니매이션 효
        final Animation animation_moveRight = AnimationUtils.loadAnimation(this, R.anim.push_right_in);
        final Animation animation_moveLeft = AnimationUtils.loadAnimation(this, R.anim.push_left_out);
        
        leftImageButton = (ImageView)findViewById(R.id.table_leftbutton);
        rightImageButton = (ImageView)findViewById(R.id.table_rightbutton);
        groupImageView = (ImageView)findViewById(R.id.group_menu);
        
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
						Intent intent = new Intent(CollageMain.this,MyMapView.class);
						startActivity(intent);
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
}
