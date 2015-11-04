package com.ntt.uutravel.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.baidu.mobstat.StatService;
import com.ntt.uutravel.R;
import com.ntt.uutravel.common.AppActivityManager;
import com.ntt.uutravel.model.StoreDetail;
import com.ntt.uutravel.utils.JavaScriptObject;
import com.ntt.uutravel.utils.AppUpdate;

import android.R.color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;


public class WebBroswerRecommendActivity extends Activity  {

	
	WebView h5WebView;
	
	TextView txtTitle;
	
	Button btnClose;
	
	Context thisContext;
	
	ProgressBar progressPB;
	
	ImageButton imgBack;
	
	Activity h5Activity;
	
	//电话前缀   
	static final String TEL_PHONE_PREFIX="0081";

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
		AppActivityManager.addActivity(this);
		setContentView(R.layout.activity_webbroswer_recommend);
		
		thisContext=this;
		txtTitle=(TextView)findViewById(R.id.txtTitle);
		h5WebView= (WebView)findViewById(R.id.webBrowser);
		imgBack=(ImageButton)findViewById(R.id.imgBack);
	
		//imgSetup=(ImageView)findViewById(R.id.imgSetup);
		
//		btnShare=(ImageButton)findViewById(R.id.imgBtnShare);
//		btnFav=(ImageButton)findViewById(R.id.imgBtnFav);
		
		
		progressPB=(ProgressBar)findViewById(R.id.webBroswerPB);
		progressPB.setMax(100);
		progressPB.setProgress(0);
		
		h5WebView.getSettings().setDefaultTextEncodingName("utf-8");
		h5WebView.getSettings().setJavaScriptEnabled(true);
		//h5WebView.setBackgroundColor(Color.argb(0, 0, 0, 0));
		h5WebView.getSettings().setDomStorageEnabled(true);
		h5WebView.setSaveEnabled(false);
		h5WebView.getSettings().setUserAgentString(h5WebView.getSettings().getUserAgentString()+"/MsWebviewAndroid"); 
		h5WebView.getSettings().setDatabaseEnabled(true);
		h5WebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		h5WebView.getSettings().setBuiltInZoomControls(true);
		SMJsObject msBridge=new SMJsObject(this,h5WebView);
		h5WebView.addJavascriptInterface(msBridge, "NTT");	
		//h5WebView.getSettings().setBlockNetworkImage(true);
		
		  //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        String url="";
        if(bundle!=null){
        	 url = bundle.getString("url");
        }

		h5WebView.setWebViewClient(new WebViewClient(){       
            public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            	if(url.toLowerCase().startsWith("tel:"))
            	{
            	    int subStartIndex=4;
            		if(url.toLowerCase().startsWith("tel:0"))
            		{
            			subStartIndex=5;
            		}
            		
            		 String phoneNum="tel:"+TEL_PHONE_PREFIX+url.substring(subStartIndex, url.length());   		
            		 Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(phoneNum)); 
                     startActivity(intent); 
                     return true;
            	}
            	else
            	{
            		view.loadUrl(url);       
                return true;    
                }   
            } 

		});
		
		 WebChromeClient wvcc = new WebChromeClient() {  
	            @Override  
	            public void onReceivedTitle(WebView view, String title) {  
	                super.onReceivedTitle(view, title);  
	                txtTitle.setText(title);  
	            }  
	            
	            @Override  
	        	public void onProgressChanged(WebView view, int progress) {
	            	Log.i("PB",String.valueOf(progress));
	        		if (progress == 100) {
	        			progressPB.setVisibility(ProgressBar.GONE );
	                } else {
	                    if (progressPB.getVisibility() == ProgressBar.GONE){
	                    	progressPB.setVisibility(ProgressBar.VISIBLE);
	                    }
	                    progressPB.setProgress(progress);
	                }
	        		super.onProgressChanged(view, progress);  
	    	   }
	        };  
	    // 设置setWebChromeClient对象  
	    h5WebView.setWebChromeClient(wvcc);  	
	    
		h5WebView.loadUrl(url);	
		
		imgBack.setOnClickListener(new OnClickListener(){
			 public void onClick(View v) {  
				 	//v.setBackgroundColor(Color.parseColor("#fff1e6"));
				 	userGoback();
				 	
	            } 
		});
		
		
		
		
//		imgSetup.setOnClickListener(new OnClickListener(){
//			 public void onClick(View v) { 
//				 	 WebBroswerActivity.this.setResult(RESULT_OK+1, null);
//				 	 closeActivity();
//	            } 
//		});
		
		
//		btnShare.setOnClickListener(new OnClickListener(){
//			 public void onClick(View v) { 
//				 AppActivityManager.h5Activity.toH5Share();
////			 	 WebBroswerActivity.this.setResult(RESULT_OK+1, null);
////			 	 closeActivity();
//            } 
//			
//		});
		
		
//		btnFav.setOnClickListener(new OnClickListener(){
//			 public void onClick(View v) { 
//				 //v.setBackgroundColor(Color.parseColor("#fff1e6"));
//				 AppActivityManager.h5Activity.toH5Fav();
//				 //v.setBackgroundColor(Color.parseColor("#00000000"));
//				 //v.getBackground().clearColorFilter();
//           } 
//			
//		});
		
//		btnFav.setOnFocusChangeListener(new OnFocusChangeListener(){
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stub
//				if(hasFocus){
//					v.getBackground().setColorFilter(Color.GRAY,Mode.MULTIPLY);
//					
//				}else
//				{
//					v.getBackground().clearColorFilter();
//					
//				}
//			}
//
//		});
		
	}
	
    public void onResume() {
	       
        super.onResume();
        
        StatService.onResume(this);
    }

    public void onPause() {
        super.onPause();

        StatService.onPause(this);
    } 
	
	public void userGoback(){
		 if(h5WebView.canGoBack()){
			 h5WebView.goBack();
		 }else
		 {
			 WebBroswerRecommendActivity.this.setResult(RESULT_OK+2, null);
			 closeActivity();
		 }
		
	}
	
	public void closeActivity()
	{
		 Activity aty=	(WebBroswerRecommendActivity)thisContext;
		 h5WebView=null;
         aty.finish();
         aty=null;
	}
	
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
	    {   
	    	userGoback();
	    	return true; 
	    }	    
	    return super.onKeyDown(keyCode, event);  
	} 
	
	private String CreateUrl(String url) throws UnsupportedEncodingException
	{
		int urlIndexOf=url.indexOf("url=");
		int viewIndexOf=url.indexOf("view=");
		
		String urlParams=url.substring(urlIndexOf+4, viewIndexOf-1);
		String viewParams=url.substring(viewIndexOf+5, url.length());

		return "http://117.121.128.16:54322/Master/PlaceView?url="+URLEncoder.encode(urlParams)+"&view="+URLEncoder.encode(viewParams);
		
	}
	
	
	public class SMJsObject{
		Context _mContext;
		WebView _mWebView;
		
		public SMJsObject(Context mContext,WebView mWebview) {
			this._mContext = mContext;
			this._mWebView= mWebview;
		}  
		
		public void jump(
				String title,
				String content,
				String imgUrl,
				String linkUrl,
				String type,
				String id,
				String type2,
				String type3) throws UnsupportedEncodingException
		{
			StoreDetail detail=new StoreDetail();
			detail.Content=content;
			detail.Title=title;
			detail.ImgUrl=imgUrl;
			detail.LinkUrl=CreateUrl(linkUrl);
			detail.Type=type;
			detail.Type2=type2;
			detail.Type3=type3;
			detail.Id=id;
			detail.Lat=31.23;
			detail.Lng=121.47;

			Intent intent =new Intent(this._mContext,WebBroswerActivity.class);
			Bundle bundle=new Bundle();
			bundle.putSerializable("StoreDetail", detail);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		

		
		
	}
	
		
}
