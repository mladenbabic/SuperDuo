package barqsoft.footballscores.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/29/2016.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

//        int appWidgetId = intent.getIntExtra(
//                AppWidgetManager.EXTRA_APPWIDGET_ID,
//                AppWidgetManager.INVALID_APPWIDGET_ID);

        return (new MatchScoresRemoteViewsFactory(this.getApplicationContext()));
    }

}
