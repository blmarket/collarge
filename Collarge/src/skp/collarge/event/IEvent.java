package skp.collarge.event;

import java.util.AbstractList;

import android.net.Uri;
import android.view.View;

public interface IEvent {
	public AbstractList<Uri> getEventPhotoList();
	public View getThumbnailView();
	public void addData(Uri item);
	public void setOnImageAdded(OnImageAddedListener listener);
	public void setEventName(String eventName);
	public void setEventPriod(String eventPriod);
	public String getEventName();
	public String getEventPriod();
	
	public interface OnImageAddedListener {
		public void OnImageAdded();
	}
}