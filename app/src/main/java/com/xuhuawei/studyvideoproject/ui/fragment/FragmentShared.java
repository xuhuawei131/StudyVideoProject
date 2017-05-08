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
* FragmentShared.java V1.0 2015-12-3 下午3:46:54
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

		package com.xuhuawei.studyvideoproject.ui.fragment;

import com.xuhuawei.studyvideoproject.R;
import com.xuhuawei.studyvideoproject.ui.activity.VideoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FragmentShared extends BaseFragment implements View.OnClickListener {


	private static final String path1 = "http://gslb.miaopai.com/stream/oxX3t3Vm5XPHKUeTS-zbXA__.mp4";
	private static final String path2 = "http://mvvideo2.meitudata.com/590c2b2d5e2b78743.mp4";

	private Button btn_netUrl;
	@Override
	protected void initData(Bundle bundle) {

	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_share;
	}

	@Override
	protected void findViewByIds() {
		Button btn_netUrl=(Button)findViewById(R.id.btn_netUrl1);
		btn_netUrl.setOnClickListener(this);

		Button btn_netUr2=(Button)findViewById(R.id.btn_netUrl2);
		btn_netUr2.setOnClickListener(this);
	}

	@Override
	protected void setTitleView() {
		text_title.setText("分享");
	}

	@Override
	protected void requestSerivce() {
	}

	@Override
	public void onClick(View v) {
		Intent intent=new Intent();
		intent.setClass(context, VideoActivity.class);
		if(v.getId()==R.id.btn_netUrl1){
			intent.putExtra("path",path1);
		}else{
			intent.putExtra("path",path2);
		}
		startActivity(intent);
	}
}
