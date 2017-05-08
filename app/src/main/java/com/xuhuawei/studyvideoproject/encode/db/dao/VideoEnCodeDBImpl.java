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
 * VideoDbImpl.java V1.0 2015-12-3 下午9:59:31
 *
 * Copyright JIAYUAN Co. ,Ltd. All rights reserved.
 *
 * Modification history(By WAH-WAY):
 *
 * Description:
 */

package com.xuhuawei.studyvideoproject.encode.db.dao;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;


import android.content.ContentValues;
import android.util.Log;

import com.xuhuawei.studyvideoproject.MyVideoApplication;
import com.xuhuawei.studyvideoproject.bean.POMedia;
import com.xuhuawei.studyvideoproject.db.IVideoTablColumns;
import com.xuhuawei.studyvideoproject.encode.db.VideoEnCodeDBHelper;
/**
 * 
		* 功能描述：
		* 视频记录管理的类
		* @author 许华维(WAH-WAY)
		*
		* <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class VideoEnCodeDBImpl implements IVideoTablColumns {
	private static VideoEnCodeDBImpl instance;
	private VideoEnCodeDBHelper dbHepler;
	private VideoEnCodeDBImpl() {
		dbHepler = new VideoEnCodeDBHelper(MyVideoApplication.getInstance());
	}
	
	public static VideoEnCodeDBImpl getInstance() {
		if (instance == null) {
			instance = new VideoEnCodeDBImpl();
		}
		return instance;
	}
	
	private  SQLiteDatabase getDBHelper() {
		SQLiteDatabase  database=null;
		try{
			database=dbHepler.getWritableDatabase("xuhuawei");
			return database;
		}catch(Exception e){
			Log.v("xhw", "exception "+e.getLocalizedMessage());
			return null;
		}
	}
	
	/**
	 * 
			* 功能描述：
			* 插入数据库
			* @author 许华维(WAH-WAY)
			* <p>创建日期 ：2015-12-4 下午2:53:08</p>
			*
			* @param media
			* @return
			*
			* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public synchronized long insertVideo(POMedia media){
		SQLiteDatabase db = getDBHelper();
		ContentValues values = new ContentValues();
		values.put(TITLE, media.title);
		values.put(TITLE_KEY, media.title_key);
		values.put(PATH, media.path);
		values.put(LAST_ACCESS_TIME, media.last_access_time);
		values.put(LAST_MODIFY_TIME, media.last_modify_time);
		values.put(DURATION, media.duration);
		values.put(POSITION, media.position);
		values.put(THUMB_PATH, media.thumb_path);
		values.put(FILE_SIZE, media.file_size);
		values.put(WIDTH, media.width);
		values.put(HEIGHT, media.height);
		values.put(MIME_TYPE, media.mime_type);
		values.put(TYPE, media.type);
		values.put(STATUS, media.status);
		values.put(TEMP_FILE_SIZE, media.temp_file_size);
		try{
			long i=db.insert(TABLE_NAME, null, values);
			return i;
		}catch(Exception e){
			return -1;
		}
	}

	public synchronized void updateVideoReadTime(POMedia media){
		SQLiteDatabase db = getDBHelper();
		ContentValues values = new ContentValues();
		values.put(POSITION, media.position);
		values.put(DURATION, media.duration);
		db.update(TABLE_NAME,values,PATH+"=?",new String[]{media.path});
	}

	/**
	 * 
			* 功能描述：
			* 视频是否入库
			* @author 许华维(WAH-WAY)
			* <p>创建日期 ：2015-12-4 上午11:23:53</p>
			*
			* @param filePath
			* @param last_modify_time
			* @return
			*
			* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public synchronized boolean exists(String filePath, long last_modify_time) {
		SQLiteDatabase database = getDBHelper();
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATH
				+ " = ? and " + LAST_MODIFY_TIME + " =?";
		Cursor c = database.rawQuery(sql, new String[] { filePath,
				last_modify_time + "" });
		if (c != null && c.moveToNext()) {
			return true;
		}
		return false;
	}
	public synchronized SQLiteDatabase getDataBase(){
		return getDBHelper();
	}
	
	
	/**
	 * 
			* 功能描述：
			* 关闭数据库
			* @author 许华维(WAH-WAY)
			* <p>创建日期 ：2015-12-4 下午2:54:17</p>
			*
			*
			* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public synchronized void closeDB() {
		if(dbHepler!=null){
			dbHepler.close();
		}
		dbHepler = null;
		instance = null;
	}
}
