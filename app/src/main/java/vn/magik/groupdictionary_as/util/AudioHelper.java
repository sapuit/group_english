package vn.magik.groupdictionary_as.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import vn.magik.groupdictionary_as.activity.MainActivity;
import vn.magik.groupdictionary_as.download.FileUtils;

import static vn.magik.groupdictionary_as.download.FileUtils.isValid;

public class AudioHelper {

    private Context context;
    private static AudioHelper mInstance = null;

    public static AudioHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AudioHelper(context);
        }
        return mInstance;
    }

    public AudioHelper(Context context) {
        this.context = context;
    }

    public void play(String folder, String STREAM_URL) {
        MediaPlayer mPlayer = new MediaPlayer();
        try {
            String url = "";
            url = GlobalParams.APP_FOLDER_DATA + folder + "/" + STREAM_URL;
            Log.i("Audio", url);
            mPlayer.reset();
            mPlayer.setDataSource(context, Uri.parse(url));
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();

                }

                ;
            });

        } catch (IOException e) {
            mPlayer.reset();
            e.printStackTrace();
        }
    }

}
