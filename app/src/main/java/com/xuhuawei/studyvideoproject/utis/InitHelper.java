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
         ���汣��       ����BUG
* InitHelper.java V1.0 2015-12-4 ����9:38:12
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.utis;

import io.vov.vitamio.Vitamio;
import android.app.Activity;

public class InitHelper {
	private Activity activity;
	public InitHelper(Activity activity){
		this.activity=activity;
	}
	public boolean startInitVitamio(){
		return Vitamio.initialize(activity, activity.getResources().getIdentifier("libarm", "raw", activity.getPackageName()));
	}
}
