/**
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\  =  /O
 * ____/`---'\____
 * .'  \\|     |//  `.
 * /  \\|||  :  |||//  \
 * /  _||||| -:- |||||-  \
 * |   | \\\  -  /// |   |
 * | \_|  ''\---/''  |   |
 * \  .-\__  `-`  ___/-. /
 * ___`. .'  /--.--\  `. . __
 * ."" '<  `.___\_<|>_/___.'  >'"".
 * | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 佛祖保佑       永无BUG
 * FragmentLocal.java V1.0 2015-12-3 下午3:46:31
 * <p>
 * Copyright JIAYUAN Co. ,Ltd. All rights reserved.
 * <p>
 * Modification history(By WAH-WAY):
 * <p>
 * Description:
 */

package com.xuhuawei.studyvideoproject.ui.fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xuhuawei.studyvideoproject.R;
import com.xuhuawei.studyvideoproject.adapters.LocalVideoAdapter;
import com.xuhuawei.studyvideoproject.bean.POMedia;
import com.xuhuawei.studyvideoproject.db.dao.VideoDBImpl;
import com.xuhuawei.studyvideoproject.service.ScanService;
import com.xuhuawei.studyvideoproject.ui.activity.VideoActivity;
import com.xuhuawei.studyvideoproject.utis.DensityUtil;
import com.xuhuawei.studyvideoproject.utis.LongTaskThread;
import com.xuhuawei.studyvideoproject.utis.LongTaskThread.ILongTaskInterface;

/**
 * 功能描述：
 * 本地视频
 *
 * @author 许华维(WAH-WAY)
 *         <p>
 *         <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class FragmentLocal extends BaseFragment implements ILongTaskInterface, OnItemClickListener, OnClickListener {
    private ListView mListView;
    private ArrayList<POMedia> arrayList;
    private LocalVideoAdapter adapter;
    private PopupWindow menuDialog;
    private LongTaskThread task;
    private static final int REQUEST_CODE = 100;

    @Override
    protected void initData(Bundle bundle) {
        IntentFilter intentFilter = new IntentFilter(ScanService.ACTION_FILE_SEARCH);
        context.registerReceiver(mBroadcastReceiver, intentFilter);

        arrayList = new ArrayList<POMedia>();
        adapter = new LocalVideoAdapter(context, arrayList);
        task = new LongTaskThread(0, this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_local;
    }

    @Override
    protected void findViewByIds() {
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void setTitleView() {
        text_title.setText("本地视频");
        text_right.setBackgroundResource(R.drawable.dialog_btn_menu_more_selector);
        text_right.setOnClickListener(this);
    }

    public List<String> getExtSDCardPath() {
        List<String> lResult = new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return lResult;
    }

    @Override
    protected void requestSerivce() {
        if (!ScanService.isRunning()) {//扫描的服务器 启动
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 
            if (!sdCardExist) {
                showToast("SD卡不存在！");
                return;
            }
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();

            Intent intent = new Intent(context, ScanService.class);
            Bundle bundle = new Bundle();
            bundle.putString(ScanService.EXTRA_DIRECTORY, rootDir);
            intent.putExtras(bundle);
            context.startService(intent);
        }
        task.execute();
    }

    @Override
    public void onClick(View v) {
        showMenuDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(mBroadcastReceiver);

        Intent intent = new Intent(context, ScanService.class);
        context.stopService(intent);
    }

    /**
     * 功能描述：
     * 显示菜单对话框
     *
     * @author 许华维(WAH-WAY)
     * <p>创建日期 ：2015-12-3 下午5:11:01</p>
     * <p>
     * <p>
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    private void showMenuDialog() {
        if (menuDialog != null && menuDialog.isShowing()) {
            menuDialog.dismiss();
        }

        View popupWindow_view = View.inflate(context, R.layout.layout_dialog_menu, null);
        TextView text_open = (TextView) popupWindow_view.findViewById(R.id.text_open);
        TextView text_edit = (TextView) popupWindow_view.findViewById(R.id.text_edit);
        TextView text_delete = (TextView) popupWindow_view.findViewById(R.id.text_delete);
        TextView text_code = (TextView) popupWindow_view.findViewById(R.id.text_code);
        TextView text_play = (TextView) popupWindow_view.findViewById(R.id.text_play);

        text_open.setOnClickListener(popWindowListener);
        text_edit.setOnClickListener(popWindowListener);
        text_delete.setOnClickListener(popWindowListener);
        text_code.setOnClickListener(popWindowListener);
        text_play.setOnClickListener(popWindowListener);

        menuDialog = new PopupWindow(popupWindow_view, DensityUtil.dip2px(150), LayoutParams.WRAP_CONTENT, true);
        menuDialog.setBackgroundDrawable(new BitmapDrawable());
        menuDialog.setOutsideTouchable(true);
        menuDialog.showAsDropDown(text_right);
    }

    private OnClickListener popWindowListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (menuDialog != null && menuDialog.isShowing()) {
                menuDialog.dismiss();
            }
            switch (v.getId()) {
                case R.id.text_open:

                    break;
                case R.id.text_edit:

                    break;
                case R.id.text_delete:

                    break;
                case R.id.text_code:

                    break;
                case R.id.text_play:

                    break;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        POMedia bean = arrayList.get(arg2);
        File file = new File(bean.path);
        if (file.exists()) {
            Intent intent = new Intent(context, VideoActivity.class);
            intent.putExtra("mediaBean", bean);
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            VideoDBImpl.getInstance().deleteVideo(bean.path, null);
            arrayList.remove(arg2);
            adapter.notifyDataSetChanged();
            showToast("视频不存在!");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                POMedia mediaBean = (POMedia) data.getSerializableExtra("mediaBean");
                for (POMedia item : arrayList) {
                    if (!TextUtils.isEmpty(item.path) && !TextUtils.isEmpty(mediaBean.path)) {
                        if (item.path.equals(mediaBean.path)) {
                            item.position = mediaBean.position;
                            item.duration = mediaBean.duration;
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void doPreUITask(int index) {

    }

    @Override
    public Object doLongTask(int index) {
        ArrayList<POMedia> dbList = VideoDBImpl.getInstance().getAllLocalList();
        return dbList;
    }

    @Override
    public void doEndUITask(int index, Object obj) {
        ArrayList<POMedia> dbList = (ArrayList<POMedia>) obj;
        if (dbList != null && dbList.size() > 1) {
            arrayList.addAll(dbList);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 广播监听扫描的POMedia
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            POMedia bean = (POMedia) intent.getSerializableExtra("POMedia");
            arrayList.add(bean);
            adapter.notifyDataSetChanged();
        }
    };
}
