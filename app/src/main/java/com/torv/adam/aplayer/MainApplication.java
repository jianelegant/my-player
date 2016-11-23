package com.torv.adam.aplayer;

import android.app.Application;

import com.torv.adam.libs.utils.AsyncJobMgr;
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
        L.setIsDebug(BuildConfig.DEBUG);
        AsyncJobMgr.instance.init(this);

        PermissionUtil.setAppContext(this);

        PlayerActivity.loadLibrariesOnce();
        Font.instance.loadFontsAsync(this);
    }
}
