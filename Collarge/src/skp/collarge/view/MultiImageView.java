package skp.collarge.view;

import java.util.AbstractList;

import skp.collarge.AllTheEvil;
import skp.collarge.R;
import skp.collarge.event.IEvent;
import skp.collarge.thumbnail.DBCacheThumbnailBuilder;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher;

public class MultiImageView extends ViewSwitcher {

	private IEvent event;

	public MultiImageView(Context context, IEvent event) {
		super(context);
		this.event = event;
		this.addView(makeView());
		postDelayed(new Zwitter(this), (AllTheEvil.getInstance().getRandom()
				.nextInt(4) + 4) * 500);
	}

	/**
	 * @return null if randomized result has same result.
	 */
	View makeView() {
		AbstractList<Uri> list = event.getEventPhotoList();
		ImageView imv = new ImageView(getContext());
		if (list.size() == 0) {
			imv.setImageResource(R.drawable.noimage);
		} else {
			Integer oldIdx = (Integer) getTag(R.string.hello);
			int idx = AllTheEvil.getInstance().getRandom().nextInt(list.size());
			if (oldIdx != null && idx == oldIdx.intValue())
				return null;
			setTag(R.string.hello, new Integer(idx));
			imv.setImageBitmap(new DBCacheThumbnailBuilder(getContext())
					.build(list.get(idx)));
		}
		imv.setLayoutParams(new FrameLayout.LayoutParams(240, 200));
		imv.setScaleType(ScaleType.CENTER_CROP);

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
				view.postDelayed(this, (AllTheEvil.getInstance().getRandom()
						.nextInt(4) + 4) * 500);
				return;
			}
			// TODO: do proper animations.
			// view.setInAnimation(AnimationUtils.loadAnimation(view.getContext(),
			// R.anim.push_left_in));
			int[] animations = { R.anim.anticipate_interpolator, R.anim.push_right_out, R.anim.push_left_out };

			view.setOutAnimation(AnimationUtils.loadAnimation(
					view.getContext(), animations[AllTheEvil.getInstance()
							.getRandom().nextInt(animations.length)]));

			view.showNext();
			view.postDelayed(this, (AllTheEvil.getInstance().getRandom()
					.nextInt(4) + 4) * 500);
			view.removeViewAt(current);
		}
	}
}
