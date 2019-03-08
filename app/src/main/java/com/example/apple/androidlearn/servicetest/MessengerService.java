package com.example.apple.androidlearn.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

public class MessengerService extends Service {
    private final String TAG = MessengerService.class.getName();
    private MessengerHandler handler = new MessengerHandler();
    private Messenger messenger = new Messenger(handler);

    private final int MESSAGE_STOP = 1;

    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_STOP){
                removeMessages(0);
            }else{
                Bundle data = msg.getData();
                String content = data.getString("content");
                Log.i(TAG, "handleMessage: msg " + content);
                Message message = Message.obtain(msg);
                try {
                    msg.replyTo.send(message);//发送信息给客户端
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 如果有多个activity startService或bindService,只会调用一次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
    }
    /**
     * 如果有多个activity bindService,只会调用一次
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return messenger.getBinder();
    }

    /**
     * 如果有多个activity startService,会调用  多多多多  次
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 如果有多个activity bindService,只在最后一个band的activity退出时调用一次
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        Message message = Message.obtain();
        message.what = MESSAGE_STOP;
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return super.onUnbind(intent);
    }
    /**
     * 如果有多个activity bindService或startService,只在最后一个bind或start的activity退出时调用一次
     */
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }


}
