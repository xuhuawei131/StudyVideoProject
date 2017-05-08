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
 * NotScrollViewPager.java V1.0 2015年4月16日 下午3:56:22
 *
 * Copyright JIAYUAN Co. ,Ltd. All rights reserved.
 *
 * Modification history(By WAH-WAY):
 *
 * Description:
 */

package com.xuhuawei.studyvideoproject.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * 
		* 功能描述：
		* 不可滑动的viewpager
		* @author 许华维(WAH-WAY)
		*
		* <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class NotScrollViewPager extends ViewPager {
	private boolean scrollble = false;

	public NotScrollViewPager(Context context) {
		super(context);
	}

	public NotScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (!scrollble) {
			return true;
		}
		return super.onTouchEvent(ev);
	}

	public boolean isScrollble() {
		return scrollble;
	}
	/**
	 * 
			* 功能描述：
			* 设置是否可以滑动
			* @author 许华维(WAH-WAY)
			* <p>创建日期 ：2015-12-3 下午3:16:32</p>
			*
			* @param scrollble 可滑动
			*
			* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public void setScrollble(boolean scrollble) {
		this.scrollble = scrollble;
	}
}
