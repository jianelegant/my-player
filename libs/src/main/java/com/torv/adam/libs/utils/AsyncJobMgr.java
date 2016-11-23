package com.torv.adam.libs.utils;

import android.content.Context;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;

/**
 * Created by AdamLi on 2016/11/18.
 */

public enum AsyncJobMgr {
    instance;

    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_MID = 2;
    public static final int PRIORITY_HIGH = 3;

    private JobManager mJobManager;

    public synchronized void init(Context context){
        if(null == mJobManager) {
            Configuration configuration = new Configuration.Builder(context)
                    .build();
            mJobManager = new JobManager(configuration);
        }
    }

    /**
     * @param job
     */
    public void addAsyncJob(Job job) {
        mJobManager.addJobInBackground(job);
    }
}
