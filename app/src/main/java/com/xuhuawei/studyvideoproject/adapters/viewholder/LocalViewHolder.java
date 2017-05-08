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
* LocalViewHolder.java V1.0 2015-12-3 下午5:15:03
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.adapters.viewholder;

import io.vov.vitamio.ThumbnailUtils;
import io.vov.vitamio.provider.MediaStore.Video;
import io.vov.vitamio.utils.StringUtils;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.xuhuawei.studyvideoproject.R;
import com.xuhuawei.studyvideoproject.bean.POMedia;
import com.xuhuawei.studyvideoproject.utis.FileUtils;

import java.io.File;

public class LocalViewHolder extends ViewHolder{
	private CheckBox item_choice;
	private ImageView item_image;
	private TextView item_text_title;
	private TextView item_text_duration;
	private TextView item_text_size;

	public LocalViewHolder(View view) {
		super(view);
		item_choice = (CheckBox) findViewById(R.id.item_choice);
		item_image = (ImageView) findViewById(R.id.item_image);
		item_text_title = (TextView) findViewById(R.id.item_text_title);
		item_text_duration = (TextView) findViewById(R.id.item_text_duration);
		item_text_size = (TextView) findViewById(R.id.item_text_size);
	}

	@Override
	public void setImteData(int position,Object obj) {
		POMedia mPOMedia=(POMedia)obj;
		item_text_title.setText(mPOMedia.title);
		item_text_duration.setText(StringUtils.generateTime(mPOMedia.duration));
		item_text_size.setText(""+FileUtils.showFileSize(mPOMedia.file_size));
		
//		 Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(view.getContext(), mPOMedia.path, Video.Thumbnails.MINI_KIND);
//		 item_image.setImageBitmap(bitmap);
		Glide
				.with( view.getContext() )
				.load( Uri.fromFile( new File( mPOMedia.path ) ) )
				.into( item_image );

//		Glide.with(view.getContext())
//				.load(mPOMedia.path)
//				.override(120, 120)
//				.listener(new RequestListener<String, GlideDrawable>() {
//					@Override
//					public boolean onException(Exception arg0, String arg1,
//											   Target<GlideDrawable> arg2, boolean arg3) {
//
//						return false;
//					}
//
//					@Override
//					public boolean onResourceReady(GlideDrawable arg0,
//												   String arg1, Target<GlideDrawable> arg2,
//												   boolean arg3, boolean arg4) {
//						if (arg0 instanceof GlideBitmapDrawable) {
////						image.setImageBitmap(FastBlur
////								.doBlur(((GlideBitmapDrawable) arg0)
////												.getBitmap(),
////										Constants.BLUR_RADIUS, false));
//
//							Observable.just(((GlideBitmapDrawable) arg0).getBitmap())
//									.subscribeOn(Schedulers.io())
//									.map(new Func1<Bitmap, Bitmap>() {
//										@Override
//										public Bitmap call(Bitmap bitmap) {
//											return FastBlur.doBlur(bitmap, Constants.BLUR_RADIUS, false);
//										}
//									})
//									.observeOn(AndroidSchedulers.mainThread())
//									.subscribe(new Action1<Bitmap>() {
//										@Override
//										public void call(Bitmap bitmap) {
//											avatar.setImageBitmap(bitmap);
//										}
//									});
//
//
//						}
//						return true;
//					}
//				}).placeholder(R.drawable.default_unlogin_image)
//				.error(R.drawable.default_unlogin_image)
//				.into(avatar);


	}
}
