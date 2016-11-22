package com.torv.adam.player;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.Toast;

import com.torv.adam.player.media.AndroidMediaController;
import com.torv.adam.player.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerActivity extends AppCompatActivity {

    private static final String EXTRA_KEY_PATH = "extra_key_path";

    private IjkVideoView mVideoView;
    private TableLayout mHudView;

    private AndroidMediaController mMediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        IjkMediaPlayer.loadLibrariesOnce(null);
        initViews();

        initPlayer();
    }

    public static void jumpTo(Context context, String path){
        if(null == context || TextUtils.isEmpty(path)) {
            Toast.makeText(context, "Path is null", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(EXTRA_KEY_PATH, path);
        context.startActivity(intent);
    }

    private void initPlayer() {
        mMediaController = new AndroidMediaController(this, false);

        mVideoView.setMediaController(mMediaController);
        mVideoView.setHudView(mHudView);

        Intent intent = getIntent();
        String path = intent.getStringExtra(EXTRA_KEY_PATH);
        mVideoView.setVideoPath(path);
    }

    private void initViews() {
        mVideoView = (IjkVideoView) findViewById(R.id.id_videoview);
        mHudView = (TableLayout) findViewById(R.id.hud_view);

        Window window = getWindow();
        View decorView = window.getDecorView();
        window.setFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN, View
                .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
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
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Window window = getWindow();
        View decorView = window.getDecorView();
        if (Configuration.ORIENTATION_LANDSCAPE == newConfig.orientation) {
            window.setFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN, View
                    .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            window.clearFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }
}
