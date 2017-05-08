/**

                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
* ScanService.java V1.0 2015-12-3 上午10:24:05
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ConcurrentHashMap;

import com.xuhuawei.studyvideoproject.MyVideoApplication;
import com.xuhuawei.studyvideoproject.bean.POMedia;
import com.xuhuawei.studyvideoproject.db.dao.VideoDBImpl;
import com.xuhuawei.studyvideoproject.utis.FileUtils;
import com.xuhuawei.studyvideoproject.utis.PinyinUtils;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import static com.xuhuawei.studyvideoproject.utis.FileUtils.MB;

/**
 * 
		* 功能描述：
		* 扫描视频
		* @author 许华维(WAH-WAY)
		*
		* <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class ScanService extends Service implements Runnable{
	/** 扫描文件夹 */
	public static final String EXTRA_DIRECTORY = "scan_directory";
	/** 扫描文件 */
	public static final String EXTRA_FILE_PATH = "scan_file";
	public static final String EXTRA_MIME_TYPE = "mimetype";
	private static final String SERVICE_NAME = "com.xuhuawei.studyvideoproject.service.ScanService";
	
	public static final String ACTION_FILE_SEARCH="com.xuhuawei.file.search";
	
	private ConcurrentHashMap<String, String> mScanMap = new ConcurrentHashMap<String, String>();
	private Intent intent=new Intent(ACTION_FILE_SEARCH);
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null){
			parseIntent(intent);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/** 解析Intent */
	private void parseIntent(final Intent intent) {
		final Bundle arguments = intent.getExtras();
		if (arguments != null) {
			if (arguments.containsKey(EXTRA_DIRECTORY)) {//扫描文件夹
				String directory = arguments.getString(EXTRA_DIRECTORY);
				if (!mScanMap.containsKey(directory)){
					mScanMap.put(directory, "");
				}
			} else if (arguments.containsKey(EXTRA_FILE_PATH)) {//单文件
				String filePath = arguments.getString(EXTRA_FILE_PATH);
				if (!TextUtils.isEmpty(filePath)) {
					if (!mScanMap.containsKey(filePath))
						mScanMap.put(filePath, arguments.getString(EXTRA_MIME_TYPE));
				}
			}
		}
		new Thread(this).start();
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void run() {
		while (mScanMap.keySet().size() > 0) {//开始扫描
			String path = "";
			for (String key : mScanMap.keySet()) {
				path = key;
				break;
			}
			if (mScanMap.containsKey(path)) {
				String mimeType = mScanMap.get(path);
				if ("".equals(mimeType)) {
					scanDirectory(path);
				} else {
					scanFile(path, mimeType);
				}
				//扫描完成一个
				mScanMap.remove(path);
			}
			//任务之间歇息一秒
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
			//扫描结束
		}
		//停止服务
		stopSelf();
	}
	
	/** 扫描文件 */
	private void scanFile(String path, String mimeType) {
		save(new POMedia(path, mimeType));
	}
	/** 扫描文件夹 */
	private void scanDirectory(String path) {
		eachAllMedias(new File(path));
	}

	/** 递归查找视频 */
	private void eachAllMedias(File f) {
		if (f != null && f.exists() && f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null) {
				for (File file : f.listFiles()) {
					if (file.isDirectory()) {
						if (!file.getAbsolutePath().startsWith(".")) {// 忽略.开头的文件夹
							eachAllMedias(file);
						}
					} else if (file.exists() && file.canRead()
							&& FileUtils.isVideo(file)) {
						long  size=f.length();
						if(size>MB){
							save(new POMedia(file));
						}
					}
				}
			}
		}
	}

	/**
	 * 
			* 功能描述：
			* 保存入库
			* @author 许华维(WAH-WAY)
			* <p>创建日期 ：2015-12-4 上午11:39:40</p>
			*
			* @param media
			*
			* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	private void save(POMedia media) {
		boolean isExit=VideoDBImpl.getInstance().exists(media.path, media.last_modify_time);
		if (!isExit) {
		if (media.title != null && media.title.length() > 0) {
			media.title_key = PinyinUtils.chineneToSpell(media.title
					.charAt(0) + "");
		}
		media.last_access_time = System.currentTimeMillis();// 提取缩略图
		media.mime_type = FileUtils.getMimeType(media.path);
		VideoDBImpl.getInstance().insertVideo(media);// 入库
		// 扫描到一个
		// notifyObservers(SCAN_STATUS_RUNNING, media);
		intent.putExtra("POMedia", media);
		sendBroadcast(intent);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * 
			* 功能描述：
			* 视频扫描的服务器是否正在运行
			* @author 许华维(WAH-WAY)
			* <p>创建日期 ：2015-12-3 下午9:45:14</p>
			*
			* @return
			*
			* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public static boolean isRunning() {
		ActivityManager manager = (ActivityManager) MyVideoApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (SERVICE_NAME.equals(service.service.getClassName()))
				return true;
		}
		return false;
	}
}
