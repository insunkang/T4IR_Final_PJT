package com.example.android.security;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.android.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class MyService2 extends Service {
    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Socket socket;
    OutputStream os;
    PrintWriter pw;

    public MyService2() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService2.this, "default");

        builder.setSmallIcon(R.drawable.ic_aa);
        builder.setContentTitle("CCTV 작동");
        builder.setContentText("CCTV가 작동 중입니다.");

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) MyService2.this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        //알림을 확인했을 때(알림창 클릭) 다른 액티비티(ByNitificationActivity) 실행
        //클릭했을 때 시작할 액티비티에게 전달하는 Intent 객체 생성
        Intent intent2 = new Intent(MyService2.this, PopupActivity.class);
        intent2.putExtra("link", "http://70.12.228.195:8091/stream_simple.html/");
        //클릭할 때까지 액티비티 실행을 보류하고 있는 PendingIntent 객체 생성
        PendingIntent pending = PendingIntent.getActivity(MyService2.this, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pending);   //PendingIntent 설정
        //  builder.setAutoCancel(true);         //클릭하면 자동으로 알림 삭제

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        Notification notification = builder.build();
        startForeground(1,notification);

        notificationManager.notify(1, notification);
        Log.d("myservice", "serviceThread");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread t2 = new Thread((new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("70.12.228.82", 12345);
                    if (socket != null) {
                        ioWork();
                    }
                    //서버에서 전달되는 메시지를 읽는 쓰레드
                    Thread t1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                String msg;
                                try {
                                    msg = br.readLine();
                                    Log.d("chat", "서버로 부터 수신된 메시지>>"
                                            + msg);
                                    if (msg.equals("on")){
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService2.this, "default");

                                        builder.setSmallIcon(R.drawable.ic_aa);
                                        builder.setContentTitle("CCTV 감지");
                                        builder.setContentText("집에서 움직임이 감지되었습니다.");

                                        builder.setColor(Color.RED);
                                        // 사용자가 탭을 클릭하면 자동 제거
                                        builder.setAutoCancel(true);

                                        // 알림 표시
                                        NotificationManager notificationManager =
                                                (NotificationManager) MyService2.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
                                        }

                                        //알림을 확인했을 때(알림창 클릭) 다른 액티비티(ByNitificationActivity) 실행
                                        //클릭했을 때 시작할 액티비티에게 전달하는 Intent 객체 생성
                                        Intent intent = new Intent(MyService2.this, PopupActivity.class);
                                        intent.putExtra("link", "http://naver.com/");
                                        //클릭할 때까지 액티비티 실행을 보류하고 있는 PendingIntent 객체 생성
                                        PendingIntent pending = PendingIntent.getActivity(MyService2.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pending);   //PendingIntent 설정
                                        builder.setAutoCancel(true);         //클릭하면 자동으로 알림 삭제

                                        // id값은
                                        // 정의해야하는 각 알림의 고유한 int값
                                        // Notification notification = builder.build();
                                        notificationManager.notify(2, builder.build());
                                    }
                                } catch (IOException e) {

                                }
                            }
                        }
                    });
                    t1.start();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
        t2.start();

        return START_STICKY;
    }

    void ioWork(){
        try {
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            os = socket.getOutputStream();
            pw = new PrintWriter(os,true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        Log.e("myservice","MyService onDestroy");


    }
}
