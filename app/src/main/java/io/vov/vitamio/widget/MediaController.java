/*
 * Copyright (C) 2006 The Android Open Source Project
 * Copyright (C) 2013 YIXIA.COM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vov.vitamio.widget;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.utils.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.xuhuawei.studyvideoproject.R;

/**
 * A view containing controls for a MediaPlayer. Typically contains the buttons
 * like "Play/Pause" and a progress slider. It takes care of synchronizing the
 * controls with the state of the MediaPlayer.
 * <p/>
 * The way to use this class is to a) instantiate it programatically or b)
 * create it in your xml layout.
 * <p/>
 * a) The MediaController will create a default set of controls and put them in
 * a window floating above your application. Specifically, the controls will
 * float above the view specified with setAnchorView(). By default, the window
 * will disappear if left idle for three seconds and reappear when the user
 * touches the anchor view. To customize the MediaController's style, layout and
 * controls you should extend MediaController and override the {#link
 * {@link #setControllerContentView()} method.
 * <p/>
 * b) The MediaController is a FrameLayout, you can put it in your layout xml
 * and get it through {@link #findViewById(int)}.
 * <p/>
 * NOTES: In each way, if you want customize the MediaController, the SeekBar's
 * id must be mediacontroller_progress, the Play/Pause's must be
 * mediacontroller_pause, current time's must be mediacontroller_time_current,
 * total time's must be mediacontroller_time_total, file name's must be
 * mediacontroller_file_name. And your resources must have a pause_button
 * drawable and a play_button drawable.
 * <p/>
 * Functions like show() and hide() have no effect when MediaController is
 * created in an xml layout.
 */
public class MediaController extends FrameLayout implements OnClickListener, OnPreparedListener {
    private static final int sDefaultTimeout = 5000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private MediaPlayerControl mPlayer;
    private Context mContext;
    private SeekBar mProgress;
    private TextView mEndTime, mCurrentTime;
    private TextView mFileName;
    private OutlineTextView mInfoView;
    private String mTitle;
    private boolean mShowing;
    private boolean mDragging;
    /**
     * 拖动的时候是否播放
     **/
    private boolean mInstantSeeking = false;
    private ImageButton mPauseButton;
    private ImageButton mPreButton;
    private ImageButton mNextButton;
    private ImageView mScreenType;
    private ImageView mBack;
    private View view_empty;
    private AudioManager mAM;
    private OnShownListener mShownListener;
    private OnHiddenListener mHiddenListener;
    private VideoView mVideoView;
    private int mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    showProgress();
                    if (!mDragging && mShowing) {
                        updatePausePlayStatusIcon();
                        sendEmptyMessageDelayed(SHOW_PROGRESS, 100);
                    }
                    break;
            }
        }
    };


    public MediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        initController(context);
    }

    public MediaController(Context context) {
        super(context);
        initController(context);
    }

    private boolean initController(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.mediacontroller, null);
        mAM = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        addView(view);
        return true;
    }

    public void setAnchorView(VideoView mVideoView) {
        this.mVideoView = mVideoView;
        this.mVideoView.setOnPreparedListener(this);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        findControllerViewByIds();
    }

    /**
     * Create the view that holds the widgets that control playback. Derived
     * classes can override this to create their own.
     *
     * @return The controller view.
     */
    protected View setControllerContentView() {
        return ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(getResources().getIdentifier("mediacontroller", "layout", mContext.getPackageName()), this);
    }

    /**
     * 功能描述：
     * 初始化控件
     *
     * @author 许华维(WAH-WAY)
     * <p>创建日期 ：2015-12-5 下午6:49:16</p>
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    private void findControllerViewByIds() {
        view_empty = findViewById(getResources().getIdentifier("view_empty", "id", mContext.getPackageName()));

        mBack = (ImageButton) findViewById(getResources().getIdentifier("image_back", "id", mContext.getPackageName()));
        mPauseButton = (ImageButton) findViewById(getResources().getIdentifier("mediacontroller_play_pause", "id", mContext.getPackageName()));

        mNextButton = (ImageButton) findViewById(getResources().getIdentifier("mediacontroller_play_next", "id", mContext.getPackageName()));
        mPreButton = (ImageButton) findViewById(getResources().getIdentifier("mediacontroller_play_pre", "id", mContext.getPackageName()));
        mScreenType = (ImageButton) findViewById(getResources().getIdentifier("image_scale_mode", "id", mContext.getPackageName()));

        view_empty.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPreButton.setOnClickListener(this);
        mScreenType.setOnClickListener(this);

        if (mPauseButton != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(this);
        }

        mProgress = (SeekBar) findViewById(getResources().getIdentifier("mediacontroller_seekbar", "id", mContext.getPackageName()));
        if (mProgress != null) {
            mProgress.setOnSeekBarChangeListener(mSeekListener);
        }
        mEndTime = (TextView) findViewById(getResources().getIdentifier("mediacontroller_time_total", "id", mContext.getPackageName()));
        mCurrentTime = (TextView) findViewById(getResources().getIdentifier("mediacontroller_time_current", "id", mContext.getPackageName()));
        mFileName = (TextView) findViewById(getResources().getIdentifier("mediacontroller_file_name", "id", mContext.getPackageName()));
        if (mFileName != null)
            mFileName.setText(mTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                Activity activity=(Activity)mContext;
                if(activity!=null){
                    activity.onBackPressed();
                }
                break;
            case R.id.view_empty:
                hide();
                break;
            case R.id.mediacontroller_play_pause:
                doPauseResume();
                show(sDefaultTimeout);
                break;
            case R.id.mediacontroller_play_next:

                break;
            case R.id.mediacontroller_play_pre:

                break;
            case R.id.image_scale_mode:
                if (mLayout == VideoView.VIDEO_LAYOUT_STRETCH) {
                    mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
                } else if (mLayout == VideoView.VIDEO_LAYOUT_ORIGIN) {
                    mLayout = VideoView.VIDEO_LAYOUT_SCALE;
                } else if (mLayout == VideoView.VIDEO_LAYOUT_SCALE) {
                    mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
                } else if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM) {
                    mLayout = VideoView.VIDEO_LAYOUT_STRETCH;
                }
                if (mVideoView != null) {
                    mVideoView.setVideoLayout(mLayout, 0);
                }
                hide();
                break;
        }
    }


    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlayStatusIcon();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        long duration = mPlayer.getDuration();
        mProgress.setMax((int) duration);
        if (mEndTime != null) {
            mEndTime.setText(StringUtils.generateTime(duration));
        }
    }

    /**
     * Control the action when the seekbar dragged by user
     *
     * @param seekWhenDragging True the media will seek periodically
     */
    public void setInstantSeeking(boolean seekWhenDragging) {
        mInstantSeeking = seekWhenDragging;
    }

    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Set the content of the file_name TextView
     *
     * @param name
     */
    public void setFileName(String name) {
        mTitle = name;
        if (mFileName != null)
            mFileName.setText(mTitle);
    }

    /**
     * Set the View to hold some information when interact with the
     * MediaController
     *
     * @param v
     */
    public void setInfoView(OutlineTextView v) {
        mInfoView = v;
    }


    /**
     * Show the controller on screen. It will go away automatically after
     * 'timeout' milliseconds of inactivity.
     *
     * @param timeout The timeout in milliseconds. Use 0 to show the controller
     *                until hide() is called.
     */
    public void show(int timeout) {
        if (!mShowing) {
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            setVisibility(View.VISIBLE);
            mShowing = true;
            if (mShownListener != null)
                mShownListener.onShown();
        }
        updatePausePlayStatusIcon();
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT),
                    timeout);
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

    public void hide() {
        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                setVisibility(View.GONE);
            } catch (IllegalArgumentException ex) {
            }
            mShowing = false;
            if (mHiddenListener != null)
                mHiddenListener.onHidden();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

    }

    public void setOnShownListener(OnShownListener l) {
        mShownListener = l;
    }

    public void setOnHiddenListener(OnHiddenListener l) {
        mHiddenListener = l;
    }

    private void showProgress() {
        if (mPlayer == null || mDragging) {
            return;
        }
        long position = mPlayer.getCurrentPosition();
        if (mProgress != null) {
            mProgress.setProgress((int) position);
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }
        if (mCurrentTime != null) {
            mCurrentTime.setText(StringUtils.generateTime(position));
        }
    }
//  @Override
//  public boolean onTouchEvent(MotionEvent event) {
//    show(sDefaultTimeout);
//    return true;
//  }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() == 0
                && (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_SPACE)) {
            doPauseResume();
            show(sDefaultTimeout);
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlayStatusIcon();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_MENU) {
            hide();
            return true;
        } else {
            show(sDefaultTimeout);
        }
        return super.dispatchKeyEvent(event);
    }

    private void updatePausePlayStatusIcon() {
        if (mPauseButton == null) {
            return;
        }
        if (mPlayer.isPlaying()) {
            mPauseButton.setImageResource(getResources().getIdentifier(
                    "mediacontroller_pause", "drawable",
                    mContext.getPackageName()));
        } else {
            mPauseButton.setImageResource(getResources().getIdentifier(
                    "mediacontroller_play", "drawable",
                    mContext.getPackageName()));
        }
    }

    private void doPauseResume() {
        if (mPlayer.isPlaying())
            mPlayer.pause();
        else
            mPlayer.start();
        updatePausePlayStatusIcon();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null)
            mPauseButton.setEnabled(enabled);
        if (mProgress != null)
            mProgress.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            mDragging = true;
            show(3600000);
            mHandler.removeMessages(SHOW_PROGRESS);
            mHandler.removeMessages(FADE_OUT);
            if (mInstantSeeking)
                mAM.setStreamMute(AudioManager.STREAM_MUSIC, true);
            if (mInfoView != null) {
                mInfoView.setText("");
                mInfoView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress,
                                      boolean fromuser) {
            if (!fromuser) {
                return;
            }
            long position = mPlayer.getCurrentPosition();
            String time = StringUtils.generateTime(position);
            if (mInstantSeeking)
                mPlayer.seekTo(mProgress.getProgress());
            if (mInfoView != null)
                mInfoView.setText(time);
            if (mCurrentTime != null)
                mCurrentTime.setText(time);
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            if (!mInstantSeeking) {
                mPlayer.seekTo(mProgress.getProgress());
            }
            if (mInfoView != null) {
                mInfoView.setText("");
                mInfoView.setVisibility(View.GONE);
            }
            show(sDefaultTimeout);
            mAM.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mDragging = false;
            mHandler.removeMessages(SHOW_PROGRESS);
            mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 100);
        }
    };

    public interface OnShownListener {
        public void onShown();
    }

    public interface OnHiddenListener {
        public void onHidden();
    }

    public interface MediaPlayerControl {
        void start();

        void pause();

        long getDuration();

        long getCurrentPosition();

        void seekTo(long pos);

        boolean isPlaying();

        int getBufferPercentage();
    }
}
