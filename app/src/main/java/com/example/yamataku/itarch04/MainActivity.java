package com.example.yamataku.itarch04;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.BIND_AUTO_CREATE;

public class MainActivity extends Activity {

    IMyAidlInterface aidl;
    TextView res;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // activity_main.xml にUIコンポーネントを配置する
        setContentView(R.layout.activity_main);

        // SubServiceにbindする
        Intent i = new Intent(this, com.example.yamataku.itarch04.SubService.class);
        i.setAction(IMyAidlInterface.class.getName());
        bindService(i, serviceConnection, BIND_AUTO_CREATE);

        btn = (Button) findViewById(R.id.start);
        //btn.setOnClickListener(this);//リスナの登録

        //btn  = (Button) findViewById(R.id.stop);
        //btn.setOnClickListener(this);//リスナの登録

        res = (TextView) findViewById(R.id.calcResult);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                switch(v.getId()){

                    case R.id.start://startServiceでサービスを起動
                        //TextView res = (TextView) findViewById(R.id.calcResult);

                        int result = 0;
                        try {
                            result = aidl.add(1, 2);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        res.setText(String.valueOf(result));

                        break;
//                    case R.id.stop://stopServiceでサービスの終了
//                        stopService(new Intent(MainActivity.this, SubService.class));
//                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (aidl != null) {
            unbindService(serviceConnection);
        }
    }




    private ServiceConnection serviceConnection = new ServiceConnection()
    {
        // サービスとの接続時に呼ばれる
        public void onServiceConnected(ComponentName name, IBinder ibinder)
        {
            // Ibinder インターフェースから、AIDLのインターフェースにキャストするメソッドらしい
            aidl = IMyAidlInterface.Stub.asInterface(ibinder);
        }

        // サービスとの切断時に呼ばれる
        public void onServiceDisconnected(ComponentName name)
        {
            aidl = null;
        }

//        private View.OnClickListener btnListener = new View.OnClickListener() {
//            public void onClick(View v) {
//
//                switch(v.getId()){
//
//                    case R.id.start://startServiceでサービスを起動
//                        startService(new Intent(MainActivity.this, SubService.class));
//                        break;
//                    case R.id.stop://stopServiceでサービスの終了
//                        stopService(new Intent(MainActivity.this, SubService.class));
//                        break;
//                }
//            }
//        };
    };
}
