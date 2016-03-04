package vn.magik.groupdictionary_as.download;

import android.os.Environment;

import vn.magik.groupdictionary_as.util.GlobalParams;

/**
 * Created by sapui on 1/20/2016.
 */
public class UrlUtils {


    public static String getFileName(String linkDownloadFile) {
        String[] strArray = linkDownloadFile.split("/");
        int lastWord = strArray.length - 1;
        return strArray[lastWord];
    }

    public static String getDirectory(String linkDownloadFile) {
        String[] strArray = linkDownloadFile.split("/");
        int lastWord = strArray.length - 2;
        return strArray[lastWord];
    }

    public static String getLinkLocalDirectory(String linkDownloadFile) {
        return Environment.getExternalStorageDirectory().getPath() +
                "/" + GlobalParams.ROOT_FOLDER +
                "/" + getDirectory(linkDownloadFile) + "/";
    }

    public static String getLinkLocalFile(String linkDownloadFile) {
        return getLinkLocalDirectory(linkDownloadFile) + getFileName(linkDownloadFile);
    }
}
