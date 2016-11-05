package com.example.yamataku.itarch04;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * Created by yamataku on 2016/11/04.
 */

public class SubService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        if(IMyAidlInterface.class.getName().equals(intent.getAction())){
            // IMyAidlInterfaceのインスタンスを返す
            return isvc;
        }
        return null;
    }

    private IMyAidlInterface.Stub isvc = new IMyAidlInterface.Stub()
    {
        @Override
        public int add(int lhs, int rhs) throws RemoteException {
            return lhs + rhs;
        }

        public int half(int lhs, int rhs) throws RemoteException {
            return lhs / rhs;
        }
    };
}
