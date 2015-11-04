package com.ntt.uutravel.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;  
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;  
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;  
  
import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;  
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
  
import android.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;  
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;  
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log; 
import android.widget.Toast;



public class AppUpdate {
	
	//public static final String SERVER_CHECK_UPDATE_URL="http://192.168.1.130/uutravel/update.txt";
	public static final String SERVER_CHECK_UPDATE_URL="http://117.121.128.16:54322/Master/GetVersion";
    public static final String PACKAGE_NAGE="com.ntt.uutravel";
    
    public static final String UPDATE_VERSION_CODE="version";
    public static final String UPDATE_VERSION_DESCRIPTION="description";
    public static final String UPDATE_VERSION_APKURL="url";
    
    public static final String UPDATE_TITLE="优游日本";
     
       
    private final int UPDATA_NONEED = 0;
	private final int UPDATA_CLIENT = 1;
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int SDCARD_NOMOUNTED = 3;
	private final int DOWN_ERROR = 4;
	
    
    int localVersion=0;
    
    Context _mContext;
    
    JSONObject versionDataJsonObject;
    
    long uureference=0;
    
	DownloadManager downloadManager; 
	
	Handler  _mUpdateCallbackHandler;
    
    /**
     * 构造函数
     * @param mContext 上下文
     * **/
    public AppUpdate(Context mContext,Handler mHandler)
    {
    	this._mContext = mContext;
    	this._mUpdateCallbackHandler=mHandler;
    	localVersion=this.getVerCode();	
    	
    }
    
    public void run()
    {
    	CheckVersionTask task=new CheckVersionTask();
    	new Thread(task).start();
    }
    
    
    public void destory()
    {
    	
    	
    }
    

    /** 
     * 向服务器发送查询请求
     */  
    public static JSONObject GetServerInfo() {  
    	
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        try {  
        	httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); 
        	httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
            HttpResponse response = null;  
            HttpGet httpGet=new HttpGet(SERVER_CHECK_UPDATE_URL);
            // 创建httpost.访问本地服务器网址  
            //HttpPost httpost = new HttpPost(SERVER_CHECK_UPDATE_URL);  
            String resContent="";
  
            //httpost.setEntity(new UrlEncodedFormEntity(vps, HTTP.UTF_8));  
           
            response = httpclient.execute(httpGet); // 执行  
  
            if (response.getEntity() != null) {  
                 resContent = EntityUtils.toString(response.getEntity(), "UTF-8");  
            }  
            Log.i("msg",resContent);  
            JSONObject josnObject=  new JSONObject(resContent);
            
            if(josnObject!=null)
            {
            	JSONObject dataJsonObject=josnObject.getJSONObject("body");
            	if(dataJsonObject!=null)
            	{
            		return  dataJsonObject;
            	}	
            }
            
            return null;
            //return builder;  
  
        } catch (Exception e) {  
        
            Log.e("msg",e.getMessage());  
            return null;  
        } finally {  
            try {  
                httpclient.getConnectionManager().shutdown();// 关闭连接  
                // 这两种释放连接的方法都可以  
            } catch (Exception e) {  
                // TODO Auto-generated catch block  
                Log.e("msg",e.getMessage());  
            }  
        }  
    } 
		
	 /** 
     * 获取软件版本号 
     * @param context 
     * @return 
     */  
    public  int getVerCode() {  
        int verCode = -1;  
        try {  
            //注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分  
            verCode = this._mContext.getPackageManager().getPackageInfo(  
            		PACKAGE_NAGE, 0).versionCode;  
        } catch (NameNotFoundException e) {  
            Log.e("msg",e.getMessage());  
        }  
        return verCode;  
    }  
    
    /** 
     * 获取版本名称 
     * @param context 
     * @return 
     */  
     public  String getVerName() {  
         String verName = "";  
         try {  
             verName = this._mContext.getPackageManager().getPackageInfo(  
            		   PACKAGE_NAGE, 0).versionName;  
         } catch (NameNotFoundException e) {  
             Log.e("msg",e.getMessage());  
         }  
         return verName;     
 } 

      
	
     public class CheckVersionTask implements Runnable{
    	 
    	 public void run()
    	 {
    		 try{
    			 versionDataJsonObject=GetServerInfo();
    			 int versionCode=versionDataJsonObject.getInt(UPDATE_VERSION_CODE);
    			 if(versionCode!=localVersion)
    			 {
    					Message msg = new Message();
    					Bundle bundle=new Bundle();
    					bundle.putString("url", versionDataJsonObject.getString(UPDATE_VERSION_APKURL));
    					msg.setData(bundle);
     					msg.what = UPDATA_CLIENT;
     					handler.sendMessage(msg);
    			 }
    			 else
    			 {
    				Message msg = new Message();
  					msg.what = UPDATA_NONEED;
  					handler.sendMessage(msg);
    			 }
    		 }
    		 catch(Exception e){
    			 Message msg = new Message();
 				msg.what = GET_UNDATAINFO_ERROR;
 				handler.sendMessage(msg);
 				e.printStackTrace();
    			 
    		 }
    		 
    	 }
     }
     
     
     Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				Bundle bundle= 	msg.getData();
				super.handleMessage(msg);
				switch (msg.what) {
				case UPDATA_NONEED:
					//Toast.makeText(_mContext, "不需要更新",
					//Toast.LENGTH_SHORT).show();
					if (_mUpdateCallbackHandler!=null){
						Message UpdateMsg = new Message();
						UpdateMsg.what = 2;	
						_mUpdateCallbackHandler.sendMessage(UpdateMsg);
					}
					break;
				case UPDATA_CLIENT:
					if (_mUpdateCallbackHandler!=null){
						Message UpdateMsg = new Message();
						UpdateMsg.what = 1;	
						_mUpdateCallbackHandler.sendMessage(UpdateMsg);
					}
					 //对话框通知用户升级程序   
					try{
						showUpdataDialog();
					}catch(Exception ex){}
					break;
				case GET_UNDATAINFO_ERROR:
					//服务器超时   
		            //Toast.makeText(_mContext, "获取服务器更新信息失败", 1).show(); 
					break;
				case DOWN_ERROR:
					//下载apk失败  
		            //Toast.makeText(_mContext, "下载新版本失败", 1).show(); 
					break;
				}
			}
		};
   
		protected void showUpdataDialog() throws JSONException {
			
			String des=versionDataJsonObject.getString(UPDATE_VERSION_DESCRIPTION);
			
			AlertDialog.Builder builer = new Builder(this._mContext);
			builer.setTitle("版本升级");
			builer.setMessage(des);
			 //当点确定按钮时从服务器上下载 新的apk 然后安装   װ
			builer.setPositiveButton("立即下载", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//Log.i(TAG, "下载apk,更新");
					try{
					downLoadApk();
					}
					catch(Exception ex){}
				}
			});
			builer.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//do sth
				}
			});
			AlertDialog dialog = builer.create();
			dialog.show();
		}
		
		
		/* 
		 * 从服务器中下载APK 
		 */  
		protected void downLoadApk() throws JSONException {  
			String url=versionDataJsonObject.getString(UPDATE_VERSION_APKURL);
			
			String serviceString = Context.DOWNLOAD_SERVICE; 
			downloadManager = (DownloadManager)this._mContext.getSystemService(serviceString); 
			
			Uri uri = Uri.parse(url); 
			DownloadManager.Request request = new Request(uri); 
			
			request.setTitle(UPDATE_TITLE);
		
			uureference = downloadManager.enqueue(request); 
			
			//定义广播接收
			IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE); 
		     
			BroadcastReceiver receiver = new BroadcastReceiver() { 
			  @Override 
			  public void onReceive(Context context, Intent intent) { 
			    long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1); 
			    if (uureference == reference) { 
			    	  Query myDownloadQuery = new Query(); 
			    	  myDownloadQuery.setFilterById(reference);
			    	  Cursor myDownload = downloadManager.query(myDownloadQuery); 
			    	  
			    	    if (myDownload.moveToFirst()) { 
			    	        int fileNameIdx =  
			    	          myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME); 
			    	        int fileUriIdx =  
			    	          myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI); 
			    	   
			    	        String fileName = myDownload.getString(fileNameIdx); 
			    	        String fileUri = myDownload.getString(fileUriIdx); 
			    	       
			    	        installApk(fileName);
			    	    } 
			    } 
			  } 
			}; 
		   this._mContext.registerReceiver(receiver, filter); 
			
//		    final ProgressDialog pd;    //进度条对话框  
//		    pd = new  ProgressDialog(this._mContext);  
//		    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
//		    pd.setMessage("正在下载更新");  
//		    pd.show();  
//		    new Thread(){  
//		        @Override  
//		        public void run() {  
//		            try { 
//		            	String url=versionDataJsonObject.getString(UPDATE_VERSION_APKURL);
//		                File file =getFileFromServer(url,pd); //DownLoadManager.getFileFromServer(info.getUrl(), pd);  
//		                sleep(3000);  
//		                installApk(file);  
//		                pd.dismiss(); //结束掉进度条对话框  
//		            } catch (Exception e) {  
//		                Message msg = new Message();  
//		                msg.what = DOWN_ERROR;  
//		                handler.sendMessage(msg);  
//		                e.printStackTrace();  
//		            }  
//		        }}.start();  
		}
		
		
		//安装apk   
		protected void installApk(String fileUri) {  
		    Intent intent = new Intent();  
		    //执行动作  
		    intent.setAction(Intent.ACTION_VIEW); 
		    File file = new File(fileUri);  
		    //执行的数据类型  
		    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");  
		    this._mContext.startActivity(intent);  
		}  
		
		
		public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{

			
			//如果相等的话表示当前的sdcard挂载在手机上并且是可用的
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				URL url = new URL(path);
				HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				//获取到文件的大小 
				pd.setMax(conn.getContentLength());
				InputStream is = conn.getInputStream();
				File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
				FileOutputStream fos = new FileOutputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);
				byte[] buffer = new byte[1024];
				int len ;
				int total=0;
				while((len =bis.read(buffer))!=-1){
					fos.write(buffer, 0, len);
					total+= len;
					//获取当前下载量
					pd.setProgress(total);
				}
				fos.close();
				bis.close();
				is.close();
				return file;
			}
			else{
				return null;
			}
		}
		
		
		
     
     
}
