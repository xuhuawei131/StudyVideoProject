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
* BaseActivity.java V1.0 2015-12-3 上午10:29:14
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity {
	protected abstract void initData();
	protected abstract int getContentView();
	protected abstract void findViewByIds();
	protected abstract void requestService();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		initData();
		int layoutId=getContentView();
		setContentView(layoutId);
		findViewByIds();
		requestService();
	}
	
	protected void showToast(int  res){
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
	}
	
	protected void showToast(String  res){
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
	}
	
	
}
