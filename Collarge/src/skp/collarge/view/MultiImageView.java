package skp.collarge.view;

import skp.collarge.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher;
import android.widget.ViewSwitcher.ViewFactory;

public class MultiImageView extends ViewSwitcher implements ViewFactory {
	private void init() {
		postDelayed(new Zwitter(this), 1000);
	}

	public MultiImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		setFactory(this);
	}

	public MultiImageView(Context context) {
		super(context);
		init();
		setFactory(this);
	}

	int[] inxs = { R.drawable.episode1, R.drawable.episode2,
			R.drawable.episode3, R.drawable.episode4, R.drawable.episode5, };
	private int index = 0;

	@Override
	public View makeView() {

		// FIXME: get candidate list from somewhere, and shuffle that list.
		ImageView imv = new ImageView(getContext());

		imv.setImageResource(inxs[index++]);
		imv.setLayoutParams(new ViewGroup.LayoutParams(240, 200));
		imv.setScaleType(ScaleType.CENTER_CROP);
		if (index == inxs.length)
			index = 0;

		return imv;
	}

	class Zwitter implements Runnable {
		MultiImageView view;

		public Zwitter(MultiImageView view) {
			this.view = view;
		}

		@Override
		public void run() {
			int current = view.getDisplayedChild();

			// TODO: do some animations
			view.showNext();
			view.postDelayed(this, 1000);

			view.removeViewAt(current);
		}
	}
}
