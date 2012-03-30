package net.blmarket.timeline;

import java.util.ArrayList;

import skp.collarge.R;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

public class TimelineViewActivity extends Activity implements OnTouchListener {
	
	ViewFlipper flipper;
	ScrollView scroller;
    // 터치 이벤트 발생 지점의 x좌표 저장
    float xAtDown;
    float xAtUp;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		String contents[] = { "content://media/external/images/media/19",
				"content://media/external/images/media/20",
				"content://media/external/images/media/21",
				"content://media/external/images/media/22",
				"content://media/external/images/media/23",
				"content://media/external/images/media/24",
				"content://media/external/images/media/25",
				"content://media/external/images/media/26" };

		ArrayList<Uri> uris = new ArrayList<Uri>();

		for (String item : contents) {
			Uri ii = Uri.parse(item);
			uris.add(ii);
		}
		
		((TimelineView)findViewById(R.id.timeline_1)).setImages(getContentResolver(), uris);
		flipper = ((ViewFlipper)findViewById(R.id.timelineflipper));
		flipper.setOnTouchListener(this);
		scroller = (ScrollView)findViewById(R.id.timeline_scroll);
		scroller.setOnTouchListener(this);
	}
	
    // View.OnTouchListener의 abstract method
    // flipper 터지 이벤트 리스너
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 터치 이벤트가 일어난 뷰가 ViewFlipper가 아니면 return
		//if(v != flipper) return false;		
		
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			xAtDown = event.getX(); // 터치 시작지점 x좌표 저장
			System.out.println("xAtDown : " + xAtDown);
			v.onTouchEvent(event);
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			xAtUp = event.getX(); 	// 터치 끝난지점 x좌표 저장
			System.out.println("xAtUp : " + xAtUp);
			
			if(Math.abs(xAtUp - xAtDown) < 40) return false;
			
			if( xAtUp < xAtDown ) {
				// 왼쪽 방향 에니메이션 지정
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
		        		R.anim.push_left_in));
		        flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
		        		R.anim.push_left_out));
		        		
		        // 다음 view 보여줌
				flipper.showNext();
				return true;
			}
			else if (xAtUp > xAtDown){
				// 오른쪽 방향 에니메이션 지정
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
		        		R.anim.push_right_in));
		        flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
		        		R.anim.push_right_out));
		        // 전 view 보여줌
				flipper.showPrevious();
				return true;
			}
		}		
		return false;
	}	
}
