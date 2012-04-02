package skp.collarge.viewer.timeline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageTextView extends ImageView {

	private String text;

	public ImageTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ImageTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		paint.setARGB(70, 255, 255, 255);
		// canvas.drawARGB(200, 255, 255, 255);
		// canvas.drawPaint(paint);
		canvas.drawRect(0, 100, width, height, paint);
		paint.setARGB(255, 0, 255, 255);
		paint.setTextSize(20);
		canvas.drawText("TODO: 적절한 시간", 0, 120, paint);

		// canvas.drawRect(0, height / 2, width, height, paint);
	}
}
