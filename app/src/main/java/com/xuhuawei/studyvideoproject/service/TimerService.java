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
* TimerService.java V1.0 2015-12-3 上午11:42:50
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.service;

import java.util.HashMap;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.xuhuawei.studyvideoproject.MyVideoApplication;
import com.xuhuawei.studyvideoproject.bean.TimerBean;
/**
 * 
		* 功能描述：
		* 定时服务
		* @author 许华维(WAH-WAY)
		*
		* <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class TimerService extends Service implements Runnable{
	private HashMap<String, TimerBean> beansHashMap =null;
	private static final String PARAM1="TimeTask";
	private static final String PARAM2="operate";
	private boolean isRunning=false;
	@Override
	public void onCreate() {
		super.onCreate();
		beansHashMap = new HashMap<String, TimerBean>();
		isRunning=true;
		new Thread(this).start();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		isRunning=false;
		beansHashMap.clear();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent!=null){
			TimerBean bean=(TimerBean)intent.getSerializableExtra(PARAM1);
			boolean isAdd=intent.getBooleanExtra(PARAM2, false);
			if(bean!=null){
				if(isAdd){
					beansHashMap.put(bean.action, bean);
				}else{
					if(beansHashMap.containsKey(bean)){
						beansHashMap.remove(bean.action);
					}
				}
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void run() {
		while(isRunning){
			try {
				Thread.sleep(1000);
				for (String key : beansHashMap.keySet()) {
					TimerBean bean = beansHashMap.get(key);
					changeTime(bean);
					sendTimeData(bean);
					if (bean.count==bean.MaxOrMin) {
						beansHashMap.remove(key);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void sendTimeData(TimerBean bean) {
		Intent intent = new Intent(bean.action);
		intent.putExtra("bean", bean);
		sendBroadcast(intent);
	}

	private void changeTime(TimerBean bean) {
		if(bean.isAdd){
			bean.count++;
		}else{
			bean.count--;
		}
	}
	
	public static void addTimeTask(TimerBean bean){
		Context context=MyVideoApplication.getInstance();
		Intent intent=new Intent(context,TimerService.class);
		intent.putExtra(PARAM1, bean);
		intent.putExtra(PARAM2, true);
		context.startService(intent);
	}
	
	public static void removeTimeTask(TimerBean bean){
		Context context=MyVideoApplication.getInstance();
		Intent intent=new Intent(context,TimerService.class);
		intent.putExtra(PARAM1, bean);
		intent.putExtra(PARAM2, false);
		context.startService(intent);
	}
	public static void stopTimeTask(){
		Context context=MyVideoApplication.getInstance();
		Intent intent=new Intent(context,TimerService.class);
		context.stopService(intent);
	}
}
