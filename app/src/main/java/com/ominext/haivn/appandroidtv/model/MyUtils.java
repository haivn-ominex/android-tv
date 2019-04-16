package com.ominext.haivn.appandroidtv.model;

public class MyUtils {

    public static boolean isPhoto(String title) {
        if (title.toLowerCase().contains(".png") || title.toLowerCase().contains(".jpg") ||
                title.toLowerCase().contains(".gif") || title.toLowerCase().contains(".tiff") ||
                title.toLowerCase().contains(".bmp") || title.toLowerCase().contains(".jpeg")) {
            return true;
        }
        return false;
    }

    public static boolean isVideo(String title) {
        if (title.toLowerCase().contains(".avi") || title.toLowerCase().contains(".flv") ||
                title.toLowerCase().contains(".wmv") || title.toLowerCase().contains(".mov") ||
                title.toLowerCase().contains(".mp4") || title.toLowerCase().contains(".3gp") ||
                title.toLowerCase().contains(".mkv")) {
            return true;
        }
        return false;
    }


}
