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
* TimerBean.java V1.0 2015-12-3 上午11:43:37
*
* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
*
* Modification history(By WAH-WAY):
*
* Description:
*/

package com.xuhuawei.studyvideoproject.bean;

import java.io.Serializable;

public class TimerBean implements Serializable {
	private static final long serialVersionUID = 8311143221349005458L;
	public String action;
	public int count;
	public boolean isAdd=false;
	public int MaxOrMin;
	/**
	 * 
			* 构造函数：
			*
			* @param action
			* @param MaxOrMin
			* @param isAdd
	 */
	public TimerBean(String action,int MaxOrMin,boolean isAdd){
		this.action=action;
		this.MaxOrMin=MaxOrMin;
		this.isAdd=isAdd;
		if(isAdd){
			count=0;
		}else{
			count=MaxOrMin;
			MaxOrMin=0;
		}
	}
	
	
	@Override
	public boolean equals(Object o) {
		TimerBean other=(TimerBean)o;
		if(other!=null){
			return action.equals(other.action);
		}else{
			return false;
		}
	}
}
