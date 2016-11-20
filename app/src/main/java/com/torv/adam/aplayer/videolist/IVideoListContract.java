package com.torv.adam.aplayer.videolist;

import com.torv.adam.aplayer.bean.VideoItem;

import java.util.List;

/**
 * Created by admin on 16/11/20.
 */

public class IVideoListContract {

    public interface IPresenter{

        public void start(String path);
    }

    public interface IView{

        public void onData(List<VideoItem> videoList);
    }
}
