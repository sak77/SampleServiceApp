package com.example.sampleserviceapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The purpose of this app is to explore services. I will create 3 services: Started Service, Intent Service
 * and Bound Service and try to understand how they are implemented, what is the difference and when
 * to use each one of them.
 */
public class MainActivity extends AppCompatActivity {

    MyBoundService myBoundService;
    boolean mBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnClick = findViewById(R.id.button2);
        btnClick.setOnClickListener(view -> {
            Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
        });
        Button btnStart = findViewById(R.id.button3);
        btnStart.setOnClickListener(view -> {
            Intent intentService = new Intent(MainActivity.this, MyBoundIPCService.class);
            //intentService.putExtra("count",50);
            //Calling Started Service
            //startService(intentService);
            //Calling IntentService
            //MyIntentService.startActionFoo(MainActivity.this, "PARAM1", "PARAM2");
            //Calling Bound Service
            bindService(intentService, mServiceConnection, Context.BIND_AUTO_CREATE);
        });
    }


    /*
    ServiceConnection class is a callback mechanism for Bound services to notify to
    the calling component whether the service connection was successful or not.
     */
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBound = true;
            Messenger messenger = new Messenger(iBinder);
            //While the constructor of Message is public,
            // the best way to get one of these is to call Message.obtain() or one of the Handler.obtainMessage() methods,
            // which will pull them from a pool of recycled objects.
            Message message = Message.obtain();
            message.what = 1;
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
            Bundle bundle = new Bundle();
            bundle.putString("id", "Saket");
            message.obj = bundle;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
/*
            MyBoundService.MyBinder myBinder = (MyBoundService.MyBinder)iBinder;
            myBoundService = myBinder.getService();
            String response = myBoundService.sayHello("Saket");
            Log.v("TAG",response);
*/
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
            myBoundService = null;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Is it important to unbind a bound service when no longer required?? Maybe not..
        if (mBound)
        unbindService(mServiceConnection);
    }
}