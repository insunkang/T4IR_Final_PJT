package com.example.android.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;
import com.example.android.home.air.AirControlActivity;
import com.example.android.home.alarms.AlarmsService;
import com.example.android.home.light.LightControlActivity;

public class HomeControlActivity extends AppCompatActivity {

    public static String family;
    public static String id;
    LinearLayout LightControl;
    LinearLayout AirControl;
    Intent intent;
/*    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    OutputStream os;
    PrintWriter pw;*/

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_control);
        Intent intent2 = getIntent();
        family = intent2.getStringExtra("member_family");
        id = intent2.getStringExtra("loginID");
        Intent notiintent = new Intent(this, AlarmsService.class);
        startService(notiintent);

        LightControl = findViewById(R.id.LightControl);
        LightControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), LightControlActivity.class);
                startActivity(intent);
            }
        });

/*
        GasControl = findViewById(R.id.GasControl);
        GasControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), GasControlActivity.class);
                startActivity(intent);
            }
        });
*/

        AirControl = findViewById(R.id.AirControl);
        AirControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), AirControlActivity.class);
                startActivity(intent);
            }
        });

        /*new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... strings) {
                try {
                    //androidId = "1111";
                    socket = new Socket("70.12.116.58", 23335);
                    ioWork();
                    *//*is = socket.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    os = socket.getOutputStream();
                    pw = new PrintWriter(os, true);
                    iowork = new Iowork(is, isr, br, os, pw);
                    pw.println("phone/"+androidId);*//*
                    Log.d("check1","소켓통신시작");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();*/

    }
    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                pw.println("close");
                Log.e("myservice","close전송");
            }
        }).start();
        SystemClock.sleep(300);
       try {
           // 자원반납
            is.close();
            isr.close();
            br.close();
            os.close();
            pw.close();
            socket.close();
            Log.d("check1","소캣종료");
        } catch (IOException e) {

        }

    }
    void ioWork(){

        try {
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            os = socket.getOutputStream();
            pw = new PrintWriter(os,true);
            pw.println("phone/"+androidId);
            //pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}
