package skp.collarge.main;

import skp.collarge.R;
import skp.collarge.event.IEvent;
import skp.collarge.thumbnail.DBCacheThumbnailBuilder;
import skp.collarge.thumbnail.IThumbnailBuilder;
import skp.collarge.thumbnail.MySimpleThumbnailBuilder;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class EventImageAdapter extends BaseAdapter {

	private Context mContext;
	private IEvent event;
	View myView;
	private LayoutInflater mInflater;

	public EventImageAdapter(Context c, IEvent e) {
		mContext = c;
		event = e;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public IEvent getEvent() {
		return event;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		System.out.println("getCount() : " + event.getEventPhotoList().size());
		return event.getEventPhotoList().size() + 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View newView = null;
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			newView = mInflater.inflate(R.layout.event_gridview_layout, parent,
					false);
			holder.imageview = (ImageView) newView
					.findViewById(R.id.gridview_item_image);
			holder.checkbox = (ImageView) newView
					.findViewById(R.id.gridview_item_star);
			holder.imageview.setLayoutParams(new FrameLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			holder.imageview.setPadding(0, 0, 0, 0);
			holder.imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
			holder.checkbox.setVisibility(View.VISIBLE);
			newView.setTag(holder);
		} else {
			newView = convertView;
			holder = (ViewHolder) newView.getTag();
		}

		// GridView ø° ª—∑¡¡ÿ¥Ÿ.
		if (position == 0) {
			holder.imageview.setImageResource(R.drawable.new_picture);
			holder.checkbox.setVisibility(View.INVISIBLE);
		} else {
			ImageView imv = holder.imageview;

			Uri tmp = event.getEventPhotoList().get(position - 1);
			IThumbnailBuilder builder = new DBCacheThumbnailBuilder(mContext,
					new MySimpleThumbnailBuilder(mContext.getContentResolver()));
			Bitmap thumbnail = builder.build(tmp);

			imv.setImageBitmap(thumbnail);
			imv.setScaleType(ScaleType.CENTER_CROP);
			imv.setLayoutParams(new FrameLayout.LayoutParams(240, 200));
		}

		return newView;
	}

	class ViewHolder {
		ImageView imageview;
		ImageView checkbox;
	};

}
