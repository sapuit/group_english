package vn.magik.groupdictionary_as.download;


import android.os.Environment;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;

import vn.magik.groupdictionary_as.util.GlobalParams;

/**
 * Created by Edgar on 16-Dec-15.
 */
public class FileUtils {

    public static String getPath(String fileName) {
        return GlobalParams.APP_FOLDER_DATA + fileName;
    }

    public static boolean isValid(String fileName) {
        return new File(GlobalParams.APP_FOLDER_DATA + fileName).exists();
    }

    public static boolean deleteFile(String fileName) {
        return new File(GlobalParams.APP_FOLDER_DATA + fileName).delete();
    }

    public static void makeAppFolderIfNotExist() {

        File appFolder = new File(GlobalParams.APP_FOLDER_PARENT);
        if (!appFolder.exists())
            appFolder.mkdir();
        File appFolderChild = new File(GlobalParams.APP_FOLDER_DATA);
        if (!appFolderChild.exists())
            appFolderChild.mkdir();

    }

    public static void unzip(String fileName, String directory) throws ZipException {
        ZipFile zip = new ZipFile(GlobalParams.APP_FOLDER_DATA + fileName);
        zip.extractAll(GlobalParams.APP_FOLDER_PARENT + directory);
    }


}
