package com.tencent.wxcloudrun.utils;

public class FileUtil {

    /**
     * 截取文件名称，如果文件名里面包含了路径，则只保留文件名
     * @param fileName
     * @return
     */
    public static String subFilename(String fileName){
        if (fileName == null || fileName.isEmpty()){
            return fileName;
        }
        String newFilename = fileName;
        if (fileName.indexOf('/') > 0){
            newFilename = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.length());
        }
        return newFilename;
    }
}
