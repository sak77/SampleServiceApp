package com.example.sampleserviceapp

import androidx.appcompat.app.AppCompatActivity
import com.example.sampleserviceapp.MyBoundService
import com.example.sampleserviceapp.R
import android.widget.Toast
import android.content.Intent
import com.example.sampleserviceapp.MyStartedService
import android.content.ServiceConnection
import android.content.ComponentName
import android.os.*
import android.view.View
import android.widget.Button

/**
 * Service class is an abstract class. So one can extend it to create an instance.
 * Or use one of the existing sub-classes. The extended sub-class can override different
 * methods like onStartCommand or onBind to define the type of service. Service also has
 * life-cycle callbacks which can be used to setup or teardown elements of the service.
 */
class MainActivity : AppCompatActivity() {
    var myBoundService: MyBoundService? = null
    var mBound = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnClick = findViewById<Button>(R.id.button)
        btnClick.setOnClickListener { view: View? ->
            Toast.makeText(
                this,
                "Clicked!",
                Toast.LENGTH_SHORT
            ).show()
        }
        val btnStart = findViewById<Button>(R.id.button3)
        btnStart.setOnClickListener { view: View? ->
            val serviceIntent = Intent(this@MainActivity, MyStartedService::class.java)
            serviceIntent.putExtra("count", 10)
            /*
            Just like an Activity, a Service can be started by calling startService(Intent) method.
            This sends an intent to the Android system with details to start the service.
            The system will then look up the service in the manifest and launch it if possible.
            So, its important to declare the service in manifest as well.
             */startService(serviceIntent)
        }
    }

    /*
    ServiceConnection class is a callback mechanism for Bound services to notify to
    the calling component whether the service connection was successful or not.
     */
    var mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            mBound = true
            val messenger = Messenger(iBinder)
            //While the constructor of Message is public,
            // the best way to get one of these is to call Message.obtain() or one of the Handler.obtainMessage() methods,
            // which will pull them from a pool of recycled objects.
            val message = Message.obtain()
            message.what = 1
            //We use bundle to pass messages.
            //We can pass the string directly as well.
            //However that only works for local services
            //if this service is being bound by remote client
            //then passing string directly in message.obj will NOT work.
            //it will throw exception that
            //Can't marshal non-Parcelable objects across processes.
            //so for IPC you can use bundles instead.
            //So since this service is also being invoked by another
            //remote client: DummyIPCBoundClient
            //Hence i decide to use bundle to send messages instead.
            val bundle = Bundle()
            bundle.putString("id", "Saket")
            message.obj = bundle
            try {
                messenger.send(message)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            /*
            MyBoundService.MyBinder myBinder = (MyBoundService.MyBinder)iBinder;
            myBoundService = myBinder.getService();
            String response = myBoundService.sayHello("Saket");
            Log.v("TAG",response);
*/
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            mBound = false
            myBoundService = null
        }
    }

    override fun onStop() {
        super.onStop()
        //Is it important to unbind a bound service when no longer required?? Maybe not..
        if (mBound) unbindService(mServiceConnection)
    }
}