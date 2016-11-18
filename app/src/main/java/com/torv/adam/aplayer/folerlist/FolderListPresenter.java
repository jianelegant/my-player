package com.torv.adam.aplayer.folerlist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.torv.adam.aplayer.bean.FolderItem;
import com.torv.adam.aplayer.bean.VideoItem;
import com.torv.adam.aplayer.utils.L;
import com.torv.adam.aplayer.utils.Util;
import com.torv.adam.aplayer.vp.IContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdamLi on 2016/11/17.
 */

public class FolderListPresenter implements IContract.IPresenter{

    final IContract.IView mView;
    final Context mContext;

    public FolderListPresenter(Context context, IContract.IView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void start() {
        scanVideoFiles();
    }

    private void scanVideoFiles() {
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
                    String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                    if(Util.isVideoFile(fileName)) {
                        VideoItem videoItem = new VideoItem();
                        videoItem.path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
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

            /** list all folders, which contains videos, with video count*/
            List<FolderItem> folderlist = new ArrayList<>();
            boolean isExist;
            for(VideoItem videoItem : videoList) {
                isExist = false;
                String folderPath = videoItem.path.substring(0, videoItem.path.indexOf(videoItem.fileName));
                L.d("folder : " + folderPath);
                for(FolderItem folderItem : folderlist) {
                    if(folderItem.path.equalsIgnoreCase(folderPath)) {
                        isExist = true;
                        folderItem.videoCount ++;
                    }
                }

                if(!isExist) {
                    FolderItem folderItem = new FolderItem();
                    folderItem.path = folderPath;
                    folderItem.bucketDisplayName = videoItem.bucketDisplayName;
                    folderItem.videoCount = 1;
                    folderlist.add(folderItem);
                }
            }

            /** return to view for display*/
            mView.onData(folderlist);
        }
    }
}
