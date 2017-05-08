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
* MyBaseAdapter.java V1.0 2015-12-3 下午5:11:45
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.xuhuawei.studyvideoproject.adapters.viewholder.ViewHolder;

public abstract class MyBaseAdapter <VH extends ViewHolder> extends BaseAdapter {
	protected Context context;
	protected OnClickListener listener;
	protected ArrayList<?> arrayList;
	private  LayoutInflater inflater;
	protected abstract VH createViewHolder(int position,ViewGroup viewgroup);
	protected abstract void bindViewHolder(int position,VH holder);
	
	public MyBaseAdapter(Context context,ArrayList<?> arrayList){
		this.context=context;
		this.inflater=LayoutInflater.from(context);
		this.arrayList=arrayList;
	}
	public void setOnClickListener(OnClickListener listener){
		this.listener=listener;
	}
	@Override
	public int getCount() {
		return arrayList==null?0:arrayList.size();
	}
	@Override
	public Object getItem(int arg0) {
		return arrayList.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	protected View inflateView(int resId){
		return inflater.inflate(resId, null);
	}
	@Override
	public View getView(int position, View converView, ViewGroup viewgroup) {
		VH holder;
		if(converView==null){
			holder=createViewHolder(position,viewgroup);
			converView=holder.view;
			converView.setTag(holder);
		}else{
			holder=(VH)converView.getTag();
		}
		bindViewHolder(position,holder);
		return converView;
	}
	
}

