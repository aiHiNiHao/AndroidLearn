package com.example.apple.androidlearn.servicetest;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class ServiceActivity extends AppCompatActivity {
    protected final String TAG = ServiceActivity.class.getName();
    IMyBinder mBinder;
    Messenger mMessenger;
    ServiceConnection conn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        messenger();
        start();
        startActivity(new Intent(this, SecondServiceActivity.class));
    }

    private void start() {
        Intent intent = new Intent(this, MyService.class);

        startService(intent);

    }

    private void bind() {
        Intent intent = new Intent(this, MyService.class);

        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBinder = (IMyBinder) service;
                mBinder.involkMethod();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, conn, BIND_AUTO_CREATE);
        unbindService(conn);
    }

    protected void messenger() {
        Intent intent = new Intent(this, MessengerService.class);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMessenger = new Messenger(service);
                Message message = Message.obtain();
                message.replyTo = replyMessenger;//传给服务器，用来接受服务器的信息
                Bundle data = new Bundle();
                data.putString("content", "hellow say");
                message.setData(data);
                try {
                    mMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    //接受服务端返回的信息
    private ReplyHandler replyHandler = new ReplyHandler();

    private class ReplyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage: 收到 reply");
        }
    }

    private Messenger replyMessenger = new Messenger(replyHandler);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            //不解绑就会报Activity has leaked ServiceConnection的问题
            unbindService(conn);
        }

        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }
}
