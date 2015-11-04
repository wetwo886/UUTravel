package com.ntt.uutravel.common;

import com.ntt.uutravel.R;
import com.ntt.uutravel.activity.WebBroswerActivity;
import com.ntt.uutravel.utils.AppUpdate;
import com.ntt.uutravel.utils.JavaScriptObject;
import com.ntt.uutravel.common.AppActivityManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.Toast;

public class BaseH5Activity extends Activity {
	//H5 WebApp 路径
		static final String H5_WEBAPP_PATH="file:///android_asset/webapp/debug.html";
		//H5 WebApp 启动页
		static final String H5_WEBAPP_STARTUP_PAGE="#Index";		
		//是否启用自动更新
		static final Boolean ENABLED_AUTO_CHECK_UPDATE=false;
		      
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
			AppActivityManager.addActivity(this);
			thisContext=this;  
			if(ENABLED_AUTO_CHECK_UPDATE){
				AppUpdate appUpdate=new AppUpdate(this,null);
				appUpdate.run();
			}
			
			  //新页面接收数据
	        Bundle bundle = this.getIntent().getExtras();
	        String url="";
	        if(bundle!=null){
	        	 url = bundle.getString("url");
	        }
	        else
	        {
	        	url=H5_WEBAPP_STARTUP_PAGE;
	        }
			
	        this.startWebView(H5_WEBAPP_PATH+url);
	     
		}
		
		
		@Override  
		protected void onResume() {  
		    super.onResume();  
//		    if(msBridge!=null)
//		    {
//		    	msBridge.resizeWindow();	
//		    }
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
			//h5WebView.getSettings().setBlockNetworkImage(true);

			try{
				
				h5WebView.getSettings().setDatabasePath("/data/data/" + h5WebView.getContext().getPackageName() + "/databases/");
				//h5WebView.getSettings().setDatabasePath(this.getFilesDir().getPath()+ h5WebView.getContext().getPackageName() + "/databases/");
			}
			catch(Exception ex )
			{}
		
					
			h5WebView.setWebViewClient(new WebViewClient(){       
	            public boolean shouldOverrideUrlLoading(WebView view, String url) { 
	            	if(url.toLowerCase().startsWith("tel:"))
	            	{
	            		 String phoneNum="tel:"+TEL_PHONE_PREFIX+url.substring(4, url.length());   		
	            		 Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(phoneNum)); 
	                     startActivity(intent); 
	                     return true;
	            	}
	            	else if(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://"))
	            	{
	            		Intent intent =new Intent(thisContext,WebBroswerActivity.class);
	          		    //用Bundle携带数据
	          		    Bundle bundle=new Bundle();
	          		    bundle.putString("url", url);
	          		    intent.putExtras(bundle);
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
			msBridge=new JavaScriptObject(this,h5WebView);
			h5WebView.addJavascriptInterface(msBridge, "MsAndroidBridge");	
			
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

		public int getSdkVersion()
		{
			int version= android.os.Build.VERSION.SDK_INT;
			return version;
		}
		
		public boolean onKeyDown(int keyCode, KeyEvent event) {  
		    if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
		    {    	
		        String url=h5WebView.getUrl();
		        //Pattern p = Pattern.compile(strPattern);   
		        //Matcher m = p.matcher(url);   
		        //boolean s=m.matches();
		       
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
						AppActivityManager.exitClient(this);
						//System.exit(0);
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
		
}
