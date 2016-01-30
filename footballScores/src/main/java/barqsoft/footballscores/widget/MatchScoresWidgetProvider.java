package barqsoft.footballscores.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;
import barqsoft.footballscores.activity.MainActivity;
import barqsoft.footballscores.sync.FootballScoresSyncAdapter;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/29/2016.
 */
public class MatchScoresWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "MatchScoresWidgetP";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_match_scores);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            views.setRemoteAdapter(R.id.match_scores_list, new Intent(context, WidgetService.class));
            views.setEmptyView(R.id.match_scores_list, R.id.scores_empty);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (FootballScoresSyncAdapter.BROADCAST_DATA_UPDATED.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: got BROADCAST_DATA_UPDATED event");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.match_scores_list);
        }
    }

}
