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
* MainActivity.java V1.0 2015-12-3 上午10:28:54
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.ui.activity;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuhuawei.studyvideoproject.R;
import com.xuhuawei.studyvideoproject.adapters.FragmentAdapter;
import com.xuhuawei.studyvideoproject.ui.BaseActivity;
import com.xuhuawei.studyvideoproject.ui.fragment.FragmentLocal;
import com.xuhuawei.studyvideoproject.ui.fragment.FragmentSetting;
import com.xuhuawei.studyvideoproject.ui.fragment.FragmentShared;
import com.xuhuawei.studyvideoproject.ui.views.NotScrollViewPager;
/**
 * 
		* 功能描述：
		* 主界面
		* @author 许华维(WAH-WAY)
		*
		* <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class MainActivity extends BaseActivity implements OnClickListener,OnPageChangeListener{
	private NotScrollViewPager mViewPager;
	private FragmentAdapter mAdapter;
	private TextView[] textViews=new TextView[3];
	private Fragment[] fragments={new FragmentLocal(),new FragmentShared(),new FragmentSetting()};
	@Override
	protected void initData() {
		mAdapter=new FragmentAdapter(getSupportFragmentManager(),fragments);
	}
	@Override
	protected int getContentView() {
		return R.layout.activity_main;
	}

	@Override
	protected void findViewByIds() {
		mViewPager=(NotScrollViewPager)findViewById(R.id.viewPager);
		
		textViews[0]=(TextView)findViewById(R.id.text_local);
		textViews[1]=(TextView)findViewById(R.id.text_share);
		textViews[2]=(TextView)findViewById(R.id.text_setting);
		
		textViews[0].setOnClickListener(this);
		textViews[1].setOnClickListener(this);
		textViews[2].setOnClickListener(this);
		
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
	}

	@Override
	protected void requestService() {
		
	}
	
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.text_local:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.text_share:
			mViewPager.setCurrentItem(1);	
			break;
		case R.id.text_setting:
			mViewPager.setCurrentItem(2);
			break;
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	@Override
	public void onPageSelected(int position) {
		
	}

	private void changeItemColor(ImageView view){
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(1.0f);//灰度百分比
		ColorMatrixColorFilter cf = new ColorMatrixColorFilter(
				cm);
		view.setColorFilter(cf);
	}

}
