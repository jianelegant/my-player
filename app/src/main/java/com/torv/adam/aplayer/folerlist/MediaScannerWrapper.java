package com.torv.adam.aplayer.folerlist;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * Created by AdamLi on 2016/11/17.
 */

public class MediaScannerWrapper implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection mConnection;
    private String mPath;
    private String mMimeType;

    private IScanCompletedListener mListener;

    public interface IScanCompletedListener{
        public void onScanCompleted();
    }

    // filePath - where to scan;
    // mime type of media to scan i.e. "image/jpeg".
    // use "*/*" for any media
    public MediaScannerWrapper(Context ctx, String filePath, String mime, IScanCompletedListener listener){
        mPath = filePath;
        mMimeType = mime;
        mConnection = new MediaScannerConnection(ctx, this);
        mListener = listener;
    }

    // do the scanning
    public void scan() {
        mConnection.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mConnection.scanFile(mPath, mMimeType);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        if(null != mListener) {
            mListener.onScanCompleted();
        }
    }
}