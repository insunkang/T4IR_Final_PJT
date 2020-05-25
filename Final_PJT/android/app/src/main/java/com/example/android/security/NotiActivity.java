package com.example.android.security;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.android.R;

import java.io.PrintWriter;

public class NotiActivity extends AppCompatActivity {
    Button btn1;
    Button btn2;

    //AsyncTaskExam asyncTaskExam;
    //InputStream is;
    //InputStreamReader isr;
    //BufferedReader br;
    //Socket socket;
    //OutputStream os;
    PrintWriter pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);

        Intent intent = new Intent(this,MyService2.class);
        startService(intent);

        //asyncTaskExam = new AsyncTaskExam();
        //asyncTaskExam.execute();

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotification();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeNotification();
            }
        });

    }

    private void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.drawable.ic_aa);
        builder.setContentTitle("CCTV 감지");
        builder.setContentText("집에서 움직임이 감지되었습니다.");

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        //알림을 확인했을 때(알림창 클릭) 다른 액티비티(ByNitificationActivity) 실행
        //클릭했을 때 시작할 액티비티에게 전달하는 Intent 객체 생성
        Intent intent= new Intent(this, PopupActivity.class);
        intent.putExtra("link","http://naver.com/");
        //클릭할 때까지 액티비티 실행을 보류하고 있는 PendingIntent 객체 생성
        PendingIntent pending= PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pending);   //PendingIntent 설정
        builder.setAutoCancel(true);         //클릭하면 자동으로 알림 삭제

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }

    private void removeNotification() {
        // Notification 제거
        NotificationManagerCompat.from(this).cancel(1);
    }

    public void send_msg(final View view){
        new Thread(new Runnable() {
            String message = "";
            @Override
            public void run() {{
                    message = "led_on";
                }
                //서버로 메시지 전송
                pw.println(message);
                pw.flush();
            }
        }).start();
    }
    /*class AsyncTaskExam extends AsyncTask<Integer,String,String> {
        @Override
        protected String doInBackground(Integer... integers) {
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
                                    createNotification();
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
            return "";
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
