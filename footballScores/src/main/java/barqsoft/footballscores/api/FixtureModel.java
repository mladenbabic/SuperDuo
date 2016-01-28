package barqsoft.footballscores.api;

import android.database.Cursor;

import barqsoft.footballscores.db.DatabaseContract;

public class FixtureModel {

    private String homeName;
    private String awayHome;
    private String date;
    private String matchStatus;
    private int scoreHome;
    private int scoreAway;
    private String crestHomeUrl;
    private String crestAwayUrl;
    private double matchScoreId;
    private int matchday;
    private int sessionId;

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAwayHome() {
        return awayHome;
    }

    public void setAwayHome(String awayHome) {
        this.awayHome = awayHome;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScoreHome() {
        return scoreHome;
    }

    public void setScoreHome(int scoreHome) {
        this.scoreHome = scoreHome;
    }

    public String getCrestHomeUrl() {
        return crestHomeUrl;
    }

    public void setCrestHomeUrl(String crestHomeUrl) {
        this.crestHomeUrl = crestHomeUrl;
    }

    public String getCrestAwayUrl() {
        return crestAwayUrl;
    }

    public void setCrestAwayUrl(String crestAwayUrl) {
        this.crestAwayUrl = crestAwayUrl;
    }

    public double getMatchScoreId() {
        return matchScoreId;
    }

    public void setMatchScoreId(double matchScoreId) {
        this.matchScoreId = matchScoreId;
    }

    public FixtureModel fromCursor(Cursor cursor) {
        setHomeName( cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.HOME_COL)));
        setAwayHome(cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_COL)));
        setDate(cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.DATE_COL)));
        setScoreHome(cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL)));
        setScoreAway(cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL)));
        setCrestHomeUrl(cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.HOME_URL_COL)));
        setCrestAwayUrl(cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_URL_COL)));
        setMatchScoreId(cursor.getDouble(cursor.getColumnIndex(DatabaseContract.scores_table.MATCH_ID)));
        setMatchday(cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.MATCH_DAY)));
        setSessionId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.LEAGUE_COL)));
        setMatchStatus(cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.MATCH_STATUS)));
        return this;
    }

    public int getScoreAway() {
        return scoreAway;
    }

    public void setScoreAway(int scoreAway) {
        this.scoreAway = scoreAway;
    }

    public int getMatchday() {
        return matchday;
    }

    public void setMatchday(int matchday) {
        this.matchday = matchday;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }
}