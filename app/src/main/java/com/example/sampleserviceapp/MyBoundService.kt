package com.example.sampleserviceapp

import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.os.IBinder
import com.example.sampleserviceapp.MyBoundService.MyBinder
import android.content.Intent
import android.os.Binder
import android.widget.Toast
import com.example.sampleserviceapp.MyBoundService

/**
 * Bound services represent a client-server setup.
 * Clients components can bind to the service to send some request. They unbind once done.
 * The service returns a IBinder instance to communicate with client component.
 * After the last component unbinds itself from the bound service, the system
 * automatically terminates the bound service.
 *
 * There are 3 ways to generate a IBinder instance -
 * 1. Extending Binder class - Suitable when client and service are in same process
 * 2. Using Messenger class - Suitable for IPC in a queued manner
 * 3. AIDL - For IPC that requires multi-threaded approach
 */
class MyBoundService : Service() {

    //Here we display how to create IBinder instance by extending Binder class.
    //This approach is suitable for local bound services whose client runs in the
    //same process as the service. Here the service is not meant to be used by other
    //apps.
    inner class MyBinder : Binder() {
        val service: MyBoundService
            get() = this@MyBoundService
    }

    private val myBinder: IBinder = MyBinder()

    override fun onBind(intent: Intent): IBinder {
        // TODO: Return the communication channel to the service.
        return myBinder
    }

    /**
     * It is not possible to perform UI tasks from Service context.
     * For eg. show a dialog.
     * This is because Service extends ContextWrapper class. Which
     * is a non-UI context.
     * However, if i pass context of MainActivity to sayHello()
     * and show dialog using that context, then it works fine.
     * This is because, Activity extends ContextThemeWrapper. Which
     * is a UI Context class.
     * This is only for demo purpose. Ideally one should not
     * pass UI context to a service like this. Passing UI
     * context to long running background task can lead to
     * memory leaks.
     */
    fun sayHello(name: String, context: Context) {
        //Toast.makeText(this@MyBoundService, "Hello $name", Toast.LENGTH_SHORT).show()
        val dialog = AlertDialog.Builder(applicationContext)
            .setTitle("Sample dialog")
            .setMessage("Hello Sample")
            .create()
        dialog.show()
    }
}