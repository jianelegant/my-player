package com.torv.adam.player;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.Toast;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.torv.adam.libs.utils.AsyncJobMgr;
import com.torv.adam.libs.utils.L;
import com.torv.adam.player.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerActivity extends AppCompatActivity {

    private static final String EXTRA_KEY_PATH = "extra_key_path";
    private static final String EXTRA_KEY_NAME = "extra_key_name";

    private IjkVideoView mVideoView;
    private TableLayout mHudView;

    private PlayerUIController mUIController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initViews();

        initPlayer();
    }

    public static void jumpTo(Context context, String path, String name){
        if(null == context || TextUtils.isEmpty(path)) {
            Toast.makeText(context, "Path is null", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(EXTRA_KEY_PATH, path);
        intent.putExtra(EXTRA_KEY_NAME, name);
        context.startActivity(intent);
    }

    private void initPlayer() {
        mVideoView.setHudView(mHudView);

        Intent intent = getIntent();
        String path = intent.getStringExtra(EXTRA_KEY_PATH);
        String name = intent.getStringExtra(EXTRA_KEY_NAME);
        mVideoView.setVideoPath(path);

        View rootView = findViewById(R.id.id_activity_player);
        mUIController = new PlayerUIController(PlayerActivity.this, rootView, mVideoView);
        mUIController.setTitle(name);

        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                finish();
            }
        });
    }

    private void initViews() {
        mVideoView = (IjkVideoView) findViewById(R.id.id_videoview);
        mHudView = (TableLayout) findViewById(R.id.hud_view);
        if(L.getIsDebug()) {
            mHudView.setVisibility(View.VISIBLE);
        }

        hideSystemUI();
    }

    private void hideSystemUI() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        window.setFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN, View
                .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(null != mVideoView) {
            mVideoView.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null != mVideoView) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mVideoView) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /** load player libraries, only call once when application start*/
    public static void loadLibrariesOnce() {
        AsyncJobMgr.instance.addAsyncJob(new Job(new Params(AsyncJobMgr.PRIORITY_HIGH)) {
            @Override
            public void onAdded() {}

            @Override
            public void onRun() throws Throwable {
                IjkMediaPlayer.loadLibrariesOnce(null);
            }

            @Override
            protected void onCancel(int cancelReason, @Nullable Throwable throwable) {}

            @Override
            protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
                return null;
            }
        });
    }
}
