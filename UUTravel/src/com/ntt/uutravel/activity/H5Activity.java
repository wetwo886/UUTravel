package com.ntt.uutravel.activity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.baidu.mobstat.StatService;
import com.ntt.uutravel.R;
import com.ntt.uutravel.model.JsResult;
import com.ntt.uutravel.model.StoreDetail;
import com.ntt.uutravel.utils.JavaScriptObject;
import com.ntt.uutravel.utils.AppUpdate;
import com.ntt.uutravel.common.AppActivityManager;
import com.ntt.uutravel.common.H5WebView;
import com.ntt.uutravel.common.JsonHelper;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;


public class H5Activity extends Activity  {
     
	//H5 WebApp 路径
	static final String H5_WEBAPP_PATH="file:///android_asset/webapp/index.html";
	//H5 WebApp 启动页
	static final String H5_WEBAPP_STARTUP_PAGE="#Index";		
	//是否启用自动更新
	static final Boolean ENABLED_AUTO_CHECK_UPDATE=true;

	//Menu菜单页面名称  
	static final String H5_MENU_PAGE_RECOMMENDLIST="#RecommendList";
	static final String H5_MENU_PAGE_NEARLIST="#NearList";
	static final String H5_MENU_PAGE_FAVORITELIST="#FavoriteList";
	   
	//电话前缀   
	static final String TEL_PHONE_PREFIX="0081";
	
	//浏览器对象    
	WebView h5WebView; 
	//js注入对象    
	JavaScriptObject msBridge;  
	
	long firstTime = 0;
	
	Context thisContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_h5);
		thisContext=this;
		
		//设置主启动页面的activity
		AppActivityManager.h5Activity=this;
				
		if(ENABLED_AUTO_CHECK_UPDATE){
			AppUpdate appUpdate=new AppUpdate(this,null);
			appUpdate.run();
		}
		
		  //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        String url="";
        if(bundle!=null){
        	 url ="#"+bundle.getString("url");
        }
        else
        {
        	url=H5_WEBAPP_STARTUP_PAGE;
        }
		
        this.startWebView(H5_WEBAPP_PATH+url);
        
        try
        {
        	configBaiduMtj();
        }
        catch( Exception ex)
        {
        	
        	
        }
	}
	
	
	@Override  
	protected void onResume() {  
	    super.onResume();  

	    
	} 
	
	
	protected void onNewIntent(Intent intent) {    
		 super.onNewIntent(intent);  
		 
		 Bundle bundle = intent.getExtras();  
		    String url="";
	        if(bundle!=null){
	        	String from=bundle.getString("fromNotificaiton");
	        	String redirectUrl=bundle.getString("url");
	        	if(from!=null && redirectUrl!=null && from.equalsIgnoreCase("Y"))
	        	{
	        		url =H5_WEBAPP_PATH+"#"+redirectUrl;
	        		loadUrl(url);
	        	}
	        }
		  
   }
	
	
	protected void configBaiduMtj()
	{
		
		StatService.setSessionTimeOut(30);
	}
	
	
	//启动webview
	protected void startWebView(String url)
	{   
		h5WebView=(WebView)findViewById(R.id.H5WebView);
		h5WebView.getSettings().setDefaultTextEncodingName("utf-8");
		h5WebView.getSettings().setJavaScriptEnabled(true);
		h5WebView.setBackgroundColor(Color.argb(0, 0, 0, 0));
		h5WebView.getSettings().setDomStorageEnabled(true);
		h5WebView.setSaveEnabled(false);
		h5WebView.getSettings().setUserAgentString(h5WebView.getSettings().getUserAgentString()+"/MsWebviewAndroid"); 
		h5WebView.getSettings().setDatabaseEnabled(true);
		h5WebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		//h5WebView.onScrollChanged(h5WebView.getScrollX(),h5WebView.getScrollY(),0,0); 
		//h5WebView.getSettings().setBlockNetworkImage(true);

		try{
			
			h5WebView.getSettings().setDatabasePath("/data/data/" + h5WebView.getContext().getPackageName() + "/databases/");
			//h5WebView.getSettings().setDatabasePath(this.getFilesDir().getPath()+ h5WebView.getContext().getPackageName() + "/databases/");
		}
		catch(Exception ex )
		{}
	
	
		WebViewClient nttwebViewClient=new WebViewClient(){  
		    public void onPageStarted(WebView view, String url, Bitmap favicon) {
		    	Log.i("URL", url);
		    }
		    
		    
		    public void onReceivedError(WebView view, int errorCode,
		            String description, String failingUrl) {
		    	
		    	Log.i("H5_ERROR", description);
		    	
		    }

			
            public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            	if(url.toLowerCase().startsWith("tel:"))
            	{
            		int startIndex=4;
            		if(url.toLowerCase().startsWith("tel:0"))
            		{
            			startIndex=5;
            		}
            		
           		    String phoneNum="tel:"+TEL_PHONE_PREFIX+url.substring(startIndex, url.length());   		
           		    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(phoneNum)); 
                    startActivity(intent); 

                     return true;
            	}
            	else if(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://"))
            	{
            		Intent intent ;
            		if(url.indexOf("MSRecommendFlag")>-1)
            		{
            			 intent =new Intent(thisContext,WebBroswerRecommendActivity.class);
            		}
            		else
            		{
                		 intent =new Intent(thisContext,WebBroswerActivity.class);
            		}
            		
            	    //用Bundle携带数据
          		    Bundle bundle=new Bundle();
          		    bundle.putString("url", url);
          		    intent.putExtras(bundle);
          		    //startActivity(intent);
          		    startActivityForResult(intent, 0);
            	
          		    return true;
            	}
            	else
            	{
            		view.loadUrl(url);       
            		return true;    
                }   
            }       
		};
		
		//h5WebView.setWebViewClient();
		msBridge=new JavaScriptObject(this,h5WebView);
		h5WebView.addJavascriptInterface(msBridge, "MsAndroidBridge");	
		
		//add baidu mtj
		StatService.bindJSInterface(this, h5WebView, nttwebViewClient);
		 
		
		//Toast.makeText(this, String.valueOf(getSdkVersion()),Toast.LENGTH_LONG).show();
		if(getSdkVersion()<15)
		{
			new AlertDialog
				.Builder(this)
				.setTitle("提示")
				.setMessage("本次APP仅仅适用于4.0.3以上版本的Android手机，点击确定后退出。")
				.setPositiveButton("确定",   
			        new DialogInterface.OnClickListener(){  
                		public void onClick(DialogInterface dialoginterface, int i){   
                			System.exit(0);  
                		}   
                    }).show();   
		}
		else
		{
			h5WebView.loadUrl(url);			
		}
		
	}
	
	public void loadUrl(String url)
	{
		h5WebView.loadUrl(url);	
	}

	public int getSdkVersion()
	{
		int version= android.os.Build.VERSION.SDK_INT;
		return version;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
	    {    	
	        String url=h5WebView.getUrl();
	       
	    	if ( url.indexOf(H5_WEBAPP_STARTUP_PAGE)>-1 ||
	    		 url.indexOf(H5_MENU_PAGE_RECOMMENDLIST)>-1 ||
	    		 url.indexOf(H5_MENU_PAGE_NEARLIST)>-1 ||
	    		 url.indexOf(H5_MENU_PAGE_FAVORITELIST)>-1 
	    		 ) {
				long secondTime = System.currentTimeMillis();
				if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
					Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
					firstTime = secondTime;// 更新firstTime
					return true;
				} else { // 两次按键小于2秒时，退出应用
					System.exit(0);
				}
			}    
	    		      	
	    	if(h5WebView.canGoBack() && event.getRepeatCount() == 0)
	    	{
	    		h5WebView.goBack();  
	 	        return true;    		    		
	    	}
	    
	    }	    
	    return super.onKeyDown(keyCode, event);  
	}  
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
//		if(RESULT_OK+1==resultCode)			
//		{
//			this.loadUrl(H5_WEBAPP_PATH+"#UserSetting");
//		}
		
	}
	
	public void toH5Fav()
	{
		String result="{\"success\":true,\"data\":\"\"}";
		this.h5WebView.loadUrl("javascript:window.MsJsBridge.callback('addFav','"+result+"')");	
		
	}
	
	public void toH5Fav(StoreDetail storeDetail)
	{
//		JsResult jsResult=new JsResult<StoreDetail>();
//		jsResult.data=storeDetail;
//		jsResult.success=true;
//		String result=JsonHelper.toJSON(jsResult);
//		String s=JsonHelper.toJSON(storeDetail);
		String result=getResult(storeDetail);
		Log.i("JSON",result);
		this.h5WebView.loadUrl("javascript:window.MsJsBridge.callback('addFavEx','"+result+"')");
	}
	
	    
	public void toH5Share()
	{
		String result="{\"success\":true,\"data\":\"\"}";
		this.h5WebView.loadUrl("javascript:window.MsJsBridge.callback('showWSShare','"+result+"')");	
	}
	
	public void toH5Share(StoreDetail storeDetail)
	{
//		JsResult jsResult=new JsResult<StoreDetail>();
//		jsResult.data=storeDetail;
//		jsResult.success=true;
//		String result=JsonHelper.toJSON(jsResult);
		String result=getResult(storeDetail);
		
		this.h5WebView.loadUrl("javascript:window.MsJsBridge.callback('showWSShareEx','"+result+"')");
	}
	
	private String getResult(StoreDetail storeDetail)
	{
		String result="{\"success\":true,\"data\":{\"place_link\":\""+storeDetail.LinkUrl+"\",\"place_name\":\""+
					    storeDetail.Title+"\",\"place_intro\":\""+storeDetail.Content+"\",\"place_id\":\""+
				        storeDetail.Id+"\",\"type\":\""+storeDetail.Type+"\",\"img\":\""+storeDetail.ImgUrl+"\",\"type2\":\""+
					    storeDetail.Type2+"\",\"type3\":\""+storeDetail.Type3+"\"}}";
		
		return result;
	}
	
	
		
}
