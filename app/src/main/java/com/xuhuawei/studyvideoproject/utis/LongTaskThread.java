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
* VideoDBTask.java V1.0 2015-12-4 下午5:57:09
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.utis;
import android.os.AsyncTask;


public class LongTaskThread extends AsyncTask<Void, Void, Object> {
	private ILongTaskInterface taskInterface;
	private int index;
	public LongTaskThread(int index,ILongTaskInterface taskInterface){
		this.taskInterface=taskInterface;
		this.index=index;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		taskInterface.doPreUITask(index);
	}
	@Override
	protected Object doInBackground(Void... params) {
		return taskInterface.doLongTask(index);
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		taskInterface.doEndUITask(index,result);
	}
	
	
	public interface ILongTaskInterface {
		public void doPreUITask(int index);
		public Object doLongTask(int index);
		public void doEndUITask(int index,Object obj);
	}
	
}
