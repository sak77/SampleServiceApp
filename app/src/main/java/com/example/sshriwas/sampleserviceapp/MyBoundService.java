package com.example.sshriwas.sampleserviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * With this bound service we want to show how to use IBinder instance to
 * communicate with client component. The bound service is bound to
 * the life-cycle of the calling component(s). After the last component unbinds
 * itself from the bound service, the system automatically terminates the bound
 * service. For this example, the client will send a name to this service and
 * the service responds with a welcome message to the client which the client
 * shows on its page.
 *
 * There are 3 ways to generate a IBinder instance -
 * 1. Extending BInder class - Suitable only for local service
 * 2. Using Messenger class - Suitable for IPC in a queued manner
 * 3. AIDL - For IPC that requires multi-threaded approach
 */
public class MyBoundService extends Service {

    private IBinder myBinder = new MyBinder();
    public MyBoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  myBinder;
    }

    //Here we display how to create IBinder instance by extending Binder class.
    //This approach is suitable for local bound services whose client runs in the
    //same process as the service. Here the service is not meant to be used by other
    //apps.
    public class MyBinder extends Binder{
        MyBoundService getService(){
            return MyBoundService.this;
        }
    }

    public String sayHello(String name) {
        return "Hello " + name;
    }
}