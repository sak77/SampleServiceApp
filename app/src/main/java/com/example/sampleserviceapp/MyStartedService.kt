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
 *
 * For API 26 and above, prefer WorkManager to background services. As the system
 * imposes many restrictions on background services when the app isn't itself in
 * the foreground.
 */
class MyStartedService : Service() {
    val TAG = "Saket"

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "ONCREATE")
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /*
     OnStartCommand is invoked every time startService() is called. So below,
     multiple calls to startService() will result in multiple threads being created.
     But onCreate() is only called once.

     onStartCommand returns an int flag value which defines how the system should behave
     if the service gets terminated after start:
     START_STICKY - default. System will try to re-start service. However in practice,
     service is recreated and destroyed. Since app is idle.
     START_NOT_STICKY - System does not try to restart the service. Works as expected.
     START_REDELIVER_INTENT - System will try to restart service, but also redeliver
     actual intent that started the servce. Again, this does not happen since app is idle.

     All above flags work correctly for foreground services. Refer evernote note on how
     services behave on being killed for more info.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.v(TAG, "startservice onStartCommand called")

        val count = intent?.getIntExtra("count", 0)
        Log.v(TAG, "count $count")
        /*
        By default service runs on the main thread. So if there is any long running
        operation to be performed, use a separate thread.
         */
        Thread {
            for (i in 0..count!!) {
                try {
                    Thread.sleep(1000)
                    Log.v(TAG, "startservice count : $i")
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "startservice onDestroy called")
    }
}
