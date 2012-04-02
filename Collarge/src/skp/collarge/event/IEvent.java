package skp.collarge.event;

import java.util.AbstractList;

import android.net.Uri;
import android.view.View;

public interface IEvent {
	public AbstractList<Uri> getEventPhotoList();
	public View getThumbnailView();
}