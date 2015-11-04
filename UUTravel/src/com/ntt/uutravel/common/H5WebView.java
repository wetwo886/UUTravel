package com.ntt.uutravel.common;

import android.content.Context;
import android.view.MotionEvent;
import android.webkit.WebView;

public class H5WebView extends WebView {
	public H5WebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override  
	public boolean onTouchEvent(MotionEvent event) {  
	    if (event.getAction() == MotionEvent.ACTION_DOWN){  
	        int temp_ScrollY = getScrollY();  
	        scrollTo(getScrollX(), getScrollY() + 1);  
	        scrollTo(getScrollX(), temp_ScrollY);  
	    }  
	    return super.onTouchEvent(event);  
	} 
}
