package com.torv.adam.player;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.torv.adam.libs.utils.L;
import com.torv.adam.libs.utils.Util;
import com.torv.adam.player.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by AdamLi on 2016/11/23.
 */

public class PlayerUIController {

    private static final java.lang.String TAG = PlayerUIController.class.getSimpleName();

    private final Activity mActivity;
    private final View mRootView;
    private final IjkVideoView mVideoView;

    private final View mTopController;
    private final View mBottomController;
    private final PlayerGestureDetector mGestureDetector;

    private int mDuration;
    private boolean isSeekBarTracking = false;

    /** Buttons */
    private ImageView mPauseOrPlayView;
    private ImageView mBackView;
    private TextView mTitleView;
    private TextView mPlayTimeView;
    private TextView mDurationView;
    private SeekBar mProgressView;

    /** Some Value*/
    private static final int PROGRESS_MAX = 1000;

    /** Delay event handler*/
    // Hide controller
    private static final int HANDLER_MSG_HIDE_CONTROLLER = 1;
    private static final int HANDLER_HIDE_CONTROLLER_DELAY_TIME = 3000; // 3 second

    // Update progress
    private static final int HANDLER_MSG_UPDATE_PROGRESS = 2;
    private static final int HANDLER_UPDATE_PROGRESS_DELAY_TIME = 1000;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_MSG_HIDE_CONTROLLER:
                    hideOrShowController();
                    break;
                case HANDLER_MSG_UPDATE_PROGRESS:
                    updateProgress();
                    break;
                default:
                    break;
            }
        }
    };

    public PlayerUIController(Activity activity, View rootView, IjkVideoView videoView) {
        this.mActivity = activity;
        this.mRootView = rootView;
        this.mVideoView = videoView;
        mTopController = mRootView.findViewById(R.id.id_top_controller);
        mBottomController = mRootView.findViewById(R.id.id_bottom_controller);
        mGestureDetector = new PlayerGestureDetector(activity, mGestureCallback);
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });
        initView();
        initListeners();
    }

    private void initListeners() {
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                setDuration();
            }
        });
    }

    private void setDuration() {
        mDuration = mVideoView.getDuration();
        mDurationView.setText(Util.convertMs2HMS(mDuration));
    }

    private void initView() {
        mPauseOrPlayView = (ImageView) mRootView.findViewById(R.id.id_pause);
        mPauseOrPlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseOrPlay();
            }
        });

        mBackView = (ImageView) mRootView.findViewById(R.id.id_back);
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mActivity) {
                    mActivity.finish();
                }
            }
        });

        mTitleView = (TextView) mRootView.findViewById(R.id.id_title);

        mPlayTimeView = (TextView) mRootView.findViewById(R.id.id_play_time);
        mDurationView = (TextView) mRootView.findViewById(R.id.id_duration);
        mProgressView = (SeekBar) mRootView.findViewById(R.id.id_progress);
        mProgressView.setMax(PROGRESS_MAX);

        mProgressView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                L.d(TAG, "progress="+progress+", fromUser=" + fromUser);
                if(fromUser) {
                    onUserSeek(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                L.d(TAG);
                isSeekBarTracking = true;
                mHandler.removeMessages(HANDLER_MSG_UPDATE_PROGRESS);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                L.d(TAG);
                isSeekBarTracking = false;
                updateProgress();
            }
        });
    }

    private void onUserSeek(int progress) {
        if(mDuration > 0) {
            double percent = (double) progress / (double) PROGRESS_MAX;
            int currentPosition = (int) (mDuration * percent);
            mVideoView.seekTo(currentPosition);
            mPlayTimeView.setText(Util.convertMs2HMS(currentPosition));
        }
    }

    private GestureCallback mGestureCallback = new GestureCallback() {
        @Override
        public void onPauseOrPlay() {
            pauseOrPlay();
        }

        @Override
        public void onHideOrShowController() {
            hideOrShowController();
        }

        @Override
        public void onFF() {

        }

        @Override
        public void onRewind() {

        }

        @Override
        public void onVolumeUp() {

        }

        @Override
        public void onVolumeDown() {

        }

        @Override
        public void onBrightUp() {

        }

        @Override
        public void onBrightDown() {

        }
    };

    private void hideOrShowController() {
        if(null == mTopController || null == mBottomController) {
            L.e("bar is null");
            return;
        }

        if(mHandler.hasMessages(HANDLER_MSG_HIDE_CONTROLLER)) {
            mHandler.removeMessages(HANDLER_MSG_HIDE_CONTROLLER);
        }

        if(View.VISIBLE == mTopController.getVisibility()) {
            // Hide
            if(isSeekBarTracking) {
                mHandler.sendEmptyMessageDelayed(HANDLER_MSG_HIDE_CONTROLLER, HANDLER_HIDE_CONTROLLER_DELAY_TIME);
            } else {
                mTopController.setVisibility(View.GONE);
                mBottomController.setVisibility(View.GONE);
            }
        } else {
            // Show
            mTopController.setVisibility(View.VISIBLE);
            mBottomController.setVisibility(View.VISIBLE);
            updateProgress();
            mHandler.sendEmptyMessageDelayed(HANDLER_MSG_HIDE_CONTROLLER, HANDLER_HIDE_CONTROLLER_DELAY_TIME);
        }
    }

    private void updateProgress() {
        if(mDuration > 0) {
            int currentPosition = mVideoView.getCurrentPosition();
            mPlayTimeView.setText(Util.convertMs2HMS(currentPosition));
            double percent = (double) currentPosition / (double) mDuration;
            int progress = (int) (percent * PROGRESS_MAX);
            mProgressView.setProgress(progress);
        }
        if(null != mTopController && View.VISIBLE == mBottomController.getVisibility() && mVideoView.isPlaying()) {
            mHandler.sendEmptyMessageDelayed(HANDLER_MSG_UPDATE_PROGRESS, HANDLER_UPDATE_PROGRESS_DELAY_TIME);
        }
    }

    private void pauseOrPlay() {
        if(null != mVideoView) {
            if(mVideoView.isPlaying()) {
                // Pause
                mVideoView.pause();
                mPauseOrPlayView.setImageResource(R.drawable.play_icon);
            } else {
                // Paly
                mVideoView.start();
                mPauseOrPlayView.setImageResource(R.drawable.pause_icon);
                updateProgress();
            }
        }
    }

    public void setTitle(String name) {
        if(!TextUtils.isEmpty(name) && null != mTitleView) {
            mTitleView.setText(name);
        }
    }

    public interface GestureCallback{
        public void onPauseOrPlay();
        public void onHideOrShowController();
        public void onFF();
        public void onRewind();
        public void onVolumeUp();
        public void onVolumeDown();
        public void onBrightUp();
        public void onBrightDown();
    }
}
