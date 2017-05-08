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
* LocalVideoAdapter.java V1.0 2015-12-3 下午5:14:29
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xuhuawei.studyvideoproject.R;
import com.xuhuawei.studyvideoproject.adapters.viewholder.LocalViewHolder;

public class LocalVideoAdapter extends MyBaseAdapter<LocalViewHolder> {

	public LocalVideoAdapter(Context context, ArrayList<?> arrayList) {
		super(context, arrayList);
	}
	@Override
	protected LocalViewHolder createViewHolder(int position, ViewGroup viewgroup) {
		View view=inflateView(R.layout.adapter_local_video);
		LocalViewHolder holder=new LocalViewHolder(view);
		return holder;
	}
	@Override
	protected void bindViewHolder(int position, LocalViewHolder holder) {
		holder.setImteData(position,arrayList.get(position));
	}
}
