package barqsoft.footballscores.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import barqsoft.footballscores.R;
import barqsoft.footballscores.api.FixtureModel;
import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.util.Constants;
import barqsoft.footballscores.util.Status;
import barqsoft.footballscores.util.Utilities;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/29/2016.
 */
public class MatchScoresRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "MatchScoresRemoteVF";
    private Context mContext = null;
    private Cursor mCursor;

    public MatchScoresRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        if (mCursor != null) {
            mCursor.close();
        }

        //Reset the identity of the incoming IPC on the current thread.
        final long identityToken = Binder.clearCallingIdentity();

        Uri uri = DatabaseContract.scores_table.buildScoreWithDate();
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        String todayDate = format.format(new Date());

        mCursor = mContext.getContentResolver().query(uri, DatabaseContract.PROJECTION, null, new String[]{todayDate}, null);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        mCursor.moveToPosition(position);

        FixtureModel bookModel = new FixtureModel().fromCursor(mCursor);

        final RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.widget_item);

        Intent intent = new Intent();
        remoteView.setOnClickFillInIntent(R.id.widget_item_container, intent);

        remoteView.setTextViewText(R.id.widget_home_name, bookModel.getHomeName());
        remoteView.setTextViewText(R.id.widget_away_name, bookModel.getAwayHome());
        remoteView.setTextViewText(R.id.widget_data_textview, bookModel.getDate());

        if (bookModel.getScoreHome() != -1 && bookModel.getScoreAway() != -1) {
            remoteView.setTextViewText(R.id.widget_score_home_textview, "" + bookModel.getScoreHome());
            remoteView.setTextViewText(R.id.widget_score_away_textview, "" + bookModel.getScoreAway());

            if (Status.FINISHED.equals(bookModel.getMatchStatus())) {
                remoteView.setTextViewText(R.id.widget_match_status, mContext.getString(R.string.match_detail_finished));
            } else {
                remoteView.setTextViewText(R.id.widget_match_status, mContext.getString(R.string.match_detail_timed));
            }
        } else {
            remoteView.setTextViewText(R.id.widget_score_home_textview, "0");
            remoteView.setTextViewText(R.id.widget_score_away_textview, "0");
        }

        remoteView.setTextViewText(R.id.widget_matchday, Utilities.getMatchDay(mContext, bookModel.getMatchday(), bookModel.getSessionId()));
        remoteView.setTextViewText(R.id.widget_league, Utilities.getLeague(mContext, bookModel.getSessionId()));

        try {

            if (TextUtils.isEmpty(bookModel.getCrestHomeUrl())) {
                remoteView.setImageViewResource(R.id.widget_home_crest, R.drawable.ic_launcher);
            } else {

                Bitmap homeBitmap = Glide.with(mContext)
                        .load(Utilities.getTeamCrestURL(bookModel.getCrestHomeUrl()))
                        .asBitmap()
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

                if (homeBitmap != null) {
                    remoteView.setImageViewBitmap(R.id.widget_home_crest, homeBitmap);
                }
            }

        } catch (InterruptedException  | ExecutionException e) {
            Log.e(TAG, "Error on getting a home crest: ", e);
        }

        try {

            if (TextUtils.isEmpty(bookModel.getCrestAwayUrl())) {
                remoteView.setImageViewResource(R.id.widget_away_crest, R.drawable.ic_launcher);
            } else {
                Bitmap awayBitmap = Glide.with(mContext)
                        .load(Utilities.getTeamCrestURL(bookModel.getCrestAwayUrl()))
                        .asBitmap()
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

                if (awayBitmap != null) {
                    remoteView.setImageViewBitmap(R.id.widget_away_crest, awayBitmap);
                }
            }

        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "Error on getting a away crest: ", e);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            remoteView.setContentDescription(R.id.widget_home_crest,bookModel.getHomeName());
            remoteView.setContentDescription(R.id.widget_away_crest,bookModel.getAwayHome());
        }

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (mCursor.moveToPosition(position)) {
            return mCursor.getLong(mCursor.getColumnIndex(DatabaseContract.scores_table.MATCH_ID));
        }
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
