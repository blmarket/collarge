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
    // ��ġ �̺�Ʈ �߻� ������ x��ǥ ����
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
	
    // View.OnTouchListener�� abstract method
    // flipper ���� �̺�Ʈ ������
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// ��ġ �̺�Ʈ�� �Ͼ �䰡 ViewFlipper�� �ƴϸ� return
		//if(v != flipper) return false;		
		
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			xAtDown = event.getX(); // ��ġ �������� x��ǥ ����
			System.out.println("xAtDown : " + xAtDown);
			v.onTouchEvent(event);
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			xAtUp = event.getX(); 	// ��ġ �������� x��ǥ ����
			System.out.println("xAtUp : " + xAtUp);
			
			if(Math.abs(xAtUp - xAtDown) < 40) return false;
			
			if( xAtUp < xAtDown ) {
				// ���� ���� ���ϸ��̼� ����
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
		        		R.anim.push_left_in));
		        flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
		        		R.anim.push_left_out));
		        		
		        // ���� view ������
				flipper.showNext();
				return true;
			}
			else if (xAtUp > xAtDown){
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
