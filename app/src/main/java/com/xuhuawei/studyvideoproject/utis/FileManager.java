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
* FileManager.java V1.0 2015-12-3 上午10:34:21
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.utis;

import java.io.File;

import android.os.Environment;

public class FileManager {

	public static String diskCachePath;
	
	private static final String ROOT = "/SSP";
	private static String AUDIO_DIR = null;
	private static String IMAGE_DIR ;
	private static String THUMB_IMAGE_DIR ;
	private static String VIDEO_DIR ;
	
	private static File getRootDir() {
		File rootFile;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			rootFile = Environment.getExternalStorageDirectory();
		} else {
			rootFile = new File("/");
		}
		return rootFile;
	}

	public static File getImageDir(){
		if(IMAGE_DIR==null){
			IMAGE_DIR=getRootDir()+ROOT + "/image";
		}
		File file=new File(IMAGE_DIR);
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}
	public static File getThumbImageDir(){
		if(THUMB_IMAGE_DIR==null){
			THUMB_IMAGE_DIR=getRootDir()+ROOT + "/image/thumb";
		}
		File file=new File(THUMB_IMAGE_DIR);
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}
	
	
	public static File getAudioDir(){
		if(AUDIO_DIR==null){
			AUDIO_DIR=getRootDir()+ROOT + "/audio";
		}
		File file=new File(AUDIO_DIR);
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}
	
	public static File getVideoDir(){
		if(VIDEO_DIR==null){
			VIDEO_DIR=getRootDir()+ROOT + "/video";
		}
		File file=new File(VIDEO_DIR);
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}
	
}
