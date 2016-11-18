package com.torv.adam.aplayer;

import android.app.Application;

import com.torv.adam.aplayer.utils.AsyncJobMgr;
import com.torv.adam.aplayer.utils.Font;
import com.torv.adam.aplayer.utils.PermissionUtil;

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
        AsyncJobMgr.instance.init(this);

        PermissionUtil.setAppContext(this);

        Font.instance.loadFontsAsync(this);
    }
}
