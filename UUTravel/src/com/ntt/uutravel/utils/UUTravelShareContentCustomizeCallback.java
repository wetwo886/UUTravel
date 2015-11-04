package com.ntt.uutravel.utils;

import android.content.res.Resources;

import com.ntt.uutravel.R;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.moments.WechatMoments;


public class UUTravelShareContentCustomizeCallback implements cn.sharesdk.onekeyshare.ShareContentCustomizeCallback
 {
	 public  void onShare (Platform platform, ShareParams paramsToShare )  { 
		 if( paramsToShare.getText()==null ||  paramsToShare.getText().trim().equals(""))
		 {
			 paramsToShare.setText("优游日本");
		 }
		 
		 if(paramsToShare.getTitle()==null ||  paramsToShare.getTitle().trim().equals(""))
		 {
			 paramsToShare.setTitle("优游日本");
			 
		 }
		 
		 
         if(platform.getName().equals(SinaWeibo.NAME)){
        	 paramsToShare.setText(paramsToShare.getText()+" "+paramsToShare.getUrl());
         }
         
         if(platform.getName().equals(WechatMoments.NAME)){
        	 paramsToShare.setTitle(paramsToShare.getText());
        	 
         }
 } 
}
