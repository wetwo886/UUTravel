package com.ntt.uutravel.activity;

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
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.PopupWindow;
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


public class WebBroswerActivity extends Activity  {

	
	WebView h5WebView;
	
	TextView txtTitle;
	
	Button btnClose;
	
	Context thisContext;
	
	ProgressBar progressPB;
	
	ImageButton imgBack;
	
//	ImageButton imgSetup;
//	
//	ImageButton btnShare;
//	
//	ImageButton btnFav;
	
	Activity h5Activity;
	
	StoreDetail storeDetail;
	
	ImageButton imgBtnMore;
	
	PopupWindow popupWindow;
	
	//电话前缀   
	static final String TEL_PHONE_PREFIX="0081";

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
		AppActivityManager.addActivity(this);
		setContentView(R.layout.activity_webbroswer);
		
//		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//		view = mLayoutInflater.inflate(resId, null);
		

		initPopupWindow();
		
		
		thisContext=this;
		txtTitle=(TextView)findViewById(R.id.txtTitle);
		h5WebView= (WebView)findViewById(R.id.webBrowser);
		imgBack=(ImageButton)findViewById(R.id.imgBack);
		//imgSetup=(ImageView)findViewById(R.id.imgSetup);
		
//		btnShare=(ImageButton)findViewById(R.id.imgBtnShare);
//		btnFav=(ImageButton)findViewById(R.id.imgBtnFav);
		
		imgBtnMore=(ImageButton)findViewById(R.id.imgBtnMore);
		
		
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
		
		h5WebView.getSettings().setSupportZoom(true);
		h5WebView.getSettings().setBuiltInZoomControls(true);
		
		
		//h5WebView.getSettings().setBlockNetworkImage(true);
		
		  //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        String url="";
        if(bundle!=null){
        	 url = bundle.getString("url");
        	 Object p=bundle.getSerializable("StoreDetail");
        	 if(p!=null)
        	 {
        		 storeDetail=(StoreDetail)p;
        		 url=storeDetail.LinkUrl;
        	 }
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
		
		
		imgBtnMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!popupWindow.isShowing()) {
					//popupWindow.showAsDropDown(v);
					//popupWindow.showAtLocation(v, Gravity.RIGHT | Gravity.BOTTOM, 10,10);

					popupWindow.showAsDropDown(v,-10, 0);
					//popupWindow.showAtLocation(v,0, 0,200);
				}
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
//				 if(storeDetail==null){
//					 AppActivityManager.h5Activity.toH5Share();
//				 }else
//				 {
//					 AppActivityManager.h5Activity.toH5Share(storeDetail);		 
//				 }
////			 	 WebBroswerActivity.this.setResult(RESULT_OK+1, null);
////			 	 closeActivity();
//            } 
//			
//		});
//		
		
//		btnFav.setOnClickListener(new OnClickListener(){
//			 public void onClick(View v) { 
//				 if(storeDetail==null){
//				 //v.setBackgroundColor(Color.parseColor("#fff1e6"));
//					 AppActivityManager.h5Activity.toH5Fav();
//				 }
//				 else
//				 {
//					 AppActivityManager.h5Activity.toH5Fav(storeDetail);					 
//				 }
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
	
	
	private void initPopupWindow()
	{
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	    View view=mLayoutInflater.inflate(R.layout.popup_window_menu, null);
		popupWindow=new PopupWindow(view,  200, LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_frame));
		popupWindow.setOutsideTouchable(true);
		
		// 自定义动画
				// mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
				// 使用系统动画
		popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		popupWindow.update();
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		
	    	((Button)view.findViewById(R.id.btnFav)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 if(storeDetail==null){
						 AppActivityManager.h5Activity.toH5Fav();
					 }
					 else
					 {
						 AppActivityManager.h5Activity.toH5Fav(storeDetail);					 
					 }
					 popupWindow.dismiss();
					 
				}
			});
	    	
	    	
	    	((Button)view.findViewById(R.id.btnShare)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				 if(storeDetail==null){
					 AppActivityManager.h5Activity.toH5Share();
				 }else
				 {
					 AppActivityManager.h5Activity.toH5Share(storeDetail);		 
				 }
				 popupWindow.dismiss();
				}
			});
	    	
	    	
	    	((Button)view.findViewById(R.id.btnBack)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					popupWindow.dismiss();
					closeActivity();
				}
			});
	    	
//           ((Button)view.findViewById(R.id.btnShowMap)).setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					
//					if(storeDetail!=null){
//						Intent  intent =new Intent(thisContext,PlaceMapActivity.class);
//					    Bundle bundle=new Bundle();
//	          		    bundle.putDouble("lat", storeDetail.Lat);
//	          		    bundle.putDouble("lng",storeDetail.Lng);
//	          		    bundle.putString("title", storeDetail.Title);   
//	          		    intent.putExtras(bundle);
//	          		    startActivity(intent);
//					}
//					
//					popupWindow.dismiss();
//				
//				}
//			});
           
		
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
			 WebBroswerActivity.this.setResult(RESULT_OK+2, null);
			 closeActivity();
		 }
		
	}
	
	public void closeActivity()
	{
		 Activity aty=	(WebBroswerActivity)thisContext;
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
	
	
	
	
	
		
}
