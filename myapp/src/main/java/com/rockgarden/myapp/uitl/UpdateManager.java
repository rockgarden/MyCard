package com.rockgarden.myapp.uitl;

public class UpdateManager {
//	private Context mContext = null;
//	private String downloadUrl="";   //安装包地�?
//	private File savePath;
//	private File saveApkPath;
//
//
//	public static final int MSG_FINISH  = 1; // 下载完成
//    public static final int MSG_FAILURE = 2;// 下载失败
//    public static final int MSG_ERROR   = 3;    //无法进行下载的错�?
//    public static final int NOTIFICATION_DOWNFINSH = 10001; //通知成功的标�?
//    public static final int NOTIFICATION_DOWNFAIL  = 10002; //通知失败的标�?
//	private Message msg=new Message();
//	private Thread downLoadThread;
//	private NotificationManager mNotifManager;
//	private Notification mdownNotification;
//	private RemoteViews mContentView;
//	private PendingIntent mDownPendingIntent;
//
//	private Handler mHandler=new Handler()
//	{
//		public void handleMessage(Message msg)
//		{
//			switch (msg.what) {
//			case MSG_FINISH:
//				Toast.makeText(App.getContext(), "下载成功，可以进行安�?", Toast.LENGTH_SHORT).show();
//				break;
//			case MSG_FAILURE:
//				AlertDialog.Builder retryDialog=new AlertDialog.Builder(App.getContext());
//				retryDialog.setTitle("版本更新");
//				retryDialog.setMessage("更新未完成，是否重试");
//				retryDialog.setCancelable(false);
//				retryDialog.setPositiveButton("�?�?", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						dialog.dismiss();
//					}
//				});
//				retryDialog.setNegativeButton("重试", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						dialog.dismiss();
//						beginDownloadApk();
//					}
//				});
//
//				break;
//			case MSG_ERROR:
//				Toast.makeText(mContext, "更新失败，请�?查手机SD是否正常运行或�?�是否有5M的空闲空�?", Toast.LENGTH_SHORT).show();
//				break;
//			default:
//				break;
//			}
//		}
//	};
//	public UpdateManager(Context mContext) {
//		super();
//		this.mContext = mContext;
//		this.mNotifManager=(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//	}
//	//�?测更新间隔时间（假定为半天）
//	public boolean beforeUpdateCheck()
//	{
//		if (isNetworkConnected()) {
//			SharedPreferences spf = mContext.getSharedPreferences(
//					"com.citylinkdata.tsm", mContext.MODE_PRIVATE);
//			long oldUpdateTime = spf.getLong("updatetime", 0);
//			SharedPreferences.Editor editor = mContext.getSharedPreferences(
//					"com.citylinkdata.tsm", mContext.MODE_PRIVATE).edit();
//			Date current = new Date(System.currentTimeMillis());
//			long currTime = current.getTime();
//			if (oldUpdateTime == 0) {
//				editor.putLong("updatetime", currTime);
//				return true;
//			} else {
//				long timeSpace = currTime - oldUpdateTime;
//				if (timeSpace >= 43200000) {
//					editor.putLong("updatetime", currTime);
//					return true;
//				} else {
//					return false;
//				}
//			}
//		} else {
//			Log.d("AppLoginActivity","net is not open");
//			return false;
//		}
//
//		//return true;
//	}
//	//向服务器端发送请求检测版本更�?
//	public void updateCheck()
//	{
//		String url="http://api.eastcomnetwork.com/axis2/services/UAService/version";
//		String app_ver="";
//		String packname="";
//		try {
//			app_ver=mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
//			packname=mContext.getPackageName();
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Log.d("AppLoginActivity","app_ver="+app_ver+","+"packname="+packname);
//		RequestParams params=new RequestParams();
//		params.put("oldVersionName", "1.0.1");
//		params.put("packageName", "com.eastcom.apphall");
//		HttpUtil.get(url, params, new AsyncHttpResponseHandler() {
//
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				// TODO Auto-generated method stub
//				try {
//					String allpage = new String(arg2, "UTF-8");
//					String[] page1=allpage.split("<ns:return>");
//					String page2=page1[1];
//					String[] page3=page2.split("</ns:return>");
//					String getPage=page3[0];
//					Log.d("AppLoginActivity",getPage);
//					JSONObject getPage1=new JSONObject(getPage);
//					JSONObject response=getPage1.getJSONObject("response");
//					int result=response.getInt("result");
//					String message=response.getString("message");
//					if(result==1)
//					{
//						JSONArray data=response.getJSONArray("data");
//						JSONObject obj=data.getJSONObject(0);
//						downloadUrl=obj.getString("downloadUrl");
//						Log.d("AppLoginActivity","downloadUrl="+downloadUrl);
//						String versionInfo=obj.getString("versionInfo");
//						String versionName=obj.getString("versionName");
//						AlertDialog.Builder updateDialog=new AlertDialog.Builder(mContext);
//						updateDialog.setTitle("版本更新");
//						updateDialog.setMessage("�?测到新版本，是否立即更新\n"+"新版�?:"+versionName+"\n"+"更新内容:"+versionInfo+"\n"+"提示：建议在连接WIFI情况下下�?");
//						updateDialog.setCancelable(false);
//						updateDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								// TODO Auto-generated method stub
//								dialog.dismiss();
//								beginDownloadApk();
//							}
//						});
//						updateDialog.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								// TODO Auto-generated method stub
//								dialog.dismiss();
//							}
//						});
//						updateDialog.show();
//					}
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//				// TODO Auto-generated method stub
//				Log.d("AppLoginActivity","update fail:"+arg3.toString());
//			}
//		});
//	}
//
//	public void beginDownloadApk()
//	{
//		downLoadThread=new Thread(downLoadRunnable);
//		downLoadThread.start();
//	}
//	private Runnable downLoadRunnable=new Runnable() {
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			Log.d("AppLoginActivity","下载线程启动");
//			if(ExistSDCard()  && (getSDFreeSize()>=5))
//			{
//
//				mdownNotification=new Notification(android.R.drawable.stat_sys_download, "apk下载", System.currentTimeMillis());
//				mdownNotification.flags=Notification.FLAG_ONGOING_EVENT;
//				mdownNotification.flags=Notification.FLAG_AUTO_CANCEL;
//				mContentView=new RemoteViews(App.getContext().getPackageName(), R.layout.app_download_notification);
//				mContentView.setImageViewResource(R.id.downLoadIcon, android.R.drawable.stat_sys_download);
//				mDownPendingIntent=PendingIntent.getActivity(App.getContext(), 0, new Intent(), 0);
//				boolean isdownSuccess=downFileApk();
//				if(isdownSuccess)
//				{
//					msg.what=MSG_FINISH;
//					Notification downFinshNotification=new Notification(android.R.drawable.stat_sys_download_done,"下载完成",System.currentTimeMillis());
//					downFinshNotification.flags=Notification.FLAG_ONGOING_EVENT;
//					downFinshNotification.flags=Notification.FLAG_AUTO_CANCEL;
//					Intent intent=new Intent(Intent.ACTION_VIEW);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intent.setDataAndType(Uri.fromFile(saveApkPath), "application/vnd.android.package-archive");
//					PendingIntent contentIntent=PendingIntent.getActivity(App.getContext(), 0, intent, 0);
//					downFinshNotification.setLatestEventInfo(App.getContext(), "下载完成", "点击安装", contentIntent);
//					mNotifManager.notify(NOTIFICATION_DOWNFINSH, downFinshNotification);
//
//				}
//				else{
//					msg.what=MSG_FAILURE;
//					Notification downFailNotification=new Notification(android.R.drawable.stat_sys_download_done,"下载失败",System.currentTimeMillis());
//					downFailNotification.flags=Notification.FLAG_AUTO_CANCEL;
//					PendingIntent contentIntent = PendingIntent.getActivity( App.getContext(), 0, new Intent(), 0);
//					downFailNotification.setLatestEventInfo(App.getContext(), "下载失败", null, contentIntent);
//					mNotifManager.notify(NOTIFICATION_DOWNFAIL, downFailNotification);
//				}
//			}else{
//					msg.what=MSG_ERROR;
//			}
//
//			mHandler.sendMessage(msg);
//
//		}
//	};
//	//发起网络请求�?始下�?
//	protected boolean downFileApk() {
//		// TODO Auto-generated method stub
//		Log.d("AppLoginActivity","�?�?2");
//		File sdcard=Environment.getExternalStorageDirectory();
//		savePath=new File(sdcard+File.separator+mContext.getPackageName());
//		saveApkPath=new File(savePath+File.separator+"citylinkdataApk");
//		int filesize=0;
//		int downfilesize=0;
//		boolean result=false;
//		int progress=0;
//		int times=0;
//		try {
//
//			URL apkurl=new URL(downloadUrl);
//			HttpURLConnection conn=(HttpURLConnection) apkurl.openConnection();    //是否还要加入HTTP连接请求是否成功的判�?
//			conn.setRequestMethod("GET");
//			conn.setRequestProperty("Accept-Encoding", "identity");       //不太明白具体意义，在之前项目中有配置
//			conn.setDoOutput(true);
//
//			conn.connect();
//			filesize=conn.getContentLength();
//			Log.d("AppLoginActivity","总大�?="+filesize);
//			InputStream is=apkurl.openStream();
//			if(!savePath.exists())
//			{
//				savePath.mkdirs();
//			}
//			if(saveApkPath.exists())
//			{
//				saveApkPath.delete();
//			}
//			if(!saveApkPath.exists())
//			{
//				saveApkPath.createNewFile();
//			}
//			FileOutputStream fos=new FileOutputStream(saveApkPath);
//			byte buf[] = new byte[1024];
//			int i=0;
//			while((i=is.read(buf)) != -1)
//			{
//				downfilesize+=i;
//				progress= (int) (((float)downfilesize / filesize)*100);
//				fos.write(buf, 0, i);
//				if(downfilesize==filesize)
//				{
//					mNotifManager.cancel(R.id.downLoadIcon);
//				}
//				else{
//					if(times>=512)    //防止频繁更新通知过快导致程序变慢或�?�崩�?
//					{
//						times=0;
//						mContentView.setTextViewText(R.id.progressPercent, progress+"%");
//						mContentView.setProgressBar(R.id.downLoadProgress, 100, progress, false);
//						mdownNotification.contentView=mContentView;
//						mdownNotification.contentIntent=mDownPendingIntent;
//						mNotifManager.notify(R.id.downLoadIcon, mdownNotification);
//					}
//				}
//				times++;
//			}
//			fos.flush();
//			fos.close();
//			is.close();
//			result=true;
//
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			result=false;
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			result=false;
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	// 判断是否有网络连�?
//	public boolean isNetworkConnected() {
//		if (mContext != null) {
//			ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
//					.getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo mNetworkInfo = mConnectivityManager
//					.getActiveNetworkInfo();
//			if (mNetworkInfo != null) {
//				return mNetworkInfo.isAvailable();
//			}
//		}
//		return false;
//	}
//
//	//�?测SD卡是否存�?
//	private boolean ExistSDCard() {
//		  if (Environment.getExternalStorageState().equals(
//		    Environment.MEDIA_MOUNTED)) {
//		   return true;
//		  } else
//		   return false;
//		 }
//	//�?测SD卡空间容�?
//	public long getSDFreeSize(){
//	     //取得SD卡文件路�?
//	     File path = Environment.getExternalStorageDirectory();
//	     StatFs sf = new StatFs(path.getPath());
//	     //获取单个数据块的大小(Byte)
//	     long blockSize = sf.getBlockSize();
//	     //空闲的数据块的数�?
//	     long freeBlocks = sf.getAvailableBlocks();
//	     //返回SD卡空闲大�?
//	     //return freeBlocks * blockSize;  //单位Byte
//	     //return (freeBlocks * blockSize)/1024;   //单位KB
//	     return (freeBlocks * blockSize)/1024/1024; //单位MB
//	   }

}
