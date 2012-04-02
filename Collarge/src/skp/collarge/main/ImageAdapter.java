package skp.collarge.main;

import skp.collarge.R;
import skp.collarge.event.EventManager;
import skp.collarge.event.IEvent;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;

	public ImageAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		return EventManager.getInstance().getEventSize();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView imageView = (ImageView) convertView;
		if (imageView == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			imageView.setPadding(0, 0, 0, 0);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		}

		if (position == 0) {
			imageView.setImageResource(R.drawable.newepisode);
		} else {
			IEvent event = EventManager.getInstance().getEvent(position);
			return event.getThumbnailView();
		}
		return imageView;
	}
}