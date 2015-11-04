package com.ntt.uutravel.common;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.PushConsts;
import com.ntt.uutravel.activity.H5Activity;
import com.ntt.uutravel.activity.WebBroswerActivity;
import com.ntt.uutravel.activity.WebBroswerRecommendActivity;

public class PushMessageReceiver extends BroadcastReceiver {

	final static String PUSH_MSG_CMD_URL="url";
	final static String PUSH_MSG_CMD_NATIVE="native";
	final static String PUSH_MSG_CMD_HYBRID="hybrid";
	final static String PUSH_MSG_CMD_RECOMMEND="recmed";
	final static String PUSH_MSG_CMD_BROWSER_URL="burl";
	
	final static int PUSH_NOTIFY_ID=9527;
	
	
	@Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        //Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传（payload）数据
                byte[] payload = bundle.getByteArray("payload");
                //payload 结构：{"cmd":"url|native|hybrid|recmed|burl","act":"","title":"","content":""}
                if (payload != null)
                {
                    String data = new String(payload);
                    Gson gson=new Gson();
                   
                    try{
                    	
                    	com.ntt.uutravel.model.PushMessageModel pushMessage=
                    		gson.fromJson(data, new TypeToken<com.ntt.uutravel.model.PushMessageModel>(){}.getType());
                    	if(pushMessage!=null)
                    	{
                    		PendingIntent activeIntent =getPendingIntent(context,pushMessage);
                    		
                    		try{
                    		com.ntt.uutravel.utils.NotificationHelper.show(
                    				context, 
                    				PUSH_NOTIFY_ID, 
                    				pushMessage.getTitle(), pushMessage.getContent(), activeIntent);
                    		}
                    		catch(Exception ex)
                    		{
                    			Log.e("PushMessageReceiver", ex.getMessage());
                    			
                    		}
                    	}
                    }
                    catch(Exception ex)
                    {
                    	Log.e("Gson-Error", ex.getMessage());
                    } 
                }
                break;
            default:
                break;
        }
    }
	
	
	private PendingIntent getPendingIntent(Context context, com.ntt.uutravel.model.PushMessageModel pushMessage )
	{
		Intent intent;
		if(pushMessage.getCmd().equalsIgnoreCase(PUSH_MSG_CMD_HYBRID))
		{
			 intent = new Intent(context,H5Activity.class);
			
		}
		else if(pushMessage.getCmd().equalsIgnoreCase(PUSH_MSG_CMD_NATIVE))
		{
			return null;
			
		}
		else if(pushMessage.getCmd().equalsIgnoreCase(PUSH_MSG_CMD_RECOMMEND))
		{
			 intent = new Intent(context,WebBroswerRecommendActivity.class);
			
		}
		else if(pushMessage.getCmd().equalsIgnoreCase(PUSH_MSG_CMD_URL))
		{
			 intent = new Intent(context,WebBroswerActivity.class);

		}
		else if(pushMessage.getCmd().equalsIgnoreCase(PUSH_MSG_CMD_BROWSER_URL))
		{
			 Uri  uri = Uri.parse(pushMessage.getAct());
			 intent = new  Intent(Intent.ACTION_VIEW, uri);
		}
		else
		{
			return null;
			
		}
		
		if(intent!=null)
		{
			intent.putExtra("url", pushMessage.getAct());	
			intent.putExtra("fromNotificaiton","Y");
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,  PendingIntent.FLAG_CANCEL_CURRENT); 
			return pendingIntent;
		}
		else
		{
			return null;
		}
		
		
	}
	
}
