package skp.collarge.view;

import java.util.AbstractList;

import skp.collarge.AllTheEvil;
import skp.collarge.R;
import skp.collarge.event.IEvent;
import skp.collarge.event.IEvent.OnImageAddedListener;
import skp.collarge.thumbnail.DBCacheThumbnailBuilder;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher;

public class MultiImageView extends ViewSwitcher implements
		OnImageAddedListener {

	private IEvent event;
	private AnimateTimeRunner pendingFlip;
	private int oldIdx = -1;

	private void refreshViews() {
		int eventSize = 0;
		if (event != null)
			eventSize = event.getEventPhotoList().size();

		if (eventSize < 2) // nothing to animate
		{
			if (pendingFlip != null) {
				removeCallbacks(pendingFlip);
				pendingFlip = null;
			}
		}

		if (eventSize == 0) {
			this.removeAllViews();
			this.addView(makeView());
		} else {
			if (pendingFlip == null)
				pendingFlip = new AnimateTimeRunner(this);
			else
				removeCallbacks(pendingFlip);
			pendingFlip.run();
		}
	}

	public MultiImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		refreshViews();
	}

	public void setEvent(IEvent event, boolean resetView) {
		this.event = event;
		if (event != null)
			event.setOnImageAdded(this);
		this.oldIdx = -1;
		if (resetView == true) {
			removeAllViews();
			addView(makeView());
		}
		refreshViews();
	}

	/**
	 * @return null if randomized result has same result.
	 */
	View makeView() {
		ImageView imv = new ImageView(getContext());

		if (event == null || event.getEventPhotoList().size() == 0) {
			imv.setImageResource(R.drawable.noimage);
			imv.setLayoutParams(new FrameLayout.LayoutParams(240, 200));
			imv.setScaleType(ScaleType.CENTER_CROP);
			return imv;
		}

		AbstractList<Uri> list = event.getEventPhotoList();

		int idx = AllTheEvil.getInstance().getRandom().nextInt(list.size());
		if (idx == oldIdx)
			return null;
		oldIdx = idx;
		imv.setImageBitmap(new DBCacheThumbnailBuilder(getContext()).build(list
				.get(idx)));
		imv.setLayoutParams(new FrameLayout.LayoutParams(240, 200));
		imv.setScaleType(ScaleType.CENTER_CROP);

		return imv;
	}

	class AnimateTimeRunner implements Runnable {
		MultiImageView view;

		public AnimateTimeRunner(MultiImageView view) {
			this.view = view;
		}

		@Override
		public void run() {
			int current = view.getDisplayedChild();
			if (view.getChildCount() < 2) {
				View tmpView = view.makeView();
				if (tmpView != null)
					view.addView(tmpView);
				view.postDelayed(this, (AllTheEvil.getInstance().getRandom()
						.nextInt(5) + 4) * 500);
				return;
			}

			int[] animations = { R.anim.anticipate_interpolator,
					R.anim.push_right_out, R.anim.push_left_out };

			view.setOutAnimation(AnimationUtils.loadAnimation(
					view.getContext(), animations[AllTheEvil.getInstance()
							.getRandom().nextInt(animations.length)]));

			view.showNext();
			view.postDelayed(this, (AllTheEvil.getInstance().getRandom()
					.nextInt(4) + 4) * 500);
			view.removeViewAt(current);
		}
	}

	@Override
	public void OnImageAdded() {
		refreshViews();
	}
}
