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
 * FragmentAdapter.java V1.0 2015-12-3 下午3:44:45
 *
 * Copyright JIAYUAN Co. ,Ltd. All rights reserved.
 *
 * Modification history(By WAH-WAY):
 *
 * Description:
 */

package com.xuhuawei.studyvideoproject.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

public class FragmentAdapter extends FragmentStatePagerAdapter {
	private Fragment[] fragments;
	public FragmentAdapter(FragmentManager fm, Fragment[] fragments) {
		super(fm);
		this.fragments = fragments;
	}
	@Override
	public Fragment getItem(int position) {
		return fragments[position];
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}
	@Override
	public int getCount() {
		return fragments.length;
	}
	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}
}
