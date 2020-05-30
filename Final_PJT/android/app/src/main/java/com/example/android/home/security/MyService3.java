package com.example.android.home.security;

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

public class MyService3 extends Service {
    public MyService3() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService3.this, "default");

        builder.setSmallIcon(R.drawable.ic_aa);
        builder.setContentTitle("CCTV 작동");
        builder.setContentText("CCTV가 작동 중입니다.");

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) MyService3.this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        //알림을 확인했을 때(알림창 클릭) 다른 액티비티(ByNitificationActivity) 실행
        //클릭했을 때 시작할 액티비티에게 전달하는 Intent 객체 생성
        Intent intent2 = new Intent(MyService3.this, PopupActivity.class);
        intent2.putExtra("link", "http://naver.com/");
        //클릭할 때까지 액티비티 실행을 보류하고 있는 PendingIntent 객체 생성
        PendingIntent pending = PendingIntent.getActivity(MyService3.this, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pending);   //PendingIntent 설정
        //  builder.setAutoCancel(true);         //클릭하면 자동으로 알림 삭제

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        Notification notification = builder.build();
        startForeground(1,notification);

        notificationManager.notify(1, notification);
        //notificationManager.cancel(1);
        Log.d("myservice", "serviceThread");

        Intent in = new Intent(this, MyService2.class);
        startService(in);

        return START_STICKY;
    }
}
