package multi.android
        .infortainmentw;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import multi.android.infortainmentw.control.Control;
import multi.android.infortainmentw.music.MusicFragment;

public class MainActivity extends AppCompatActivity {
    // ================================
    // 프래그먼트
    Control control = new Control();
    MusicFragment musicFragment = new MusicFragment();
    // =================================
    // 소켓, 입출력변수
    Socket socket;
    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    OutputStream os;
    PrintWriter pw;
    String ip = "70.12.228.112";
    int port = 12345;
    // =================================

    // =================================
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_control, control);
        transaction.replace(R.id.fragment_music, musicFragment);

        new AsyncTask<String, String, String>() {
            String temporature="";
            String humidity="";
            @Override
            protected String doInBackground(String... strings) {
                try {

                    socket = new Socket(ip, port);
                    if (socket != null) {
                        ioWork();
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
                                    filteringMsg(msg);
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

            void ioWork() {
                try {
                    is = socket.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);

                    os = socket.getOutputStream();
                    pw = new PrintWriter(os, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            private void filteringMsg(String msg) {
                try {
                    StringTokenizer token = new StringTokenizer(msg, "/");
                    String protocol = token.nextToken();
                    String message = token.nextToken();
                    System.out.println("프로토콜:" + protocol + ",메시지:" + message);
                    if (protocol.equals("temporature")) {
                        temporature = message;
                        publishProgress(message);
                    } else if (protocol.equals("humidity")) {
                        humidity = message;
                        publishProgress(message);
                    }
                } catch(NoSuchElementException e){

                }
            }

            @Override
            protected void onProgressUpdate(String... values) {
                TextView tvTemp = findViewById(R.id.temporature);
                TextView tvHumi = findViewById(R.id.humidity);
                tvTemp.setText(temporature+"℃");
                tvHumi.setText(humidity+"％");
            }
        }.execute();

    }

    public void left_btn(View v) {
        final ImageView iv = findViewById(R.id.rew);
        new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... strings) {
                for (int i = 0; i < 5; i++) {
                    publishProgress();
                    SystemClock.sleep(600);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if (iv.getVisibility() == View.INVISIBLE) {
                    iv.setVisibility(View.VISIBLE);
                } else {
                    iv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            protected void onPostExecute(String s) {
                iv.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    public void right_btn(View v) {
        final ImageView iv = findViewById(R.id.ff);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                for (int i = 0; i < 5; i++) {
                    publishProgress();
                    SystemClock.sleep(600);
                }
                return null;
            }
            @Override
            protected void onProgressUpdate(String... values) {
                if (iv.getVisibility() == View.INVISIBLE) {
                    iv.setVisibility(View.VISIBLE);
                } else {
                    iv.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            protected void onPostExecute(String s) {
                iv.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }
    public void emergency_btn(View v) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pw.println("emergency");
            }
        });
        thread.start();
    }

}
