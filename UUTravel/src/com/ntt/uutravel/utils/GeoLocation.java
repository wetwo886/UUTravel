package com.ntt.uutravel.utils;


  

import android.content.Context;  
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;


public class GeoLocation {
	
	Handler mHandler;
	Context mContext;
	LocationManager mLocationManager; 
	Criteria mCriteria;
	Boolean isNetworkLocation;
	
//	
//    public class GeoLocationTask implements Runnable{
//   	 
//   	 public void run()
//   	 {
//   		 try{
//   			execute();
//   		 }
//   		 catch(Exception e){
//   			 e.printStackTrace();
//   		 }
//   		 
//   	 }
//    }
	
	
	public GeoLocation(Context context,Handler handler){
		this.mHandler=handler;
		this.mContext=context;
	}
	
	private void init()
	{
		this.mLocationManager = (LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE);
		this.mCriteria = new Criteria();
		this.mCriteria.setCostAllowed(false);
		this.mCriteria.setAccuracy(Criteria.ACCURACY_COARSE); // 设置水平位置精度
		this.mCriteria.setAltitudeRequired(false);
		this.mCriteria.setBearingRequired(false);
		this.mCriteria.setCostAllowed(true);
	    this.mCriteria.setPowerRequirement(Criteria.POWER_LOW);
	    this.mCriteria.setSpeedRequired(false);
	    this.isNetworkLocation=false;
	    
//		this.isLocationSuccess=false;
//		this.isLocationError=false;
		
	}
	
	public void run()
	{
//		GeoLocationTask task=new GeoLocationTask();
//		new Thread(task).start();
		this.execute();
	
	}
	
	
	private void execute()
	{	
		this.init();
		
		this.startGPSLocation();
		
//		//首先获取最后一次定位信息
//		Location location=this.getLastLocation();		
//		
//		//GPS定位
//		this.startGPSLocation();
//		
//		location=this.getLastLocation();
//		
//		while(location==null && !isLocationError){
//			location=this.getLastLocation();
//		}
//		
//		this.endLocation();
//		
//		if(location!=null)
//		{
//			this.locationCallback(location,"GPSLocation");
//			return;
//		}
//		
//		this.startNetworkLocation();
//		location=this.getLastLocation();	
//		while(location==null && !isLocationError){
//			location=this.getLastLocation();
//		}
//		
//		this.endLocation();
//		
//		this.locationCallback(location,"NetworkLocation");
//		
	}
	
	
	private void startNetworkLocation()
	{
		if(!this.isNetworkLocation){
			this.isNetworkLocation=true;
			String providerName = this.mLocationManager.getBestProvider(this.mCriteria, true);
			this.mLocationManager.requestLocationUpdates(providerName, 3000, 0, networkLocationListener);
		}
		
	}
	
	private void startGPSLocation()
	{
		 String providerName = this.mLocationManager.getProvider(LocationManager.GPS_PROVIDER).getName();
		 this.mLocationManager.requestLocationUpdates(providerName, 3000, 0, gpsLocationListener);		
	}
	
//	private void endLocation(){
//		try{
//			this.isLocationError=false;
//			this.isLocationSuccess=false;
//			this.mLocationManager.removeUpdates(gpsLocationListener);
//			this.mLocationManager.removeUpdates(networkLocationListener);
//		}
//		catch(Exception ex){}
//	}
	
	
	 //创建位置监听器
    private LocationListener networkLocationListener = new LocationListener(){
        //位置发生改变时调用
        @Override
        public void onLocationChanged(Location location) {
        	mLocationManager.removeUpdates(gpsLocationListener);
        	locationCallback(location,"LOCATION_NETWORK");
        }

        //provider失效时调用
        @Override
        public void onProviderDisabled(String provider) {
            //Log.d("Location", "onProviderDisabled");
        	mLocationManager.removeUpdates(gpsLocationListener);
        	isNetworkLocation=false;
        	locationCallback(null,"LOCATION_NETWORK");
        }

        //provider启用时调用
        @Override
        public void onProviderEnabled(String provider) {
            //Log.d("Location", "onProviderEnabled");
        }

        //状态改变时调用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //Log.d("Location", "onStatusChanged");
        	//mLocationManager.removeUpdates(gpsLocationListener);
        	//isNetworkLocation=false;
        	
        }
    };
	
    
    private LocationListener gpsLocationListener = new LocationListener(){
        //位置发生改变时调用
        @Override
        public void onLocationChanged(Location location) {
            //Log.d("Location", "onLocationChanged");
        	//isLocationSuccess=true;
        	//endLocation();
        	
        	mLocationManager.removeUpdates(gpsLocationListener);
        	if(location!=null)
        	{
        		locationCallback(location,"LOCATION");
        	}
        	else
        	{
        		//失败，启用网络定位
        		startNetworkLocation();	
        	}
        	
        }

        //provider失效时调用
        @Override
        public void onProviderDisabled(String provider) {
        	mLocationManager.removeUpdates(gpsLocationListener);
        	startNetworkLocation();	
        }

        //provider启用时调用
        @Override
        public void onProviderEnabled(String provider) {
            //Log.d("Location", "onProviderEnabled");
        }

        //状态改变时调用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        	//mLocationManager.removeUpdates(gpsLocationListener);
        	//startNetworkLocation();	
        }
    };
	
	
	
	
	private Location getLastLocation()
	{
		String providerName = this.mLocationManager.getBestProvider(this.mCriteria, true);
		 try{  
			 Location location = this.mLocationManager.getLastKnownLocation(providerName);
		     location = this.mLocationManager.getLastKnownLocation(providerName);
			 return location;
			 		
	      } catch  (Exception e) {  
	        	return null;
	      } 
	}
		
	
	private void locationCallback(Location location,String type)
	{
		Message msg=new Message();
		Bundle bd=new Bundle();
		bd.putParcelable("LOCATION",  (Parcelable)location);
		bd.putString("LOCATIONTYPE", type);
		msg.setData(bd);
		this.mHandler.sendMessage(msg);
	}
		
     
     
}
