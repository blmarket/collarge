package skp.collarge.main;

import skp.collarge.R;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    int width;
    int height;
   
    
	public ImageAdapter(Context c) {
		mContext = c;
		WindowManager wm = (WindowManager)mContext.getSystemService(mContext.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}
 
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
                  ImageView imageView;
                  if (convertView == null) {           // if it's not recycled, initialize some attributes
                                   imageView = new ImageView(mContext);
                                   imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
                                   imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                   imageView.setPadding(0, 0, 0, 0);
                  } else {
                                   imageView = (ImageView) convertView;
        }
                  imageView.setImageResource(mThumbIds[position]);
                  return imageView;
    }
 
    // references to our images
    private Integer[] mThumbIds = {
    		
    		R.drawable.newepisode, R.drawable.episode1, 
    		R.drawable.episode2, R.drawable.episode3, 
    		R.drawable.episode4, R.drawable.episode5,
    		R.drawable.episode3, R.drawable.episode2
    		
    			
    };
}