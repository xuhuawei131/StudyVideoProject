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
* FragmentSetting.java V1.0 2015-12-3 下午3:47:12
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.ui.fragment;

import com.xuhuawei.studyvideoproject.R;

import android.os.Bundle;
/**
 * 
		* 功能描述：
		* 设置
		* @author 许华维(WAH-WAY)
		*
		* <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class FragmentSetting extends BaseFragment {
	@Override
	protected void initData(Bundle bundle) {
		
	}
	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_setting;
	}
	@Override
	protected void findViewByIds() {
		
	}
	@Override
	protected void setTitleView() {
		text_title.setText("设置");
		
	}
	@Override
	protected void requestSerivce() {
		
	}

}
