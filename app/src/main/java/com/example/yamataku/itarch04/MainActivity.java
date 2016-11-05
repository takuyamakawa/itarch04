package com.example.yamataku.itarch04;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.BIND_AUTO_CREATE;

public class MainActivity extends Activity {

    IMyAidlInterface aidl;
    TextView res;
    Button btn;
    private  int result = 1;
    private  int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // activity_main.xml にUIコンポーネントを配置する
        setContentView(R.layout.activity_main);

        // SubServiceにbindする
        final Intent i = new Intent(this, com.example.yamataku.itarch04.SubService.class);
        i.setAction(IMyAidlInterface.class.getName());
        bindService(i, serviceConnection, BIND_AUTO_CREATE);

        btn = (Button) findViewById(R.id.start);

        res = (TextView) findViewById(R.id.calcResult);
        res.setTextSize(36);
        
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                switch(v.getId()){

                    case R.id.start://startServiceでサービスを起動

                        Timer timer = new Timer(true);
                        final Handler handler = new Handler();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        handler.post( new Runnable(){
                                            public void run(){
                                                try {
                                                    result = aidl.add(result, result);
                                                } catch (RemoteException e) {
                                                    e.printStackTrace();
                                                }
                                                res.setText(String.valueOf(result));
                                            }
                                        });
                                    }
                                }, 0, 1000   //開始遅延(何ミリ秒後に開始するか)と、周期(何ミリ秒ごとに実行するか)
                        );

                        break;
//                    case R.id.stop://stopServiceでサービスの終了
//                        result = 0;
//                        res.setText(String.valueOf(result));
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

    };
}
