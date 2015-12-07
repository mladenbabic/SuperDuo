package barqsoft.footballscores.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FootballScoreSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static FootballScoresSyncAdapter sFootballScoreSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("FootballScoreSyncS", "onCreate-FootballScoreSyncService");
        synchronized (sSyncAdapterLock) {
            if (sFootballScoreSyncAdapter == null) {
                sFootballScoreSyncAdapter = new FootballScoresSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sFootballScoreSyncAdapter.getSyncAdapterBinder();
    }
}