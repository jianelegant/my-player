package com.torv.adam.aplayer.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

/**
 * Created by AdamLi on 2016/11/18.
 */

public enum Font {
    instance;

    private SparseArray<Typeface> mTypefaceMap = new SparseArray<>();

    public static final int ROBOTO_LIGHT = 0;
    public static final int ROBOTO_REGULAR = 1;

    public void loadFontsAsync(final Context context){
        AsyncJobMgr.instance.addAsyncJob(new Job(new Params(1)) {
            @Override
            public void onAdded() {}

            @Override
            public void onRun() throws Throwable {
                L.d("load font job run");
                load(context);
            }

            @Override
            protected void onCancel(int cancelReason, @Nullable Throwable throwable) {}

            @Override
            protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
                return null;
            }
        });
    }

    private void load(Context context){
        mTypefaceMap.put(ROBOTO_LIGHT, Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));
        mTypefaceMap.put(ROBOTO_REGULAR, Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
    }

    public Typeface getTypeFace(int key){
        if(!isValidKey(key)){
            L.d("typeface for this key is not exist, key = " + key);
            return null;
        }
        return mTypefaceMap.get(key);
    }

    private boolean isValidKey(int key) {
        return ROBOTO_LIGHT == key
                || ROBOTO_REGULAR == key;
    }
}
