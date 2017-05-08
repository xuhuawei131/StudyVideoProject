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
 * DensityUtil.java V1.0 2015-12-3 下午5:05:23
 *
 * Copyright JIAYUAN Co. ,Ltd. All rights reserved.
 *
 * Modification history(By WAH-WAY):
 *
 * Description:
 */

package com.xuhuawei.studyvideoproject.utis;

import com.xuhuawei.studyvideoproject.MyVideoApplication;

import android.content.res.Resources;
import android.util.TypedValue;

public class DensityUtil {
	
	public static int dip2px(float dip) {
		Resources r = MyVideoApplication.getInstance().getResources();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dip, r.getDisplayMetrics());
		return px;
	}

	public static int px2dipfloat(float pxValue) {
		final float scale = MyVideoApplication.getInstance().getResources()
				.getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
