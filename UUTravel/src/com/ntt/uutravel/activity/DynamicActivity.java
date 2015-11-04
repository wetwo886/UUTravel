package com.ntt.uutravel.activity;

import com.ntt.uutravel.common.BaseH5Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;


public class DynamicActivity extends BaseH5Activity {
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
	    {    	
	       this.finish();
	    }	    
	    return super.onKeyDown(keyCode, event);  
	}  
	
}
