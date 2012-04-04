package skp.collarge.main;

import skp.collarge.R;
import skp.collarge.event.EventManager;
import skp.collarge.event.IEvent;
import skp.collarge.view.MultiImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;

	public ImageAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		return EventManager.getInstance().getEventSize() + 1;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {

		if (position == 0) {
			ImageView imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			imageView.setPadding(0, 0, 0, 0);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setImageResource(R.drawable.newepisode);
			return imageView;
		} else {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			RelativeLayout view = (RelativeLayout) inflater.inflate(
					R.layout.collarge_gridview_layout, null);
			
			IEvent event = EventManager.getInstance().getEvent(position - 1);
			((MultiImageView)view.findViewById(R.id.collarge_gridview_image)).setEvent(event, true);
			//miv.setLayoutParams(new GridView.LayoutParams(240, 200));
			return view;
		}
	}

}
