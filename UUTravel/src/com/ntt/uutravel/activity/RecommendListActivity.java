package com.ntt.uutravel.activity;

import com.baidu.mobstat.StatService;
import com.ntt.uutravel.common.BaseH5Activity;

import android.app.Activity;
import android.os.Bundle;


public class RecommendListActivity extends BaseH5Activity {

	  public void onResume() {
	       
	        super.onResume();
	        
	        StatService.onResume(this);
	    }

	    public void onPause() {
	        super.onPause();

	        StatService.onPause(this);
	    }
	
}
