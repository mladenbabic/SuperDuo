package barqsoft.footballscores.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.view.View;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Mladen on 8/14/2015.
 */
public class DeviceUtil {

    private static final int BRIGHTNESS_THRESHOLD = 130;

    public static int getDimensionInDp(Context context, @DimenRes int dimenResId) {
        return (int) (context.getResources().getDimension(dimenResId) /
                context.getResources().getDisplayMetrics().density);
    }

    public static boolean isDay() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        return timeOfDay >= 6 && timeOfDay < 20;
    }

    public static boolean isDay(long now) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now);
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        return timeOfDay >= 6 && timeOfDay < 20;
    }

    public static final boolean isColorDark(final int color) {
        return (30 * Color.red(color) + 59 * Color.green(color) + 11 * Color.blue(color)) / 100 <= BRIGHTNESS_THRESHOLD;
    }

    public static final boolean isTablet(final Context context) {
        final int layout = context.getResources().getConfiguration().screenLayout;
        return (layout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static final boolean isLandscape(final Context context) {
        final int orientation = context.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    public static boolean isOnline(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static boolean isRtl(Context context) {
        boolean rtl = false;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            Configuration config = context.getResources().getConfiguration();
            if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                rtl = true;
            }
        } else {

            Set<String> lang = new HashSet<String>();
            lang.add("ar");
            lang.add("dv");
            lang.add("fa");
            lang.add("ha");
            lang.add("he");
            lang.add("iw");
            lang.add("ji");
            lang.add("ps");
            lang.add("ur");
            lang.add("yi");
            Set<String> RTL = Collections.unmodifiableSet(lang);

            Locale locale = Locale.getDefault();

            rtl = RTL.contains(locale.getLanguage());
        }

        return rtl;
    }

}
