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
* VideoDbHalper.java V1.0 2015-12-3 下午9:56:06
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
		* 功能描述：
		* 视频数据库集合
		* @author 许华维(WAH-WAY)
		*
		* <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class VideoDBHelper extends SQLiteOpenHelper implements IVideoTablColumns{
	
	private static final String CREATTABLE = "CREATE TABLE " + TABLE_NAME
			+ " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ TITLE+ " VARCHAR(400), " 
			+ TITLE_KEY + " VARCHAR(200), " 
			+ PATH+ " VARCHAR(400), " 
			+ LAST_ACCESS_TIME + " LONG, " 
			+ LAST_MODIFY_TIME+ "  LONG, " 
			+ DURATION + " INTEGER, " 
			+ POSITION + " INTEGER, "
			+ THUMB_PATH + " VARCHAR(400),"
			+ FILE_SIZE + " LONG, " 
			+ WIDTH +" INTEGER, "
			+ HEIGHT+" INTEGER,"
			+ MIME_TYPE+" VARCHAR(200), "
			+ TYPE+" INTEGER, "
			+ STATUS+" INTEGER, "//UNIQUE, 
			+ TEMP_FILE_SIZE+" VARCHAR(400) " 
			+")";
	
	public VideoDBHelper(Context context){
		this(context,DATA_NAME,null,DATABASE_VERSION);
	}
	public VideoDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATTABLE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		int version = oldVersion;
		if (version != DATABASE_VERSION) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}
}
