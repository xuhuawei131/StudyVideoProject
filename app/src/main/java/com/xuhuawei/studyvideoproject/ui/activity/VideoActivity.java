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
 * VideoActivity.java V1.0 2015-12-3 上午10:30:11
 * <p>
 * Copyright JIAYUAN Co. ,Ltd. All rights reserved.
 * <p>
 * Modification history(By WAH-WAY):
 * <p>
 * Description:
 */

package com.xuhuawei.studyvideoproject.ui.activity;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import java.io.File;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuhuawei.studyvideoproject.InitHelper;
import com.xuhuawei.studyvideoproject.R;
import com.xuhuawei.studyvideoproject.bean.POMedia;
import com.xuhuawei.studyvideoproject.db.dao.VideoDBImpl;
import com.xuhuawei.studyvideoproject.ui.BaseActivity;
import com.xuhuawei.studyvideoproject.utis.DateUtils;
import com.xuhuawei.studyvideoproject.utis.DensityUtil;
import com.xuhuawei.studyvideoproject.utis.LongTaskThread;
import com.xuhuawei.studyvideoproject.utis.LongTaskThread.ILongTaskInterface;
import com.xuhuawei.studyvideoproject.utis.Utils;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class VideoActivity extends BaseActivity implements VideoView.OnGetDuringTimeListener, OnTouchListener, OnGestureListener, OnCompletionListener, OnInfoListener, ILongTaskInterface {
    private String mPath;
    private String mTitle;

    private VideoView mVideoView;
    private View mVolumeBrightnessLayout;
    private View gesture_progress_layout;
    private ImageView gesture_iv_progress;// 快进或快退标志
    private TextView geture_tv_progress_time;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;
    /**
     * 最大声音
     */
    private int mMaxVolume;
    /**
     * 当前声音
     */
    private int mVolume = -1;
    /**
     * 当前亮度
     */
    private float mBrightness = -1f;
    /**
     * 当前缩放模式
     */
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;
    private MediaController mMediaController;
    private View mLoadingView;
    /**
     * 是否需要自动恢复播放，用于自动暂停，恢复播放
     */
    private boolean needResume;
    private static final int DISTANCE = 50;
    private boolean firstScroll = false;
    private int GESTURE_FLAG = 0;// 1,调节进度，2，调节音量
    private static final int GESTURE_MODIFY_PROGRESS = 1;
    private static final int GESTURE_MODIFY_VOLUME = 2;
    private static final float STEP_PROGRESS = 6f;// 设定进度滑动时的步长，避免每次滑动都改变，导致改变过快
    private static final float STEP_VOLUME = 2f;// 协调音量滑动时的步长，避免每次滑动都改变，导致改变过快
    int windowWidth;//竖屏的宽是横屏的高
    int windowHeight;//竖屏的高是横屏的宽度
    private long palyerCurrentPosition = 0;// 模拟进度播放的当前标志，毫秒
    private long playerDuration = 0;// 模拟播放资源总时长，毫秒
    private static final String LOG = "xhw";
    //private static final float SETP_PROGRESS=DensityUtil.dip2px(STEP_PROGRESS);
    private POMedia mediaBean;
    private long during;

    @Override
    protected void initData() {
        Display disp = getWindowManager().getDefaultDisplay();
        windowWidth = disp.getWidth();//竖屏的宽是横屏的高
        windowHeight = disp.getHeight();//竖屏的高是横屏的宽度
        initIntent();
    }

    /**
     * 初始化init
     */
    private void initIntent() {
        Intent intent = this.getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            String fileName = Uri.decode(intent.getDataString());
            if (!TextUtils.isEmpty(fileName)) {
                if (fileName.startsWith("content://")) {
                    Uri selectedImage = intent.getData();
                    mPath = Utils.getAbsoluteVideoPath(this, selectedImage);
                } else {
                    int sdcardloaction = fileName.indexOf("sdcard");
                    mPath = "/mnt/" + fileName.substring(sdcardloaction);
                }
            }
        } else {
            if (intent.hasExtra("mediaBean")) {
                mediaBean = (POMedia) getIntent().getSerializableExtra("mediaBean");
                mPath = mediaBean.path;
            } else if (intent.hasExtra("path")) {
                mPath = intent.getStringExtra("path");
            }
        }


        if (TextUtils.isEmpty(mPath)) {
            showToast("视频文件不存在!");
            finish();
            return;
        }
        int lastIndex = mPath.lastIndexOf("/");
        mTitle = mPath.substring(lastIndex + 1);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_video_player;
    }

    @Override
    protected void findViewByIds() {
        View root_layout = findViewById(R.id.root_layout);
        root_layout.setLongClickable(true);
        root_layout.setOnTouchListener(this);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        gesture_progress_layout = findViewById(R.id.gesture_progress_layout);
        gesture_iv_progress = (ImageView) findViewById(R.id.gesture_iv_progress);
        geture_tv_progress_time = (TextView) findViewById(R.id.geture_tv_progress_time);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
        mLoadingView = findViewById(R.id.video_loading);


        // ~~~ 绑定事件
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnGetDuringTimeListener(this);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (mPath.startsWith("http:")) {
            mVideoView.setVideoURI(Uri.parse(mPath), 0);
        } else {
            if (mediaBean != null) {
                mVideoView.setVideoPath(mPath, mediaBean.position);
            } else {
                mVideoView.setVideoPath(mPath, 0);
            }

        }
        //设置显示名称
        mMediaController = (MediaController) findViewById(R.id.mediaController);
        mMediaController.setFileName(mTitle);
        mVideoView.setMediaController(mMediaController);
        mVideoView.requestFocus();
//		new MyGestureListener()
        mGestureDetector = new GestureDetector(this, this);
        mGestureDetector.setIsLongpressEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void requestService() {
        if (!Vitamio.isInitialized(this)) {
            showToast("第一次运行初始化，请耐心等待！");
            LongTaskThread task = new LongTaskThread(0, this);
            task.execute();
        }
        startPlayer();
    }

    //-----------------------ILongTaskInterface  start------------------------
    @Override
    public void doPreUITask(int index) {

    }

    @Override
    public Object doLongTask(int index) {
        //开始初始化vitamio
        InitHelper initHelper = new InitHelper(this);
        return initHelper.startInitVitamio();
    }

    @Override
    public void doEndUITask(int index, Object obj) {
        Boolean isInit = (Boolean) obj;
        if (isInit) {
            startPlayer();
        } else {
            finish();
        }
    }

    //-----------------------ILongTaskInterface end------------------------
    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                //开始缓存，暂停播放
                if (isPlaying()) {
                    stopPlayer();
                    needResume = true;
                }
                mLoadingView.setVisibility(View.VISIBLE);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                //缓存完成，继续播放
                if (needResume)
                    startPlayer();
                mLoadingView.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                //显示 下载速度
                //mListener.onDownloadRateChanged(arg2);
                break;
        }
        return true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null)
            mVideoView.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null)
            mVideoView.resume();
    }

    @Override
    public void onBackPressed() {
        if (mediaBean != null) {
            mediaBean.position = mVideoView.getCurrentPosition();
            VideoDBImpl.getInstance().updateVideoReadTime(mediaBean);
            Intent intent = new Intent();
            intent.putExtra("mediaBean", mediaBean);
            setResult(1, intent);
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null)
            mVideoView.stopPlayback();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mediaBean != null) {
            mediaBean.position = during;
        }
        onBackPressed();
    }

    public boolean onTouch(View v, MotionEvent event) {
        // 手势里除了singleTapUp，没有其他检测up的方法
        if (event.getAction() == MotionEvent.ACTION_UP) {
            GESTURE_FLAG = 0;// 手指离开屏幕后，重置调节音量或进度的标志
            mVolumeBrightnessLayout.setVisibility(View.INVISIBLE);
            gesture_progress_layout.setVisibility(View.INVISIBLE);
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        firstScroll = true;// 设定是触摸屏幕后第一次scroll的标志
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mMediaController.show();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        if (firstScroll) {
            // 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
            // 横向的距离变化大则调整进度，纵向的变化大则调整音量
            if (Math.abs(distanceX) >= Math.abs(distanceY)) {// 水平滑动
                mMediaController.hide();
                mVolumeBrightnessLayout.setVisibility(View.INVISIBLE);
                gesture_progress_layout.setVisibility(View.VISIBLE);
                GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
                palyerCurrentPosition = mVideoView.getCurrentPosition();
                playerDuration = mVideoView.getDuration();
            } else {// 垂直滑动
                mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
                gesture_progress_layout.setVisibility(View.INVISIBLE);
                GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
            }
        }
        // 如果每次触摸屏幕后第一次scroll是调节进度，那之后的scroll事件都处理音量进度，直到离开屏幕执行下一次操作
        if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {
            if (Math.abs(distanceX) > Math.abs(distanceY)) {// 横向移动大于纵向移动
                if (distanceX >= STEP_PROGRESS) {// 快退，用步长控制改变速度，可微调
                    gesture_iv_progress
                            .setImageResource(R.drawable.souhu_player_backward);
                    if (palyerCurrentPosition > 3 * 1000) {// 避免为负
                        palyerCurrentPosition -= 500;// scroll方法执行一次快退3秒
                    } else {
                        palyerCurrentPosition = 3 * 1000;
                    }
                } else if (distanceX <= -STEP_PROGRESS) {// 快进
                    gesture_iv_progress
                            .setImageResource(R.drawable.souhu_player_forward);
                    if (palyerCurrentPosition < playerDuration) {// 避免超过总时长
                        palyerCurrentPosition += 500;// scroll执行一次快进3秒
                    } else {
                        palyerCurrentPosition = playerDuration;
                    }
                }
            }
            geture_tv_progress_time.setText(DateUtils
                    .converLongTimeToStr(palyerCurrentPosition) + "/"
                    + DateUtils.converLongTimeToStr(playerDuration));
            mVideoView.seekTo(palyerCurrentPosition);
        }
        // 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
        else if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {
            float mOldX = e1.getX();
            float mOldY = e1.getY();

            float y = e2.getRawY();
            float x = e2.getRawX();
            if (mOldX > windowWidth * 1.0 / 2)// 右边滑动
                onVolumeSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 1.0)// 左边滑动
                onBrightnessSlide((mOldY - y) / windowHeight);
        }
        firstScroll = false;// 第一次scroll执行完成，修改标志
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        mVideoView.seekTo(palyerCurrentPosition);
        return false;
    }

    /**
     * 定时隐藏
     */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

    private void stopPlayer() {
        if (mVideoView != null)
            mVideoView.pause();
    }

    private void startPlayer() {
        if (mVideoView != null)
            mVideoView.start();
    }


    private boolean isPlaying() {
        return mVideoView != null && mVideoView.isPlaying();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mVideoView != null)
            mVideoView.setVideoLayout(mLayout, 0);
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (mVolume == -1) {
            if (mVolume < 0) {
                mVolume = 0;
            }
            // 显示
            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume) {
            index = mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }
        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        // 变更进度条
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f) {
                mBrightness = 0.50f;
            }
            if (mBrightness < 0.01f) {
                mBrightness = 0.01f;
            }
            // 显示
            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 获取
     *
     * @param during
     */
    @Override
    public void onGetDuringTime(long during) {
        if (mediaBean != null) {
            mediaBean.duration = during;
        }
        this.during = during;
    }
}
