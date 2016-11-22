package com.torv.adam.aplayer.videolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.torv.adam.aplayer.R;

public class VideoListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
