package com.example.android.home.light;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;
import com.example.android.home.HomeControlActivity;
import com.example.android.home.alarms.AlarmsService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class LightControlActivity extends AppCompatActivity {
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
    String androidId = HomeControlActivity.family + "/" +HomeControlActivity.id;
    Thread t1;
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
                    String message = "";

                    @Override
                    public void run() {
                        message = "L_manual";
                        pw.println("led/"+message+"/"+"phone/"+androidId);
                        pw.println("led/"+L_txtValue.getText()+"/"+"phone/"+androidId);
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
                    String message = "";

                    @Override
                    public void run() {
                        message = "K_manual";
                        pw.println("led/"+message+"/"+"phone/"+androidId);
                        pw.println("led/"+K_txtValue.getText()+"/"+"phone/"+androidId);
                    }
                }).start();
            }
        });
    }

    public void send_msg(final View view) {
        new Thread(new Runnable() {
            String message = "";

            @Override
            public void run() {
                if (view.getId() == R.id.btn_L_auto) {
                    message = "L_auto";
                    pw.println("led/"+message+"/"+"phone/"+androidId);

                } else if (view.getId() == R.id.btn_L_manual) {
                    message = "L_manual";
                    pw.println("led/"+message+"/"+"phone/"+androidId);
                } else if (view.getId() == R.id.btn_K_auto) {
                    message = "K_auto";
                    pw.println("led/"+message+"/"+"phone/"+androidId);
                } else if (view.getId() == R.id.btn_K_manual) {
                    message = "K_manual";
                    pw.println("led/"+message+"/"+"phone/"+androidId);
                }
            }
        }).start();
    }

    class AsyncTaskExam extends AsyncTask<Integer, String, String> {
        @Override
        protected String doInBackground(Integer... integers) {

            socket = AlarmsService.socket;
            if (socket != null) {
                ioWork();
            }
            t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!t1.isInterrupted()) {
                        String msg= "";
                        try {
                            msg = br.readLine();
                            t1.sleep(300);
                        } catch (IOException | InterruptedException e) {
                            t1.interrupt();
                        }
                    }
                }
            });
            t1.start();
            return "";
        }

        void ioWork(){
            is = AlarmsService.iowork.getIs();
            isr = AlarmsService.iowork.getIsr();
            br = AlarmsService.iowork.getBr();
            os = AlarmsService.iowork.getOs();
            pw = AlarmsService.iowork.getPw();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
