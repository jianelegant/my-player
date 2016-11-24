package com.torv.adam.aplayer.videolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.torv.adam.aplayer.R;
import com.torv.adam.libs.utils.Constant;

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
        TextView titleView = (TextView) findViewById(R.id.id_title);
        Intent intent = getIntent();
        if(null != intent) {
            String path = intent.getStringExtra(Constant.VIDEO_PATH_KEY);
            if(!TextUtils.isEmpty(path)) {
                titleView.setText(path);
            }
        }
    }
}
