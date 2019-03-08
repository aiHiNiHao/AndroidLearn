package com.example.apple.androidlearn.servicetest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

public class SecondServiceActivity extends Activity {

    Messenger mMessenger;
    ServiceConnection conn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        messenger();
        start();
    }

    private void start(){
        Intent intent = new Intent(this, MyService.class);

        startService(intent);
    }

    protected void messenger() {
        Intent intent = new Intent(this, MessengerService.class);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMessenger = new Messenger(service);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn);
            mMessenger = null;
        }

        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }
}
