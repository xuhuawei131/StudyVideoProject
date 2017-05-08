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
* SplashActivity.java V1.0 2015-12-3 上午10:28:08
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.ui.activity;

import io.vov.vitamio.Vitamio;

import java.util.Random;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.xuhuawei.studyvideoproject.InitHelper;
import com.xuhuawei.studyvideoproject.R;
import com.xuhuawei.studyvideoproject.ui.BaseActivity;
import com.xuhuawei.studyvideoproject.utis.LongTaskThread;
import com.xuhuawei.studyvideoproject.utis.LongTaskThread.ILongTaskInterface;
/**
 * 
		* 功能描述：
		* 初始化页面
		* @author 许华维(WAH-WAY)
		*
		* <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class SplashActivity extends BaseActivity implements ILongTaskInterface{
	private TextView text_tips;
	private String[] tips;
	private Random random=new Random();
	private LongTaskThread task;
	@Override
	protected void initData() {
		task=new LongTaskThread(0,this);
		tips=this.getResources().getStringArray(R.array.splash_tips);
	}
	@Override
	protected int getContentView() {
		return R.layout.activity_splash;
			
	}
	@Override
	protected void findViewByIds() {
		text_tips=(TextView)findViewById(R.id.text_tips);

		int index=random.nextInt(tips.length);
//		text_tips.setText(tips[index]);
	}
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		handler.removeMessages(1);
	}
	@Override
	protected void requestService() {
		if(!Vitamio.isInitialized(this)){
			task.execute();
		}else{
			handler.sendEmptyMessageDelayed(1, 2000);
		}
	}
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent=new Intent(SplashActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
	};
	@Override
	public void doPreUITask(int index) {
		
	}
	@Override
	public Object doLongTask(int index) {
		InitHelper initHelper=new InitHelper(this);
		return initHelper.startInitVitamio();
			
	}
	@Override
	public void doEndUITask(int index,Object obj) {
		Boolean isInit=(Boolean)obj;
		if(isInit){
			handler.sendEmptyMessageDelayed(1, 0);
		}else{
			finish();
		}
	}
}
