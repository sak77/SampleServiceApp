package com.example.sampleserviceapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * Started service resemble a fire-and-forget background task.
 * It is called by a component, like Activity, Broadcastreceiver or ContentProvider.
 * It is designed to run indefinitely. Which means the service continues to run even
 * after the calling component is not active, or destroyed. So its responsibility of
 * calling component or the service itself to terminate, when not required.
 * However, since the service runs in the same app process as the calling component,
 * if the app is destroyed, then the service is also destroyed.
 */
class MyStartedService : Service() {
    val TAG = "Saket"

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "ONCREATE")
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    /*
     OnStartCommand is invoked every time startService() is called. So below,
     multiple calls to startService() will result in multiple threads being created.
     But onCreate() is only called once.

     onStartCommand returns an int flag value which defines how the system should behave if
     the service gets terminated. Options are START_NOT_STICKY, START_STICKY and START_REDELIVER_INTENT
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val count = intent.getIntExtra("count", 0)
        /*
        By default service runs on the main thread. So if there is any long running
        operation to be performed, use a separate thread.
         */
        Thread {
            for (i in 0..count) {
                try {
                    Thread.sleep(1000)
                    Log.v(TAG, "startservice count : $i")
                    if (i == 3) {
                        /*
                        Once requested to stop with stopSelf() or stopService(),
                        the system destroys the service as soon as possible.
                         */
                        stopSelf()
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "startservice onDestroy called")
    }
}
