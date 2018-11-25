package com.example.sshriwas.sampleserviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * A simple started service to print all values from 0 to 50
 */
public class MyStartedService extends Service {
/*    public MyStartedService() {
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("ONCREATE", "ONCREATE");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /*
     OnStartCommand is invoked every time startService() is called. So based on the
     below implementation, multiple calls to startService() will result in
     multiple threads being created. But i think onCreate() is only called once??

     onStartCommand returns an int flag value which defines how the system should behave if
     the service gets terminated. Options are START_NOT_STICKY, START_STICKY and START_REDELIVER_INTENT
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int count = intent.getIntExtra("count", 0);
        new Thread(() -> {
            for (int i = 0; i <= count; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.v("SERVICE", "COUNT "+ i);
            }
            stopSelf();
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
