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
* BaseFragment.java V1.0 2015-12-3 下午2:56:14
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

		package com.xuhuawei.studyvideoproject.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xuhuawei.studyvideoproject.R;
import com.xuhuawei.studyvideoproject.ui.BaseActivity;

public abstract class BaseFragment extends Fragment {
	protected Context context;
	protected View fragmentView;
	private View fragmentChildView;
	
	private View layout_title;
	protected TextView text_left;
	protected TextView text_right;
	protected TextView text_title;
	
	protected abstract void initData(Bundle bundle);
	protected abstract int getFragmentLayout();
	protected abstract void findViewByIds();
	protected abstract void setTitleView();
	protected abstract void requestSerivce();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Activity activity=(Activity)this.getActivity();
		Bundle bundle=this.getArguments();
		context=activity;
		initData(bundle);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(fragmentView==null){
			fragmentView=inflater.inflate(R.layout.fragment_base, null);
			
			FrameLayout layout_content=(FrameLayout)fragmentView.findViewById(R.id.layout_content);
			fragmentChildView=inflater.inflate(getFragmentLayout(), null);
			layout_content.addView(fragmentChildView);
			
			layout_title=fragmentView.findViewById(R.id.layout_title);
			text_left=(TextView)fragmentView.findViewById(R.id.text_left);
			text_right=(TextView)fragmentView.findViewById(R.id.text_right);
			text_title=(TextView)fragmentView.findViewById(R.id.text_title);
			
			findViewByIds();
			setTitleView();
			requestSerivce();
		}
		ViewGroup parent = (ViewGroup) fragmentView.getParent();
		if (parent != null) {
			parent.removeView(fragmentView);
		}
		return fragmentView;
	}
	protected View findViewById(int res){
		return fragmentChildView.findViewById(res);
	}
	
	protected void setLeftMagin(){
		setLeftMargin(50);
	}
	protected void setLeftMargin(int margin){
		RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)text_left.getLayoutParams();
		lp.leftMargin=margin;
		text_left.setLayoutParams(lp);
	}
	protected void setRightMagin(){
		setRightMargin(50);
	}
	protected void setRightMargin(int margin){
		RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)text_right.getLayoutParams();
		lp.rightMargin=margin;
		text_right.setLayoutParams(lp);
	}
	
	protected void setHideTitleBar(){
		layout_title.setVisibility(View.GONE);
	}
	
	
	public void showToast(String text){
		Toast.makeText(this.getActivity(), text, Toast.LENGTH_SHORT).show();
	}
	
	public void showToast(int res){
		Toast.makeText(this.getActivity(), res, Toast.LENGTH_SHORT).show();
	}
}
