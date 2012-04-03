package skp.collarge.event;

import java.util.AbstractList;

import android.net.Uri;
import android.view.View;

public interface IEvent {
	public AbstractList<Uri> getEventPhotoList();
	public View getThumbnailView();
	public void addData(Uri item);
	public void setOnImageAdded(OnImageAddedListener listener);
	
	public interface OnImageAddedListener {
		public void OnImageAdded();
	}
}