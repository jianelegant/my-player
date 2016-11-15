package com.torv.adam.aplayer.mediacodec;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MediaCodecActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private PlayerThread mPlayerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurfaceView surfaceView = new SurfaceView(MediaCodecActivity.this);
        surfaceView.getHolder().addCallback(this);
        setContentView(surfaceView);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (null == mPlayerThread) {
            mPlayerThread = new PlayerThread(holder.getSurface());
            mPlayerThread.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mPlayerThread.interrupt();
        mPlayerThread = null;
    }
}
