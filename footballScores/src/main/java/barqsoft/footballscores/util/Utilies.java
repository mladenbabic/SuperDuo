package barqsoft.footballscores.util;

import android.text.TextUtils;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies {

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


    public static String getLeague(int league_num) {
        switch (league_num) {
            case SERIE_A:
                return "Seria A";
            case PREMIER_LEAGUE:
                return "Premier League";
            case CHAMPIONS_LEAGUE:
                return "UEFA Champions League";
            case PRIMERA_DIVISION:
                return "Primera Division";
            case SEGUNDA_DIVISION:
                return "Segunda Division";
            case BUNDESLIGA:
                return "Bundesliga";
            case BUNDESLIGA2:
                return "Bundesliga 2";
            case BUNDESLIGA3:
                return "Bundesliga 3";
            case LIGUE_1:
                return "LIGUE 1";
            case LIGUE_2:
                return "LIGUE 2";
            case PREMIERA_LIGA:
                return "Premiera Liga";
            case EREDIVISIE:
                return "Eredivisie";
            case EURO_CHAMPIONSHIPS_FRANCE:
                return "Euro Championships";
            case LEAGUE_ONE:
                return "League One";
            default:
                return "Not known League: " + league_num;
        }
    }

    public static String getMatchDay(int match_day, int league_num) {
        if (league_num == CHAMPIONS_LEAGUE) {
            if (match_day <= 6) {
                return "Group Stages, Matchday : 6";
            } else if (match_day == 7 || match_day == 8) {
                return "First Knockout round";
            } else if (match_day == 9 || match_day == 10) {
                return "QuarterFinal";
            } else if (match_day == 11 || match_day == 12) {
                return "SemiFinal";
            } else {
                return "Final";
            }
        } else {
            return "Matchday : " + String.valueOf(match_day);
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


}
