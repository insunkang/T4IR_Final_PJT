package com.example.android.home.gas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class GasControlActivity extends AppCompatActivity {
    AsyncTaskExam asyncTaskExam;
    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Socket socket;
    OutputStream os;
    PrintWriter pw;

    SeekBar L_seekBar;
    SeekBar K_seekBar;
    TextView L_txtValue;
    TextView K_txtValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_control);
        asyncTaskExam = new AsyncTaskExam();
        asyncTaskExam.execute(10, 20);

        //거실 seekBar
        L_txtValue = findViewById(R.id.L_light_number);
        L_seekBar = findViewById(R.id.L_seekBar);
        L_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                L_txtValue.setText(Integer.toString(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new Thread(new Runnable() {
                    String message="";
                    @Override
                    public void run() {
                            message = "L_manual";
                            pw.println(message);
                            pw.println(L_txtValue.getText());
                    }
                }).start();
            }
        });

        //부엌 seekBar
        K_txtValue = findViewById(R.id.K_light_number);
        K_seekBar = findViewById(R.id.K_seekBar);
        K_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                K_txtValue.setText(Integer.toString(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new Thread(new Runnable() {
                    String message="";
                    @Override
                    public void run() {
                        message = "K_manual";
                        pw.println(message);
                        pw.println(K_txtValue.getText());
                    }
                }).start();
            }
        });
    }

    public void send_msg(final View view){
           new Thread(new Runnable() {
               String message="";
               @Override
               public void run() {
                   if(view.getId()==R.id.btn_L_auto){
                       message = "L_auto";
                       pw.println(message);
                   }else if(view.getId()==R.id.btn_L_manual){
                       message = "L_manual";
                       pw.println(message);
                   }else if(view.getId()==R.id.btn_K_auto){
                       message = "K_auto";
                       pw.println(message);
                   }else if (view.getId()==R.id.btn_K_manual){
                       message = "K_manual";
                       pw.println(message);
                   }
               }
           }).start();
        }
    class AsyncTaskExam extends AsyncTask<Integer,String,String> {
        @Override
        protected String doInBackground(Integer... integers) {
            try {
                socket = new Socket("70.12.226.242", 54321);
                //socket = new Socket("70.12.116.75", 12345);
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
    }
}
