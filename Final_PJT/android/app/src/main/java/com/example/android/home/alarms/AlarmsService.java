package com.example.android.home.alarms;

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

import com.example.android.Iowork;
import com.example.android.R;
import com.example.android.Variable;
import com.example.android.home.HomeControlActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class AlarmsService extends Service {
    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    OutputStream os;
    PrintWriter pw;
    public static Socket socket;
    public static Iowork iowork;
    public static String androidId;


    public AlarmsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AlarmsService.this, "default");

        builder.setSmallIcon(R.drawable.ic_aa);
        builder.setContentTitle("CCTV 작동");
        builder.setContentText("CCTV가 작동 중입니다.");

        builder.setColor(Color.RED);
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) AlarmsService.this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }
        Intent intent2 = new Intent(AlarmsService.this, PopupActivity.class);
        intent2.putExtra("link", Variable.CCTVIP);
        PendingIntent pending = PendingIntent.getActivity(AlarmsService.this, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pending);
        Notification notification = builder.build();
        startForeground(1,notification);
        notificationManager.notify(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread t2 = new Thread((new Runnable() {
            @Override
            public void run() {

                try {
                    if (socket == null){
                        //androidId = HomeControlActivity.id;
                        androidId = HomeControlActivity.id;
                        socket = new Socket(Variable.homeServerIP, 23335);

                        ioWork();
                        iowork = new Iowork(is, isr, br, os, pw);
                    }
                    if (socket != null) {
                        /*ioWork();
                        is = socket.getInputStream();
                        isr = new InputStreamReader(is);
                        br = new BufferedReader(isr);
                        os = socket.getOutputStream();
                        pw = new PrintWriter(os, true);
                        iowork = new Iowork(is, isr, br, os, pw);*/
                        //pw.println("phone/"+androidId);
                        //String message = "보냄~~~";
                        //pw.println("pirLed/"+message+"/phone/"+androidId);
                    }
                    Thread t1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                String msg;
                                try {
                                    msg = br.readLine();
                                    Log.d("chat", "서버로 부터 수신된 메시지>>"
                                            + msg);

                                    if (msg.equals("M_on")){
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(AlarmsService.this, "default");
                                        builder.setSmallIcon(R.drawable.ic_aa);
                                        builder.setContentTitle("CCTV 감지");
                                        builder.setContentText("집에서 움직임이 감지되었습니다.");
                                        builder.setColor(Color.RED);
                                        builder.setAutoCancel(true);
                                        NotificationManager notificationManager =
                                                (NotificationManager) AlarmsService.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
                                        }                                        Intent intent = new Intent(AlarmsService.this, PopupActivity.class);
                                        intent.putExtra("link", Variable.CCTVIP);
                                        PendingIntent pending = PendingIntent.getActivity(AlarmsService.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pending);   //PendingIntent 설정
                                        builder.setAutoCancel(true);         //클릭하면 자동으로 알림 삭제
                                        notificationManager.notify(2, builder.build());
                                    } else if (msg.equals("F_on")){
                                        NotificationCompat.Builder builder =
                                                new NotificationCompat.Builder(AlarmsService.this, "default");
                                        builder.setSmallIcon(R.drawable.ic_aa);
                                        builder.setContentTitle("불꽃 감지");
                                        builder.setContentText("집에서 불꽃이 감지되었습니다.");
                                        builder.setColor(Color.RED);
                                        builder.setAutoCancel(true);
                                        NotificationManager notificationManager =
                                                (NotificationManager) AlarmsService.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
                                        }
                                        Intent intent = new Intent(AlarmsService.this, PopupActivity.class);
                                        intent.putExtra("link", Variable.CCTVIP);
                                        PendingIntent pending = PendingIntent.getActivity(AlarmsService.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pending);   //PendingIntent 설정
                                        builder.setAutoCancel(true);         //클릭하면 자동으로 알림 삭제
                                        notificationManager.notify(2, builder.build());
                                    } else if (msg.equals("G_on")) {
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(AlarmsService.this, "default");
                                        builder.setSmallIcon(R.drawable.ic_aa);
                                        builder.setContentTitle("가스 누출 감지");
                                        builder.setContentText("집에서 가스 누출이 감지되었습니다.");
                                        builder.setColor(Color.RED);
                                        builder.setAutoCancel(true);
                                        NotificationManager notificationManager =
                                                (NotificationManager) AlarmsService.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
                                        }
                                        Intent intent = new Intent(AlarmsService.this, PopupActivity.class);
                                        intent.putExtra("link", Variable.CCTVIP);
                                        PendingIntent pending = PendingIntent.getActivity(AlarmsService.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pending);   //PendingIntent 설정
                                        builder.setAutoCancel(true);         //클릭하면 자동으로 알림 삭제
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
            pw.println("phone/"+HomeControlActivity.family+"/"+androidId);
            //pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*@Override
    public void onDestroy() {

        try {
            is.close();
            isr.close();
            br.close();
            os.close();
            pw.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
