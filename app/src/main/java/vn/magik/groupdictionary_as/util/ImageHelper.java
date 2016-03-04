package vn.magik.groupdictionary_as.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

import vn.magik.groupdictionary_as.R;
import vn.magik.groupdictionary_as.activity.MainActivity;
import vn.magik.groupdictionary_as.download.FileUtils;

import static vn.magik.groupdictionary_as.download.FileUtils.isValid;

public class ImageHelper {

    ImageLoader imageLoader ;
    DisplayImageOptions options;
    private static ImageHelper mInstance = null;

    public static ImageHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ImageHelper(context);
        }
        return mInstance;
    }

    public ImageHelper(Context context) {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .resetViewBeforeLoading(true)
                .build();
    }



    public void setImage(ImageView imageView, String image) {
        String imageUri = "file://" + GlobalParams.APP_FOLDER_DATA + image.trim();
        Log.i("imageUri", imageUri);

        imageLoader.displayImage(imageUri, imageView, options);
//        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
//        imageLoader.displayImage(imageUri,imageView);
//        imageLoader.displayImage(
//                imageUri, imageView, options, new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
//                        super.onLoadingStarted(imageUri, view);
////                        spinner.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                        String message = null;
//                        switch (failReason.getType()) {
//                            case IO_ERROR:
//                                message = "Input/Output error";
//                                break;
//                            case DECODING_ERROR:
//                                message = "Image can't be decoded";
//                                break;
//                            case NETWORK_DENIED:
//                                message = "Downloads are denied";
//                                break;
//                            case OUT_OF_MEMORY:
//                                message = "Out Of Memory error";
//                                break;
//                            case UNKNOWN:
//                                message = "Unknown error";
//                                break;
//                        }
//                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
//
////                        spinner.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
////                        spinner.setVisibility(View.GONE);
//                    }
//                });

    }

}
