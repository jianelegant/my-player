package com.torv.adam.aplayer;

import android.app.Application;

import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAgentListener;
import com.torv.adam.libs.utils.AsyncJobMgr;
import com.torv.adam.libs.utils.Constant;
import com.torv.adam.libs.utils.Font;
import com.torv.adam.libs.utils.L;
import com.torv.adam.libs.utils.PermissionUtil;
import com.torv.adam.player.PlayerActivity;

/**
 * Created by AdamLi on 2016/11/18.
 */

public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        doSomeInit();
    }

    private void doSomeInit() {
        // Log
        L.setIsDebug(BuildConfig.DEBUG);
        // Job Manager = Thread pool
        AsyncJobMgr.instance.init(this);
        // Permission tools
        PermissionUtil.setAppContext(this);
        // ijkPlayer and ffmpeg libs, should after the job manager
        PlayerActivity.loadLibrariesOnce();
        // load font, should after the job manager
        Font.instance.loadFontsAsync(this);
        // flurry
        initFlurry();
    }

    private void initFlurry() {
        new FlurryAgent.Builder()
                .withLogEnabled(BuildConfig.DEBUG)
                .withListener(new FlurryAgentListener() {
                    @Override
                    public void onSessionStarted() {
                        L.d("session start");
                    }
                })
                .build(this, Constant.FLURRY_API_KEY);
    }
}
