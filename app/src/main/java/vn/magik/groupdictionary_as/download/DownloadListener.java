package vn.magik.groupdictionary_as.download;

/**
 * Created by Edgar on 16-Dec-15.
 */
public interface DownloadListener {

    public void onStart(int minValue, int maxValue);

    public void onSuccess(String fileName, String directory);

    public void onQueue(int progress);

    public void onFail();

    public void onFinish();

    void onDownloadCancel();
}
