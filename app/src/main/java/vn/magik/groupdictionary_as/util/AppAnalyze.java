package vn.magik.groupdictionary_as.util;

import android.app.Activity;
import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Tracker}.
 */
public class AppAnalyze extends Application {
    private Tracker mTracker;

    public AppAnalyze() {
        super();
    }
    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG

            analytics = GoogleAnalytics.getInstance(this);
            analytics.setLocalDispatchPeriod(1800);
            mTracker = analytics.newTracker(GlobalParams.CONFIG_RES_ID);
            mTracker.enableExceptionReporting(true);
            mTracker.enableAdvertisingIdCollection(true);
            mTracker.enableAutoActivityTracking(true);
        }
        return mTracker;
    }

//    public static void log(Activity activity) {
//        Tracker tracker = ((AppAnalyze) activity.getApplication()).getDefaultTracker();
//        tracker.setScreenName(activity.getPackageName());
//        tracker.send(new HitBuilders.ScreenViewBuilder().build());
//    }

}