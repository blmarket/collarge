package skp.collarge.main;

import skp.collarge.R;
import skp.collarge.event.EventManager;
import skp.collarge.event.IEvent;
import skp.collarge.thumbnail.DBCacheThumbnailBuilder;
import skp.collarge.thumbnail.IThumbnailBuilder;
import skp.collarge.thumbnail.MySimpleThumbnailBuilder;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class EventImageAdapter extends BaseAdapter {

	private Context mContext;
	private IEvent event;

	public EventImageAdapter(Context c, IEvent e) {
		mContext = c;
		event = e;
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

		ImageView imageView = (ImageView) convertView;
		if (imageView == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			imageView.setPadding(0, 0, 0, 0);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		}

		if (position == 0) {
			imageView.setImageResource(R.drawable.newepisode);
		} else {
			ImageView imv = new ImageView(mContext);

			Uri tmp = event.getEventPhotoList().get(position - 1);
			IThumbnailBuilder builder = new DBCacheThumbnailBuilder(mContext, new MySimpleThumbnailBuilder(
					mContext.getContentResolver()));
			Bitmap thumbnail = builder.build(tmp);

			imv.setImageBitmap(thumbnail);
			imv.setScaleType(ScaleType.CENTER_CROP);
			imv.setLayoutParams(new AbsListView.LayoutParams(240, 200));
			// imv.setMinimumHeight(200);
			// imv.setMinimumWidth(240);
			return imv;
		}
		return imageView;
	}

}
