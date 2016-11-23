package com.torv.adam.libs.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 16/11/17.
 */

public class Util {

    /**
     * Convert Byte to display format K / M / G
     * @param size
     * @return
     */
    public static String convertByte2KMG(long size) {
        if(size <= 0) {
            return "0";
        } else if(size < 1048576) {
            // < 1M
            return size/1024 + "K";
        } else if(size < 1073741824) {
            // < 1G
            return size/1024/1024 + "M";
        } else {
            DecimalFormat df = new DecimalFormat("#.00");
            double value = ((double)size)/1024/1024/1024;
            return df.format(value) + "G";
        }
    }
    /**
     * Convert ms to 00:00:00 format
     * @param millis
     * @return
     */
    public static String convertMs2HMS(long millis){
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
    /**
     * path = pathAndName - name
     * @param pathAndName
     * @param name
     * @return
     */
    public static String getVideoPath(String pathAndName, String name){
        String path = null;
        if(!TextUtils.isEmpty(pathAndName) && !TextUtils.isEmpty(name)){
            path = pathAndName.substring(0, pathAndName.indexOf(name));
        }
        return path;
    }
    /**
     * Check the file is video file or not
     * @param fileName with extension name
     * @return
     */
    public static boolean isVideoFile(String fileName){
        boolean isVideo = false;
        if(null != fileName) {
            String[] ext = fileName.split("\\.");
            if(null != ext) {
                int length = ext.length;
                if(length > 0) {
                    String extName = "." + ext[length - 1];
                    if(null != extName) {
                        if(extName.equalsIgnoreCase(".mp4")
                                || extName.equalsIgnoreCase(".3gp")
                                || extName.equalsIgnoreCase(".wmv")
                                || extName.equalsIgnoreCase(".ts")
                                || extName.equalsIgnoreCase(".rmvb")
                                || extName.equalsIgnoreCase(".mov")
                                || extName.equalsIgnoreCase(".m4v")
                                || extName.equalsIgnoreCase(".avi")
                                || extName.equalsIgnoreCase(".m3u8")
                                || extName.equalsIgnoreCase(".3gpp")
                                || extName.equalsIgnoreCase(".3gpp2")
                                || extName.equalsIgnoreCase(".mkv")
                                || extName.equalsIgnoreCase(".flv")
                                || extName.equalsIgnoreCase(".divx")
                                || extName.equalsIgnoreCase(".f4v")
                                || extName.equalsIgnoreCase(".rm")
                                || extName.equalsIgnoreCase(".asf")
                                || extName.equalsIgnoreCase(".ram")
                                || extName.equalsIgnoreCase(".mpg")
                                || extName.equalsIgnoreCase(".v8")
                                || extName.equalsIgnoreCase(".swf")
                                || extName.equalsIgnoreCase(".m2v")
                                || extName.equalsIgnoreCase(".asx")
                                || extName.equalsIgnoreCase(".ra")
                                || extName.equalsIgnoreCase(".ndivx")
                                || extName.equalsIgnoreCase(".xvid")) {
                            isVideo = true;
                        }
                    }
                }
            }

        }
        return isVideo;
    }
}
