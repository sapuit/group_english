package vn.magik.groupdictionary_as.download;

//import com.edgar.ecard.utils.UrlUtils;

import android.util.Log;

/**
 * Created by Edgar on 17-Dec-15.
 */
public class FilePath {
    private String linkDownloadFile;        //  http://192.168.0.100/groupdictionary/data/icon.zip
    private String linkLocalFile;           //  /mnt/sdcard/groupdictionary/data/icon.zip
    private String linkLocalDirectory;      //  /mnt/sdcard/groupdictionary/data
    private String directory;               //  data
    private String fileName;                //  icon.zip

    public FilePath(String linkDownloadFile) {
        this.linkDownloadFile =linkDownloadFile;

        this.fileName = UrlUtils.getFileName(this.linkDownloadFile);
        this.linkLocalFile = UrlUtils.getLinkLocalFile(this.linkDownloadFile);
        this.linkLocalDirectory = UrlUtils.getLinkLocalDirectory(this.linkDownloadFile);
        this.directory = UrlUtils.getDirectory(this.linkDownloadFile);
        Log.i("fileName",fileName);
        Log.i("linkLocalFile",linkLocalFile);
        Log.i("linkLocalDirectory",linkLocalDirectory);
        Log.i("directory",directory);
    }

    public String getLinkDownloadFile() {
        return linkDownloadFile;
    }

    public void setLinkDownloadFile(String linkDownloadFile) {
        this.linkDownloadFile = linkDownloadFile;
    }

    public String getLinkLocalFile() {
        return linkLocalFile;
    }

    public void setLinkLocalFile(String linkLocalFile) {
        this.linkLocalFile = linkLocalFile;
    }

    public String getLinkLocalDirectory() {
        return linkLocalDirectory;
    }

    public void setLinkLocalDirectory(String linkLocalDirectory) {
        this.linkLocalDirectory = linkLocalDirectory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
