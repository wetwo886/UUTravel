package com.ntt.uutravel.common;

import java.util.ArrayList;
import java.util.List;

import com.ntt.uutravel.activity.H5Activity;

import android.app.Activity;
import android.content.Context;
import android.app.ActivityManager; 

public class AppActivityManager {
	 private static List<Activity> activityList = new ArrayList<Activity>(); 
	 
	 public static H5Activity h5Activity;
	 
	 public static void exitClient(Context ctx)   {   
		 // 关闭所有Activity   
		 for (int i = 0; i < activityList.size(); i++)   
		 {      
			 if (null != activityList.get(i))      
			 {        
				 activityList.get(i).finish();     
		     }    
	     }    
		 
//		 ActivityManager activityMgr = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE );    
//		 activityMgr.restartPackage(ctx.getPackageName());   
		 android.os.Process.killProcess(android.os.Process.myPid());
		 System.exit(0);  
	}
	 
	 
	 public static void addActivity(Activity aty){
		 activityList.add(aty);
	 }
	 
	 

	 

}

