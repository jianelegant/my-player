package com.torv.adam.aplayer.videolist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.torv.adam.aplayer.R;
import com.torv.adam.aplayer.bean.VideoItem;
import com.torv.adam.libs.utils.Constant;
import com.torv.adam.libs.utils.Font;
import com.torv.adam.libs.utils.L;
import com.torv.adam.player.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 16/11/20.
 */

public class VideoListFragment extends Fragment implements IVideoListContract.IView{

    private IVideoListContract.IPresenter mPresenter;
    private String path;
    private RecyclerView mRecyclerView;

    private VideoListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d("Lifecycle");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        L.d("Lifecycle");
        View view = inflater.inflate(R.layout.fragment_videolist, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_video_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.d("Lifecycle");

        mPresenter = new VideoListPresenter(getContext(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new VideoListAdapter();
        mAdapter.setContext(getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        L.d("Lifecycle");
        path = getActivity().getIntent().getStringExtra(Constant.VIDEO_PATH_KEY);
        mPresenter.start(path);
    }

    @Override
    public void onResume() {
        super.onResume();
        L.d("Lifecycle");
    }

    @Override
    public void onPause() {
        super.onPause();
        L.d("Lifecycle");
    }

    @Override
    public void onStop() {
        super.onStop();
        L.d("Lifecycle");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.d("Lifecycle");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d("Lifecycle");
    }

    @Override
    public void onData(List<VideoItem> data) {
        L.d("onData");
        mAdapter.setData(data);
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder{

        TextView videoName;
        TextView videoDuration;

        public VideoViewHolder(View itemView) {
            super(itemView);
            videoName = (TextView) itemView.findViewById(R.id.id_video_name);
            videoDuration = (TextView) itemView.findViewById(R.id.id_video_duration);
        }
    }

    static class VideoListAdapter extends RecyclerView.Adapter<VideoViewHolder>{

        private Context mContext;
        private List<VideoItem> mVideoList = new ArrayList<>();

        public void setContext(Context context) {
            mContext = context;
        }

        public void setData(List<VideoItem> data) {
            if(null != data) {
                mVideoList.clear();
                mVideoList.addAll(data);
                notifyDataSetChanged();
            }
        }

        @Override
        public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            VideoViewHolder viewHolder = new VideoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.video_list_item, null));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(VideoViewHolder holder, int position) {
            VideoItem videoItem = mVideoList.get(position);
            if(null != videoItem) {

                holder.videoName.setText(videoItem.fileName);
                holder.videoName.setTypeface(Font.instance.getTypeFace(Font.ROBOTO_REGULAR));

                final String pathAndName = videoItem.path;
                final String name = videoItem.fileName;
                holder.videoName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PlayerActivity.jumpTo(mContext, pathAndName, name);
                    }
                });

//                holder.videoDuration.setText(videoItem.);
            }
        }

        @Override
        public int getItemCount() {
            return mVideoList.size();
        }
    }
}
