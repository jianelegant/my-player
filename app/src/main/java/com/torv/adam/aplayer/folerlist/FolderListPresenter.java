package com.torv.adam.aplayer.folerlist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.torv.adam.aplayer.bean.FolderItem;
import com.torv.adam.aplayer.bean.VideoItem;
import com.torv.adam.aplayer.utils.L;
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

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DISPLAY_NAME };

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        if(null == cursor) {
            mView.onData(null);
        } else {
            List<FolderItem> mFolderList = new ArrayList<>();
            try {
                L.d("count : " + cursor.getCount());
                while(cursor.moveToNext()) {
                    VideoItem videoItem = new VideoItem();
                    videoItem.path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                    videoItem.fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                    videoItem.title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                    videoItem.mineType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
                    L.d(videoItem.path + "," + videoItem.fileName + "," + videoItem.title + "," +videoItem.mineType);
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if(null != cursor) {
                    cursor.close();
                }
                mView.onData(mFolderList);
            }
        }
    }
}
