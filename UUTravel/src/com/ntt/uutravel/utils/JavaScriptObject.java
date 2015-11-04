package com.ntt.uutravel.utils;

import java.util.HashMap;
import java.util.List;



import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


import com.baidu.mobstat.StatService;
import com.ntt.uutravel.R;
import com.ntt.uutravel.activity.DynamicActivity;
import com.ntt.uutravel.activity.H5Activity;
import com.ntt.uutravel.*;
import com.ntt.uutravel.utils.AppUpdate;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.Toast;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import cn.sharesdk.wechat.utils.WechatTimelineNotSupportedException;
import android.content.Context;





public class JavaScriptObject {
	Context _mContext;
	WebView _mWebView;
	GeoLocation mGeoLocation;  
	Handler mHandler;
	Handler mShowToastHandler;
	
    
	public JavaScriptObject(Context mContext,WebView mWebview) {
		this._mContext = mContext;
		this._mWebView= mWebview;
		
		
		mHandler = new Handler(){
			@Override   
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					showShareSDK(
							msg.getData().getString("title"), 
							msg.getData().getString("content"), 
							msg.getData().getString("imgUrl"), 
							msg.getData().getString("linkUrl"));
					break;
				}
			}  
		};   
		
		mShowToastHandler=new Handler(){
			@Override   
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				showToast(msg.getData().getString("msg"),"CENTER");
			}  
			
		};
		
	}  
   
	
	
	/**
	 * 检查更新
	 * **/
	Handler updateHandler=new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			AppUpdate appUpdate=new AppUpdate(_mContext,updateCallbackHandler);
			appUpdate.run();
		}
	};
	
	Handler updateCallbackHandler=new Handler(){
		@Override   
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				executeJsFunction("checkUpdate",createCallBackJson("\"YES\"",true));
				break;
			case 2:
				executeJsFunction("checkUpdate",createCallBackJson("\"NO\"",true));
			}
		}  		
	};
	
	
	Handler geoLocationHandler=new Handler(){
		@Override   
		public void handleMessage(Message msg) {
			String result=createCallBackJson("",false);;
			Bundle bd=msg.getData();
			Parcelable p=(Parcelable) bd.get("LOCATION");
			String locationType=bd.getString("LOCATIONTYPE");
			
			
			
			if(p!=null){
				Location location=(Location)p;
				// 获取维度信息
				double latitude = location.getLatitude();
				// 获取经度信息
				double longitude = location.getLongitude();		
				result=createCallBackJson("{\"lat\":\""+Double.toString(latitude)+"\",\"lng\":\""+Double.toString(longitude)+"\"}",true);		
			}
			
			//showToast("TYPE:"+locationType+","+result,"CENTER");
			
			//Log.i("LOCATION", "TYPE:"+locationType+","+result);
			executeJsFunction("getGeolocation", result);
		}  		
		
	};
	
	
	/** 得到当前位置信息 （经纬度） **/
	public void getGeolocation() {
		
		if(this.mGeoLocation==null){
			
			this.mGeoLocation=new GeoLocation(this._mContext,geoLocationHandler);
		}
		
		this.mGeoLocation.run();
		
//		LocationManager lm = (LocationManager) this._mContext.getSystemService(Context.LOCATION_SERVICE);
//		// 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
//		List<String> lp = lm.getAllProviders();
//
//		Criteria criteria = new Criteria();
//		criteria.setCostAllowed(false);
//		// 设置位置服务免费
//		criteria.setAccuracy(Criteria.ACCURACY_COARSE); // 设置水平位置精度
//		criteria.setAltitudeRequired(false);
//	    criteria.setBearingRequired(false);
//	    criteria.setCostAllowed(true);
//	    criteria.setPowerRequirement(Criteria.POWER_LOW);
//		String providerName = lm.getBestProvider(criteria, true);
//		String result="";
//		
//		 try  {  
//			 
//			 
//			 int maxTryTimes=20;
//			 int tryTimes=1;
//			 Location location = lm.getLastKnownLocation(providerName);
//			 while(location==null && tryTimes<=maxTryTimes)
//			 {
//				  location = lm.getLastKnownLocation(providerName);
//				  tryTimes++;
//				  //休眠5秒
//				  Thread.sleep(5000);
//			 }
////			 		 
//			 if (location!= null) {			
//					// 获取维度信息
//					double latitude = location.getLatitude();
//					// 获取经度信息
//					double longitude = location.getLongitude();		
//					 result=this.createCallBackJson("{\"lat\":\""+Double.toString(latitude)+"\",\"lng\":\""+Double.toString(longitude)+"\"}",true);		
//				} else {
//					 result=this.createCallBackJson("",false);
//				}
//	        } catch  (Exception e) {  
//	        	result=this.createCallBackJson("",false);
//	        } finally  {  
//	        	this.executeJsFunction("getGeolocation", result);
//	     }  

	}
	

    /**调用分享**/
	public void showShare(String title,String content,String imgUrl,String linkUrl) {
		
		if(linkUrl.equalsIgnoreCase("http://www.nttapt.com.cn"))
		{
			StatService.onEvent(_mContext, "AppShare", "pass");
		}
		else
		{
			StatService.onEvent(_mContext, "PlaceShare", "pass");
		}
		
		Message msg = new Message();
		msg.what = 1;
		Bundle b = new Bundle();
		b.putString("title",title);
		b.putString("content",content);
		b.putString("imgUrl",imgUrl);
		b.putString("linkUrl",linkUrl);
		msg.setData(b);		
		mHandler.sendMessage(msg);
   }
	
	/**显示系统Toast**/
	public void showToast(String msg,String position)
	{
		Toast toast = Toast.makeText(this._mContext,msg, Toast.LENGTH_SHORT);
		if(position.equals("CENTER")){
			toast.setGravity(Gravity.CENTER, 0, 0);
		}
		toast.show();
	}
	
	public void closeActivity()
	{
		android.app.Activity  aty=(android.app.Activity)this._mContext;
		aty.finish();
	}
	
	
	public void openActivity(String url)	
	{
		//获取url中Activity名称
		String hashOfActivityName="";
		if(url.indexOf("?")>-1){	
			hashOfActivityName=url.substring(0, url.indexOf("?"));
		}
		else
		{
			hashOfActivityName=url;	
		}
		
		hashOfActivityName="com.ntt.uutravel.activity."+hashOfActivityName+"Activity";
		
		try{
			Class<?> atyClass=null;
		
			try{
			 atyClass=Class.forName(hashOfActivityName);
			}
			catch(Exception ex)
			{
				atyClass=DynamicActivity.class;
			}
			
			if(atyClass==null){
				atyClass=DynamicActivity.class;
			}
			
		    Intent intent =new Intent(this._mContext,atyClass);
		    
		    //用Bundle携带数据
		    Bundle bundle=new Bundle();
		    
		    bundle.putString("url", "#"+url);
		    intent.putExtras(bundle);
		    
		    //android.app.Activity  aty=(android.app.Activity)this._mContext;
		    this._mContext.startActivity(intent);
		    //aty.overridePendingTransition(R.anim.ani_in,R.anim.ani_out); 
	    }
		catch(Exception ex)
		{
			Log.i("OPENACTIVITY", ex.getMessage());
			this.showToast("Sorry,["+hashOfActivityName+"] is not found!", "CENTER");
		}
		
	}
	
	public void confirm(String msg,String title)
	{
		new AlertDialog.Builder(this._mContext)
		.setTitle(title)
		.setMessage(msg)
		.setPositiveButton("确定",  
	        new DialogInterface.OnClickListener(){  
        		public void onClick(DialogInterface dialoginterface, int i){  
        			String result=createCallBackJson("\"Y\"",true);
        			executeJsFunction("confirm",result);
        		}  
            })
        .setNegativeButton("取消", 
        	new DialogInterface.OnClickListener(){  
        		public void onClick(DialogInterface dialoginterface, int i){   
        			String result=createCallBackJson("\"N\"",true);
        			executeJsFunction("confirm",result);
        		}   
            })
        .show();   
		
	}
	
	/**
	 * 检查更新
	 * **/
	public void checkUpdate()
	{
		Message msg = new Message();
		msg.what = 1;
		updateHandler.sendMessage(msg);
	}
	
     	
	private void showShareSDK(String title,String content,String imgUrl,String linkUrl) {
		
        ShareSDK.initSDK(this._mContext);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();  
        // 分享时Notification的图标和文字
        oks.setNotification(R.drawable.ic_launcher, this._mContext.getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(linkUrl);        
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath(imgUrl);
        oks.setImageUrl(imgUrl);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(linkUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(this._mContext.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(linkUrl);
        
        oks.setShareContentCustomizeCallback(new UUTravelShareContentCustomizeCallback());
        
        final String shareUrl=linkUrl;
        oks.setCallback(new PlatformActionListener(){

			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				
				
				if(shareUrl.equalsIgnoreCase("http://www.nttapt.com.cn"))
				{
					StatService.onEvent(_mContext, "AppShareSuccess", "pass");
				}
				else
				{
					StatService.onEvent(_mContext, "PlaceShareSuccess", "pass");
				}
				
				
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putString("msg","分享成功");
				msg.setData(b);
				mShowToastHandler.sendMessage(msg);
				
			}

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				
				String text="";
				
				if(arg2 instanceof WechatClientNotExistException)
				{
					text="对不起，您还没有安装微信客户端或者微信版本过低";
				}
				else
				{
					text="分享失败";
					
				}

				Message msg = new Message();
				Bundle b = new Bundle();
				b.putString("msg",text);
				msg.setData(b);
				mShowToastHandler.sendMessage(msg);
			}
        });
        // 启动分享GUI
        oks.show(this._mContext);
   }
	
	
	/**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */ 
    public static final boolean isOPen(final Context context) { 
        LocationManager locationManager  
                                 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); 
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快） 
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); 
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位） 
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); 
        if (gps || network) { 
            return true; 
        }   
        return false; 
    }
    
    
    /**
     * 强制帮用户打开GPS
     * @param context
     */ 
    public static final void openGPS(Context context) { 
        Intent GPSIntent = new Intent(); 
        GPSIntent.setClassName("com.android.settings", 
                "com.android.settings.widget.SettingsAppWidgetProvider"); 
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE"); 
        GPSIntent.setData(Uri.parse("custom:3")); 
        try { 
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send(); 
        } catch (CanceledException e) { 
            e.printStackTrace(); 
        } 
    }
	
    
    public void resizeWindow()
    {
    	this.executeJsFunction("resizeWindow", this.createCallBackJson("",true));    	
    }
	
		
	//创建回调结果
	public String createCallBackJson(String data,Boolean isSuccess)
	{
		if(isSuccess){
			return "{\"success\":true,\"data\":"+data+"}";
		}
		else
		{
			return "{\"success\":false,\"data\":\"\"}";			
		}	
	}
	
	//执行js回调
	public void executeJsFunction(String event,String data)
	{
		this._mWebView.loadUrl("javascript:window.MsJsBridge.callback('"+event+"','"+data+"')");	
	}
	
	

	

}
