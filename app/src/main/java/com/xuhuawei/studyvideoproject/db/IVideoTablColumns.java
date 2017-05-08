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
* IVideoTablColumns.java V1.0 2015-12-3 下午9:57:44
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.db;


public interface IVideoTablColumns {
	public static final String DATA_NAME="video.db";
	public static final String TABLE_NAME="videolist";
	public static final int DATABASE_VERSION=1;
	
	public static final String ID="_id";
	/** 视频标题 */
	public static final String TITLE="title";
	/** 视频标题拼音 */
	public static final String TITLE_KEY="title_key";
	/** 视频路径 */
	public static final String PATH="path";
	/** 最后一次访问时间 */
	public static final String LAST_ACCESS_TIME="last_access_time";
	/** 最后一次修改时间 */
	public static final String LAST_MODIFY_TIME="last_modify_time";
	/** 视频时长 */
	public static final String DURATION="duration";
	/** 视频播放进度 */
	public static final String POSITION="position";
	/** 视频缩略图路径 */
	public static final String THUMB_PATH="thumb_path";
	/** 文件大小 */
	public static final String FILE_SIZE="file_size";
	/** 视频宽度 */
	public static final String WIDTH="width";
	/** 视频高度 */
	public static final String HEIGHT="height";
	/** MIME类型 */
	public static final String MIME_TYPE="mime_type";
	/** 0 本地视频 1 网络视频 */
	public static final String TYPE="type";
	/** 文件状态0 - 10 分别代表 下载 0-100% */
	public static final String STATUS="status";
	/** 文件临时大小 用于下载 */
	public static final String TEMP_FILE_SIZE="temp_file_size";
}
