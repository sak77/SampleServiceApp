package com.example.sampleserviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

//For IPC with a queued approach we define a Handler and its corresponding
//Messenger which is used to return the IBinder instance in onBind()
//unlike regular bound service where we extend the Binder class, with
//IPC the client and Service communicate via messages and not methods
public class MyBoundIPCService extends Service {

    private static final int SAY_HELLO = 1;
    Messenger messenger;

    public MyBoundIPCService() {
        messenger = new Messenger(new MyServiceHandler());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return messenger.getBinder();
    }

    //Our Handler class
    private class MyServiceHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SAY_HELLO:
                    if (msg.obj != null) {
                        Bundle bundle = (Bundle) msg.obj;
                        Toast.makeText(MyBoundIPCService.this, "Hello "+ bundle.getString("id"), Toast.LENGTH_SHORT).show();
                        //If client has a replyto messenger object then send a response as well.
                        if (msg.replyTo != null){
                            //inform the client that operation was successful.
                            Messenger clientMessenger = msg.replyTo;
                            Message responseMessage = new Message();
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("RESPONSE", "SUCCESS");
                            responseMessage.obj = bundle1;
                            try {
                                clientMessenger.send(responseMessage);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}
