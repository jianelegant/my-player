package com.torv.adam.player;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.torv.adam.libs.utils.L;
import com.torv.adam.libs.utils.Util;
import com.torv.adam.player.media.IjkVideoView;

/**
 * Created by AdamLi on 2016/11/23.
 */

public class PlayerGestureDetector {

    private static final String TAG = PlayerGestureListener.class.getSimpleName();

    private static final int BRIGHTNESS_FACTOR = 3;
    private static final int VOLUME_FACTOR = 3;

    private enum ScrollType {
        NONE,
        VOLUME,
        BRIGHTNESS,
        FFORREWIND
    }
    private ScrollType mScrollType = ScrollType.NONE;

    private final Activity mActivity;
    private final IjkVideoView mVideoView;
    private final GestureDetector mGestureDetector;
    private final PlayerUIController.GestureCallback mGestureCallback;

    public PlayerGestureDetector(Activity activity, IjkVideoView videoView, PlayerUIController.GestureCallback gestureCallback) {
        mActivity = activity;
        mGestureDetector = new GestureDetector(activity, new PlayerGestureListener());
        mVideoView = videoView;
        mGestureCallback = gestureCallback;
    }

    public void onTouchEvent(MotionEvent event){
        mGestureDetector.onTouchEvent(event);

        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            mScrollType = ScrollType.NONE;
            mGestureCallback.hidePanel();
        }
    }

    class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            L.d(TAG);
            if(ScrollType.NONE == mScrollType) {
                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    mScrollType = ScrollType.FFORREWIND;
                } else {
                    if (e1.getX() < mVideoView.getMeasuredWidth() / 2) {
                        mScrollType = ScrollType.BRIGHTNESS;
                    } else {
                        mScrollType = ScrollType.VOLUME;
                    }
                }
            }
            return calculateGesture(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            L.d(TAG);
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            L.d(TAG);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            L.d(TAG);
            mGestureCallback.onPauseOrPlay();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            L.d(TAG);
            mGestureCallback.onHideOrShowController();
            return super.onSingleTapConfirmed(e);
        }
    }

    private boolean calculateGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(null == mVideoView) {
            return false;
        }
        if(ScrollType.FFORREWIND == mScrollType) {
            // FF or Rewind
            float percent = distanceX / (float) mVideoView.getMeasuredWidth();
            int total = mVideoView.getDuration();
            int currPosition = mVideoView.getCurrentPosition();
            int seekOffset = (int) (total * percent * -1);

            currPosition += seekOffset;
            if (currPosition < 0) {
                currPosition = 0;
            } else if (currPosition > total) {
                currPosition = total;
            }

            mVideoView.seekTo(currPosition);

            if (seekOffset > 0) {
                mGestureCallback.onFF(currPosition, total);
            } else {
                mGestureCallback.onRewind(currPosition, total);
            }
        } else if(ScrollType.BRIGHTNESS == mScrollType) {
            // Brightness
            float percent = distanceY / (float) mVideoView.getMeasuredHeight();
            float brightness = Util.getBrightness(mActivity);
            float brightnessOffset = percent * BRIGHTNESS_FACTOR;

            brightness += brightnessOffset;
            if (brightness < 0) {
                brightness = 0;
            } else if (brightness > 1) {
                brightness = 1;
            }

            Util.setBrightness(mActivity, brightness);
            mGestureCallback.onBrightnessChanged(brightness);
        } else if(ScrollType.VOLUME == mScrollType) {
            // Volume
            float percent = distanceY / (float) mVideoView.getMeasuredHeight();

            AudioManager manager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int currVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float volumeOffsetAccurate = maxVolume * percent * VOLUME_FACTOR;
            int volumeOffset = (int) volumeOffsetAccurate;

            if (volumeOffset == 0 && Math.abs(volumeOffsetAccurate) > 0.2f) {
                if (distanceY > 0) {
                    volumeOffset = 1;
                } else if (distanceY < 0) {
                    volumeOffset = -1;
                }
            }

            currVolume += volumeOffset;
            if (currVolume < 0) {
                currVolume = 0;
            } else if (currVolume >= maxVolume) {
                currVolume = maxVolume;
            }

            manager.setStreamVolume(AudioManager.STREAM_MUSIC, currVolume, 0);

            float volumePercent = (float) currVolume / (float) maxVolume;
            mGestureCallback.onVolumeChanged(volumePercent);
        }
        return true;
    }
}
