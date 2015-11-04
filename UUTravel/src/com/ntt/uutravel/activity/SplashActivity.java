package com.ntt.uutravel.activity;



import java.util.List;

import com.baidu.mobstat.StatService;
import com.ntt.uutravel.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import com.igexin.sdk.PushManager;


public class SplashActivity extends Activity {
	// 延迟2秒
	private final int SPLASH_DISPLAY_LENGHT = 2000;   
	
    String startupPage="Index";
    String pageParams="";
	
	  @Override  
	   protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.activity_splashscreen);  

	        try
	        {

	        	PushManager.getInstance().initialize(this.getApplicationContext());
	        	
	        }
	        catch(Exception ex)
	        {
	        	Log.i("GETUI", ex.getMessage());
	        	
	        }
	        
	        Uri uriData= getIntent().getData();
	        if(uriData!=null){
	        	List<String> params = uriData.getPathSegments();
	        	//获取URL传递过来的参数
	        	if(params!=null && params.size()>0 )
	        	{
	        		pageParams=params.get(0);
	        	}
	        	
	        	//获取启动页面
	        	//startupPage=uriData.getHost();
	        	//if(startupPage==)

	        	Log.i("Url info", startupPage);
	        }
	        
	        new Handler().postDelayed(new Runnable() {  
	            public void run() {  
	                Intent mainIntent = new Intent(SplashActivity.this,  
	                        H5Activity.class); 
	                mainIntent.putExtra("url", startupPage+"?"+pageParams);
	                SplashActivity.this.startActivity(mainIntent);  
	                SplashActivity.this.finish();  
	            }  
	  
	        }, SPLASH_DISPLAY_LENGHT);  
	  
	    } 
	  
	  
	  
	  
//	    public void onResume() {
//	       
//	        super.onResume();
//	        
//	        StatService.onResume(this);
//	    }
//
//	    public void onPause() {
//	        super.onPause();
//
//	        StatService.onPause(this);
//	    }
}
