package com.ntt.uutravel.activity;

import com.amap.api.cloud.model.LatLonPoint;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.ntt.uutravel.R;
import com.ntt.uutravel.model.StoreDetail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlaceMapActivity extends Activity implements 
AMapLocationListener, InfoWindowAdapter,OnMarkerClickListener  {

	private MapView mapView;
	
	private AMap mAMap;
	
	private Marker placeMarker;
	
	final int GEO_LOCATION_CALLBACK=1;
	
	private ImageButton back;
	
	private double lng;
	
	private double lat;
	
	private String title;
	
	private String content; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_map);
		
		
		 Bundle bundle = this.getIntent().getExtras();
	        if(bundle!=null){
	        	 title = bundle.getString("title");
	        	 lng= bundle.getDouble("lng");
	        	 lat=bundle.getDouble("lat");
	        	 content="";
	        }
		
		
		mapView = (MapView) findViewById(R.id.placeMap);
		mapView.onCreate(savedInstanceState);// 
		
		back = (ImageButton) findViewById(R.id.imgBack);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		init();
	}
	
	
	private void init() {
		if (mAMap == null) {
			mAMap = mapView.getMap();
			setUpMap();
		}
		
		this.setMapCamera();
		
		setMapMarker();
	}
	
	private void setMapCamera()
	{
		mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat,lng)));
		CameraUpdate cameraUpdate=CameraUpdateFactory.zoomTo(14);
		mAMap.moveCamera(cameraUpdate);
		
	}
	
	private void setMapMarker()
	{
		MarkerOptions markOptions= new MarkerOptions()
		.position(new LatLng(lat, lng))
		.title(title).snippet(content)
		.icon(null)
		.perspective(true);
		 placeMarker= mAMap.addMarker(markOptions);	
	}
	
	private void setUpMap() {
		mAMap.getUiSettings().setMyLocationButtonEnabled(true);
		mAMap.setMyLocationEnabled(true); 
		mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		mAMap.setInfoWindowAdapter(this);
		mAMap.setOnMarkerClickListener(this);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View view = getLayoutInflater().inflate(R.layout.place_info_window,null);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(marker.getTitle());
		return view;
	}

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
}
