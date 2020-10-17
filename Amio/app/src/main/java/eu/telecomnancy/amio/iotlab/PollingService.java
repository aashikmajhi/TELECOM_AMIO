package eu.telecomnancy.amio.iotlab;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Polling service who will periodically fetch data from the server
 */
public class PollingService extends Service {

    /**
     * Elapsed milliseconds between each request to the server
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final long POLLING_DELAY_MS = 2_000;

    /**
     * Android logging tag for this class
     */
    private static final String TAG = PollingService.class.getName();

    /**
     * Inner-timer used for firing events
     */
    private Timer _timer;

    /**
     * Schedule the polling task
     * @param delay Delay in milliseconds before task is to be executed
     * @param period Time in milliseconds between successive task executions
     */
    @SuppressWarnings("SameParameterValue")
    private void schedulePollingTask(long delay, long period) {
        TimerTask task = new PollingTimerTask();
        _timer.scheduleAtFixedRate(task, delay, period);

        Log.i(TAG, String.format("Task schedule for %d ms, every %d ms", delay, period));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        _timer.cancel();

        Log.d(TAG, "Service destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        _timer = new Timer();
        schedulePollingTask(0, POLLING_DELAY_MS);

        Log.i(TAG, "Service started");

        return START_STICKY;
    }

}
