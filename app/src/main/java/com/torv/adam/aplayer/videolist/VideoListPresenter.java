package com.torv.adam.aplayer.videolist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.torv.adam.aplayer.bean.VideoItem;
import com.torv.adam.libs.utils.L;
import com.torv.adam.libs.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 16/11/20.
 */

public class VideoListPresenter implements IVideoListContract.IPresenter{

    final IVideoListContract.IView mView;
    final Context mContext;

    public VideoListPresenter(Context context, IVideoListContract.IView view) {
        this.mView = view;
        this.mContext = context;
    }

    @Override
    public void start(String path) {
        scanVideoFiles(path);
    }

    private void scanVideoFiles(String path) {
        if(null == mView) {
            L.e("view is null");
            return;
        }
        if(null == mContext) {
            L.e("mContext is null");
            mView.onData(null);
            return;
        }

        Uri uri = MediaStore.Files.getContentUri("external");

        String[] projection = { MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        if(null == cursor) {
            L.e("cursor is null");
            mView.onData(null);
        } else {
            /** query all files and filter the vieo file */
            List<VideoItem> videoList = new ArrayList<>();
            try {
                L.d("count : " + cursor.getCount());
                while(cursor.moveToNext()) {
                    String pathAndName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                    String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                    String videoPath = Util.getVideoPath(pathAndName, fileName);
                    if(null != pathAndName && Util.isVideoFile(pathAndName) && null != path && path.equals(videoPath)) {
                        VideoItem videoItem = new VideoItem();
                        videoItem.path = pathAndName;
                        videoItem.fileName = fileName;
                        videoItem.bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                        L.d(videoItem.path + "," + videoItem.fileName);
                        videoList.add(videoItem);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if(null != cursor) {
                    cursor.close();
                }
            }

            /** return to view for display*/
            mView.onData(videoList);
        }
    }
}
