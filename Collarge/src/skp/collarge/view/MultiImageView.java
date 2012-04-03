package skp.collarge.view;

import java.util.AbstractList;
import java.util.Random;

import skp.collarge.AllTheEvil;
import skp.collarge.R;
import skp.collarge.event.IEvent;
import skp.collarge.thumbnail.DBCacheThumbnailBuilder;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher;

public class MultiImageView extends ViewSwitcher {

	private IEvent event;
	private int index = 0;

	public MultiImageView(Context context, IEvent event) {
		super(context);
		this.event = event;
		this.addView(makeView());
		postDelayed(new Zwitter(this), 2000);
	}

	/**
	 * @return null if randomized result has same result.
	 */
	View makeView() {
		AbstractList<Uri> list = event.getEventPhotoList();
		Integer oldIdx = (Integer) getTag(R.string.hello);
		int idx = AllTheEvil.getInstance().getRandom().nextInt(list.size());
		if (oldIdx != null && idx == oldIdx.intValue())
			return null;
		setTag(R.string.hello, new Integer(idx));

		// FIXME: get candidate list from somewhere, and shuffle that list.
		ImageView imv = new ImageView(getContext());
		imv.setImageBitmap(new DBCacheThumbnailBuilder(getContext()).build(list
				.get(idx)));
		imv.setLayoutParams(new FrameLayout.LayoutParams(240, 200));
		imv.setScaleType(ScaleType.CENTER_CROP);
		if (index == list.size())
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
			if (view.getChildCount() < 2) {
				View tmpView = view.makeView();
				if (tmpView != null)
					view.addView(tmpView);
				view.postDelayed(this, 2000);
				return;
			}
			// TODO: do proper animations.
			// view.setInAnimation(AnimationUtils.loadAnimation(view.getContext(),
			// R.anim.push_left_in));
			int[] animations = { R.anim.push_up, R.anim.push_right_out,
					R.anim.push_left_out };
			view.setOutAnimation(AnimationUtils.loadAnimation(
					view.getContext(), animations[AllTheEvil.getInstance()
							.getRandom().nextInt(animations.length)]));
			view.showNext();
			view.postDelayed(this, 2000);

			view.removeViewAt(current);
		}
	}
}
