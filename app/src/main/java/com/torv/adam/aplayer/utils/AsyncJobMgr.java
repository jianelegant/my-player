package com.torv.adam.aplayer.utils;

import android.content.Context;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;

/**
 * Created by AdamLi on 2016/11/18.
 */

public enum AsyncJobMgr {
    instance;

    private JobManager mJobManager;

    public synchronized void init(Context context){
        if(null == mJobManager) {
            Configuration configuration = new Configuration.Builder(context)
                    .build();
            mJobManager = new JobManager(configuration);
        }
    }

    public void addAsyncJob(Job job) {
        mJobManager.addJobInBackground(job);
    }
}
