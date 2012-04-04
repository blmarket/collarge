package skp.collarge.pictureview;

import skp.collarge.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class PictureView extends Activity {
	ImageView imageview;
	WebView mWebView;
	
    private Context mContext = null;
    private final int imgWidth = 480;
    private final int imgHeight = 380;

    private String imgPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.pictureview);
		mWebView = (WebView)findViewById(R.id.webView1);
		mContext = this;

		imgPath=getIntent().getStringExtra("dir");
		

		        ImageView iv = (ImageView)findViewById(R.id.imageView);


		        iv.setImageURI(Uri.parse(imgPath));
		
		        mWebView.setInitialScale(100);
		        mWebView.getSettings().setJavaScriptEnabled(true);  // 웹뷰에서 자바스크립트실행가능
		        mWebView.loadUrl("http://rubric.cafe24.com/collarge/index.php?pictureid="+Uri.parse(imgPath));  // 구글홈페이지 지정
		        mWebView.setWebViewClient(new HelloWebViewClient());  // WebViewClient 지정 		        
		        
		        
		        
		
		/*
		findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buttonHandling1();
				
			}
			
			
			
		});
		
		*/
		
	}//on Create end
	

	
    private class HelloWebViewClient extends WebViewClient { 
        @Override 
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            return true; 
        } 
    }	
	
	
}
