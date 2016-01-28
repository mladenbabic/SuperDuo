package barqsoft.footballscores.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Vector;

import barqsoft.footballscores.R;
import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.event.OnRefreshEndEvent;
import barqsoft.footballscores.http.CallResponse;
import barqsoft.footballscores.http.HttpUtil;
import barqsoft.footballscores.model.Fixtures;
import barqsoft.footballscores.model.FixturesResult;
import barqsoft.footballscores.model.Href;
import barqsoft.footballscores.model.Season;
import barqsoft.footballscores.model.Team;
import barqsoft.footballscores.model.TeamResult;
import de.greenrobot.event.EventBus;
import retrofit.Call;

public class FootballScoresSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String TAG = FootballScoresSyncAdapter.class.getSimpleName();
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    public static final int SYNC_INTERVAL = 60 * 1; //TEST
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private String apiKey;
    private Map<String, Team> mTeamMap = new HashMap<>();
    private static final int SEASON_MIN = 394;
    private static final int SEASON_MAX = 425;

    public FootballScoresSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        apiKey = context.getString(R.string.api_key);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "Starting sync");
        mTeamMap.clear();
        retrieveTeams();
        retrieveFixtures("n2");
        retrieveFixtures("p2");

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new OnRefreshEndEvent());
            }
        });
    }

    private void retrieveFixtures(String timeFrame) {
        FixturesResult fixturesResult = new CallResponse<FixturesResult>().execute(HttpUtil.getService().getFixtures(apiKey, timeFrame));
        if (fixturesResult != null) {
            List<Fixtures> fixtures = fixturesResult.getFixtures();
            processFixtures(fixtures, true);
        } else if (fixturesResult == null || fixturesResult.getFixtures().isEmpty()) {
            String dummyFixtures = getContext().getString(R.string.dummy_data);
            fixturesResult = new Gson().fromJson(dummyFixtures, FixturesResult.class);
            processFixtures(fixturesResult.getFixtures(), false);
        }
    }

    private void processFixtures(List<Fixtures> fixturesList, boolean isReal) {

        String mSeasonId = null;
        String mDate = null;
        String mTime = null;
        String mHome = null;
        String mAway = null;
        String mMatchId = null;
        String teamHomeId = null;
        String teamAwayId = null;

        Vector<ContentValues> values = new Vector<ContentValues>(fixturesList.size());
        int i = 0;
        for (Fixtures fixtures : fixturesList) {

            mMatchId = extractId(fixtures.getLinks().getSelf());
            mSeasonId = extractId(fixtures.getLinks().getSoccerseason());
            Log.d(TAG, "processFixtures: storing fixture: " + mMatchId + " for seasion " + mSeasonId);

            if (checkSeasonRange(mSeasonId)) {

                mDate = fixtures.getDate();
                mTime = mDate.substring(mDate.indexOf("T") + 1, mDate.indexOf("Z"));
                mDate = mDate.substring(0, mDate.indexOf("T"));
                SimpleDateFormat match_date = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                match_date.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {

                    Date parseddate = match_date.parse(mDate + mTime);
                    SimpleDateFormat new_date = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
                    new_date.setTimeZone(TimeZone.getDefault());
                    mDate = new_date.format(parseddate);
                    mTime = mDate.substring(mDate.indexOf(":") + 1);
                    mDate = mDate.substring(0, mDate.indexOf(":"));

                    if (!isReal) {
                        //This if statement changes the dummy data's date to match our current date range.
                        Date fragmentdate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
                        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                        mDate = mformat.format(fragmentdate);
                        mMatchId = mMatchId + Integer.toString(i);
                    }

                    teamHomeId = extractId(fixtures.getLinks().getHomeTeam());
                    teamAwayId = extractId(fixtures.getLinks().getAwayTeam());

                    Team homeTeam = mTeamMap.get(teamHomeId);
                    Team awayTeam = mTeamMap.get(teamAwayId);

                    mHome = getTeamName(homeTeam);
                    mAway = getTeamName(awayTeam);

                    ContentValues match_values = new ContentValues();
                    match_values.put(DatabaseContract.scores_table.MATCH_ID, mMatchId);
                    match_values.put(DatabaseContract.scores_table.DATE_COL, mDate);
                    match_values.put(DatabaseContract.scores_table.TIME_COL, mTime);
                    match_values.put(DatabaseContract.scores_table.HOME_COL, mHome);
                    match_values.put(DatabaseContract.scores_table.AWAY_COL, mAway);
                    match_values.put(DatabaseContract.scores_table.HOME_GOALS_COL, fixtures.getResult().getGoalsHomeTeam());
                    match_values.put(DatabaseContract.scores_table.AWAY_GOALS_COL, fixtures.getResult().getGoalsAwayTeam());
                    match_values.put(DatabaseContract.scores_table.HOME_URL_COL, homeTeam.getCrestUrl());
                    match_values.put(DatabaseContract.scores_table.AWAY_URL_COL, awayTeam.getCrestUrl());
                    match_values.put(DatabaseContract.scores_table.LEAGUE_COL, mSeasonId);
                    match_values.put(DatabaseContract.scores_table.MATCH_DAY, fixtures.getMatchday());
                    match_values.put(DatabaseContract.scores_table.MATCH_STATUS, fixtures.getStatus());
                    values.add(match_values);

                } catch (Exception e) {
                    Log.d(TAG, "error here! " + teamHomeId + " " + teamAwayId);
                    Log.e(TAG, e.getMessage());
                }
            }
            i++;
        }

        int inserted_data = 0;
        ContentValues[] insert_data = new ContentValues[values.size()];
        values.toArray(insert_data);
        inserted_data = getContext().getContentResolver().bulkInsert(DatabaseContract.BASE_CONTENT_URI, insert_data);
        Log.v(TAG, "Successfully inserted : " + String.valueOf(inserted_data));
    }

    private String getTeamName(Team team) {
        String name = "";
        name = team.getCode();
        if (TextUtils.isEmpty(name)) {
            name = team.getShortName();
        }
        if (TextUtils.isEmpty(name)) {
            name = team.getName();
        }
        return name;
    }


    private boolean checkSeasonRange(String mSeasonId) {
        return Integer.valueOf(mSeasonId) >= SEASON_MIN || Integer.valueOf(mSeasonId) <= SEASON_MAX;
    }

    private void retrieveTeams() {
        Call<List<Season>> seasonResultCall = HttpUtil.getService().getSoccerSeasons(apiKey);
        List<Season> seasonResult = new CallResponse<List<Season>>().execute(seasonResultCall);

        if (seasonResult != null) {
            for (Season season : seasonResult) {
                String seasonId = extractId(season.getLinks().getSelf());
                if (checkSeasonRange(seasonId)) {
                    Log.d(TAG, "retrieveTeams: found season " + seasonId);
                    TeamResult teamResult = new CallResponse<TeamResult>().execute(HttpUtil.getService().getTeams(apiKey, seasonId));
                    if (teamResult != null) {
                        for (Team team : teamResult.getTeams()) {
                            String teamId = extractId(team.getLinks().getSelf());
                            mTeamMap.put(teamId, team);
                        }
                    } else {
                        Log.d(TAG, "retrieveTeams: no teams for season " + seasonId);
                    }
                }
            }
        }
    }

    private String extractId(Href self) {
        String[] splitArray = self.getHref().split("/(?=[^/]*$)");
        if (splitArray != null) {
            return splitArray[1];
        }
        return "";
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        FootballScoresSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}