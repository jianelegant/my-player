package com.torv.adam.aplayer.mediacodec;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by admin on 16/11/15.
 */

public class PlayerThread extends Thread{

    private static final String PATH = Environment.getExternalStorageDirectory() + "/Download/test.mp4";
    private static final String TAG = PlayerThread.class.getSimpleName();

    private MediaExtractor extractor;
    private MediaCodec decoder;
    private Surface surface;

    public PlayerThread(Surface surface) {
        this.surface = surface;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void run() {
        extractor = new MediaExtractor();
        try {
            extractor.setDataSource(PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0;i<extractor.getTrackCount();i++){
            MediaFormat mediaFormat = extractor.getTrackFormat(i);
            String mine = mediaFormat.getString(MediaFormat.KEY_MIME);
            if(mine.startsWith("video/")){
                extractor.selectTrack(i);
                try {
                    decoder = MediaCodec.createDecoderByType(mine);
                    decoder.configure(mediaFormat, surface, null, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        if(null == decoder){
            Log.e(TAG, "can not find video info");
            return;
        }

        decoder.start();

        ByteBuffer[] inputBuffers = decoder.getInputBuffers();
        ByteBuffer[] outputBuffers = decoder.getOutputBuffers();
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

        boolean isEOF = false;
        long startMs = System.currentTimeMillis();

        while (!Thread.interrupted()) {
            if(!isEOF) {
                int inIndex = decoder.dequeueInputBuffer(10000);
                if(inIndex >= 0) {
                    ByteBuffer byteBuffer = inputBuffers[inIndex];
                    int sampleSize = extractor.readSampleData(byteBuffer, 0);
                    if(sampleSize < 0) {
                        Log.d(TAG, "InputBuffer, BUFFER_FLAG_END_OF_STREAM");
                        decoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        isEOF = true;
                    } else {
                        decoder.queueInputBuffer(inIndex, 0, sampleSize, extractor.getSampleTime(), 0);
                        extractor.advance();
                    }
                }
            }

            int outIndex = decoder.dequeueOutputBuffer(bufferInfo, 10000);
            switch (outIndex) {
                case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                    Log.d("DecodeActivity", "INFO_OUTPUT_BUFFERS_CHANGED");
                    outputBuffers = decoder.getOutputBuffers();
                    break;
                case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                    Log.d("DecodeActivity", "New format " + decoder.getOutputFormat());
                    break;
                case MediaCodec.INFO_TRY_AGAIN_LATER:
                    Log.d("DecodeActivity", "dequeueOutputBuffer timed out!");
                    break;
                default:
                    ByteBuffer byteBuffer = outputBuffers[outIndex];
                    while (bufferInfo.presentationTimeUs / 1000 > System.currentTimeMillis() - startMs) {
                        try {
                            sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    decoder.releaseOutputBuffer(outIndex, true);
                    break;
            }

            // All decoded frames have been rendered, we can stop playing now
            if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                Log.d("DecodeActivity", "OutputBuffer BUFFER_FLAG_END_OF_STREAM");
                break;
            }
        }

        decoder.stop();
        decoder.release();
        extractor.release();
    }
}
