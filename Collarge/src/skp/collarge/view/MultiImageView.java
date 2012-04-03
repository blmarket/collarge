package skp.collarge.view;

import java.util.Random;

import skp.collarge.R;
import skp.collarge.event.IEvent;
import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher;
import android.widget.ViewSwitcher.ViewFactory;

public class MultiImageView extends ViewSwitcher implements ViewFactory {
	
	private IEvent event;
	private int index = 0;
	Random random = new Random();
	
	public MultiImageView(Context context, IEvent event) {
		super(context);
		this.event = event;
		this.addView(makeView());		
		postDelayed(new Zwitter(this), (random.nextInt(4)+4)*500);
	}

	@Override
	public View makeView() {

		// FIXME: get candidate list from somewhere, and shuffle that list.
		ImageView imv = new ImageView(getContext());
		
		imv.setImageURI(event.getEventPhotoList().get(index++));
		imv.setLayoutParams(new FrameLayout.LayoutParams(240, 200));
		imv.setScaleType(ScaleType.CENTER_CROP);
		if (index == event.getEventPhotoList().size())
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
			if(view.getChildCount() < 2)
				view.addView(view.makeView());
			// TODO: do proper animations.
			//view.setInAnimation(AnimationUtils.loadAnimation(view.getContext(),
			//		R.anim.push_up));
			view.setOutAnimation(AnimationUtils.loadAnimation(view.getContext(),
					R.anim.push_up));
			view.showNext();
			view.postDelayed(this, (random.nextInt(4)+4)*500);

			view.removeViewAt(current);
		}
	}
}
