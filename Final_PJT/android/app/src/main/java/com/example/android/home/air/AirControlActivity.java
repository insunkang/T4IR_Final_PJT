package com.example.android.home.air;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;


public class  AirControlActivity extends AppCompatActivity {
    AsyncTaskExam asyncTaskExam;
    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Socket socket;
    OutputStream os;
    PrintWriter pw;
    String androidId = AlarmsService.androidId;
    Thread t1;
    private Spinner T_spinner;
    ArrayList<Integer> arrayList;
    ArrayAdapter<Integer> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_control);
        asyncTaskExam = new AsyncTaskExam();
        asyncTaskExam.execute(10, 20);
        arrayList = new ArrayList<>();
        for (int i = 10; i <= 30; i++) {
            arrayList.add(i);
        }
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);

        T_spinner = findViewById(R.id.A_w_temperature);
        T_spinner.setAdapter(arrayAdapter);
        T_spinner.setSelection(20);
        T_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), arrayList.get(i) + "ºC가 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();
                final String num = T_spinner.getSelectedItem().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String message = "";
                        message = "FAN_auto";
                        pw.println("air/"+message+"/"+"phone/"+ androidId);
                        pw.println("air/"+num+"/"+"phone/" + androidId);
                    }
                }).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void send_msg(final View view) {
        new Thread(new Runnable() {
            String message = "";

            @Override
            public void run() {
                if (view.getId() == R.id.btn_A_auto) {
                    message = "FAN_auto";
                    pw.println("air/"+message+"/"+"phone/" + androidId);
                } else if (view.getId() == R.id.btn_A_on) {
                    message = "FAN_on";
                    pw.println("air/"+message+"/"+"phone/" + androidId);
                } else if (view.getId() == R.id.btn_A_off) {
                    message = "FAN_off";
                    pw.println("air/"+message+"/"+"phone/" + androidId);
                }
            }
        }).start();
    }

    class AsyncTaskExam extends AsyncTask<Integer, String, String> {
        String temperature = "";
        String humidity = "";

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
                        String msg = "";
                        try {
                            Log.d("check1", br.toString());
                            msg = br.readLine();
                            Log.d("temp", msg);
                            filteringMsg(msg);
                            t1.sleep(300);
                        }catch (IOException | InterruptedException e) {
                            Log.d("check1","인터럽트종료");
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

        private void filteringMsg(String msg) {
            try {
                StringTokenizer token = new StringTokenizer(msg, "-");
                String msg1 = token.nextToken();
                String msg2 = token.nextToken();
                String msg3 = token.nextToken();
                String msg4 = token.nextToken();
                temperature = msg2;
                humidity = msg4;
                publishProgress();
            } catch (NoSuchElementException e) {
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            TextView A_temp = findViewById(R.id.A_temperature);
            TextView A_humi = findViewById(R.id.A_humidity);
            A_temp.setText(temperature + "℃");
            A_humi.setText(humidity + "％");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*try {
            is.close();
            isr.close();
            br.close();
            os.close();
            pw.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        t1.interrupt();
        Log.d("check1", Thread.activeCount() + "");
    }
}
