package barqsoft.footballscores.util;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.Time;

import java.text.SimpleDateFormat;

import barqsoft.footballscores.R;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities {

    public static final int BUNDESLIGA = 394; // "1. Bundesliga 2015/16"
    public static final int BUNDESLIGA2 = 395; // "2. Bundesliga 2015/16"
    public static final int BUNDESLIGA3 = 403; // "3. Bundesliga 2015/16"
    public static final int LIGUE_1 = 396; // "Ligue 1 2015/16"
    public static final int LIGUE_2 = 397; // "Ligue 2 2015/16"
    public static final int PREMIER_LEAGUE = 398; // "Premier League 2015/16"
    public static final int PRIMERA_DIVISION = 399; // "Primera Division 2015/16"
    public static final int SEGUNDA_DIVISION = 400; // "Segunda Division 2015/16"
    public static final int SERIE_A = 401; // "Serie A 2015/16"
    public static final int PREMIERA_LIGA = 402; // "Primeira Liga 2015/16"
    public static final int EREDIVISIE = 404; // "Eredivisie 2015/16"
    public static final int CHAMPIONS_LEAGUE = 405; // "Champions League 2015/16"
    public static final int EURO_CHAMPIONSHIPS_FRANCE = 424; // "European Championships France 2016"
    public static final int LEAGUE_ONE = 425; // "League One 2015/16"

    public static String getLeague(Context mContext, int league_num) {
        switch (league_num) {
            case SERIE_A:
                return mContext.getString(R.string.seriaa);
            case PREMIER_LEAGUE:
                return mContext.getString(R.string.premierleague);
            case CHAMPIONS_LEAGUE:
                return mContext.getString(R.string.champions_league);
            case PRIMERA_DIVISION:
                return mContext.getString(R.string.primeradivison);
            case SEGUNDA_DIVISION:
                return mContext.getString(R.string.segundadivison);
            case BUNDESLIGA:
                return mContext.getString(R.string.bundesliga);
            case BUNDESLIGA2:
                return mContext.getString(R.string.bundesliga_2);
            case BUNDESLIGA3:
                return mContext.getString(R.string.bundesliga_3);
            case LIGUE_1:
                return mContext.getString(R.string.ligue_1);
            case LIGUE_2:
                return mContext.getString(R.string.ligue_2);
            case PREMIERA_LIGA:
                return mContext.getString(R.string.primeradivison);
            case EREDIVISIE:
                return mContext.getString(R.string.eredivisie);
            case EURO_CHAMPIONSHIPS_FRANCE:
                return mContext.getString(R.string.euro_championships_france);
            case LEAGUE_ONE:
                return mContext.getString(R.string.league_1);
            default:
                return mContext.getString(R.string.not_known_league);
        }
    }

    public static String getMatchDay(Context mContext, int match_day, int league_num) {
        if (league_num == CHAMPIONS_LEAGUE) {
            if (match_day <= 6) {
                return mContext.getString(R.string.match_detail_ch_group_stage);
            } else if (match_day == 7 || match_day == 8) {
                return mContext.getString(R.string.match_detail_ch_first_knockout);
            } else if (match_day == 9 || match_day == 10) {
                return mContext.getString(R.string.match_detail_ch_quarterfinals);
            } else if (match_day == 11 || match_day == 12) {
                return mContext.getString(R.string.match_detail_ch_semifinals);
            } else {
                return mContext.getString(R.string.match_detail_ch_finals);
            }
        } else {
            return mContext.getString(R.string.match_detail_match_day) + " " + String.valueOf(match_day);
        }
    }

    public static String getTeamCrestURL(String crestTeamUrl) {
        if (!TextUtils.isEmpty(crestTeamUrl) && !crestTeamUrl.contains(".png")) {
            String IMAGE_URL_BASE = "http://upload.wikimedia.org/wikipedia/";
            String imageName = crestTeamUrl.substring(crestTeamUrl.lastIndexOf("/") + 1);
            String prefixURL = crestTeamUrl.substring(0, crestTeamUrl.indexOf("/", IMAGE_URL_BASE.length() + 1));
            String postFixURL = crestTeamUrl.substring(prefixURL.length());
            crestTeamUrl = prefixURL + "/thumb" + postFixURL + "/200px-" + imageName + ".png";
        }
        return crestTeamUrl;
    }

    public static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.match_detail_today);
        } else if (julianDay == currentJulianDay + 1) {
            return context.getString(R.string.match_detail_tomorrow);
        } else if (julianDay == currentJulianDay - 1) {
            return context.getString(R.string.match_detail_yesterday);
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }


}
