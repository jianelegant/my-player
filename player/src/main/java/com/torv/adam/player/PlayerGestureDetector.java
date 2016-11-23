package com.torv.adam.player;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.torv.adam.libs.utils.L;

/**
 * Created by AdamLi on 2016/11/23.
 */

public class PlayerGestureDetector {

    private static final String TAG = PlayerGestureListener.class.getSimpleName();

    private final GestureDetector mGestureDetector;
    private final PlayerUIController.GestureCallback mGestureCallback;

    public PlayerGestureDetector(Context context, PlayerUIController.GestureCallback gestureCallback) {
        mGestureDetector = new GestureDetector(context, new PlayerGestureListener());
        mGestureCallback = gestureCallback;
    }

    public boolean onTouchEvent(MotionEvent event){
        return mGestureDetector.onTouchEvent(event);
    }

    class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            L.d(TAG);
            return super.onScroll(e1, e2, distanceX, distanceY);
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
}
