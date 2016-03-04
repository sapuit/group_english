package vn.magik.groupdictionary_as.download;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class FileDownloadAsync extends AsyncTask<Void, Integer, String> {

    private DownloadListener hander;
    private FilePath fileInfo;
    private ProgressDialog progressDialog;
    private Context context;
    private boolean isShowProgressDialog;
    private boolean isStop;

    public FileDownloadAsync(final Context context, String linkDownloadFile, boolean showDownload, final DownloadListener hander) {
        this.context = context;
        fileInfo = new FilePath(linkDownloadFile);
        this.hander = hander;
        isStop = false;

        //  setting dialog
        isShowProgressDialog = showDownload;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please waiting download for using offline :)");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isStop = true;
                FileDownloadAsync.this.cancel(true);
                hander.onDownloadCancel();
            }
        });
    }


    public void showDialogProgress() {
        isShowProgressDialog = true;
    }

    public void download(String link) throws Exception {
        FileUtils.makeAppFolderIfNotExist();
        byte data[] = new byte[1024];
        long total = 0;
        int count;

        URL url = new URL(link);
        URLConnection conection = url.openConnection();
        conection.connect();

        int lenghtOfFile = conection.getContentLength();
        InputStream input = new BufferedInputStream(url.openStream(), 10 * 1024);

        if (FileUtils.isValid(fileInfo.getFileName())) {
            FileUtils.deleteFile(fileInfo.getFileName());
        }
        // Output stream to write file in SD card
        OutputStream output = new FileOutputStream(fileInfo.getLinkLocalFile());

        while ((count = input.read(data)) != -1) {
            total += count;
            publishProgress((int) ((total * 100) / lenghtOfFile));
            output.write(data, 0, count);
            if (isStop) {
                output.flush();
                output.close();
                input.close();
                throw new Exception("Terminal download");
            }
        }

        output.flush();
        output.close();
        input.close();

        FileUtils.unzip(fileInfo.getFileName(), fileInfo.getDirectory());
        FileUtils.deleteFile(fileInfo.getFileName());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isShowProgressDialog) {
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();
        }
        hander.onStart(0, 100);
    }


    @Override
    protected String doInBackground(Void... no) {
        int count;
        try {
            download(fileInfo.getLinkDownloadFile());
            return fileInfo.getLinkLocalDirectory();
        } catch (Exception e) {
            FileUtils.deleteFile(fileInfo.getFileName());
        }
        return "0";
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        hander.onQueue(progress[0]);
        if (isShowProgressDialog)
            progressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String directory) {
        if (isShowProgressDialog)
            progressDialog.dismiss();

        if (!directory.equals("0")) {
            hander.onSuccess(fileInfo.getLinkLocalFile(), directory);
            Toast.makeText(context, "Download successful !", Toast.LENGTH_SHORT).show();
        } else {
            hander.onFail();
            Toast.makeText(context, "Download Fail !", Toast.LENGTH_SHORT).show();
        }

        hander.onFinish();
    }

}
