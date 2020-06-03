package com.example.android.driving;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.MainActivity;
import com.example.android.R;
import com.example.android.Variable;
import com.example.android.car.Car;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import static com.example.android.R.id.driving_left;

public class Driving extends AppCompatActivity {
    public final static String DRIVE_URL = Variable.carCemeraIP;
    //public final static String DRIVE_URL = "http://70.12.228.195:8091/stream_simple.html";
    ////public final static String DRIVE_URL = "https://sites.google.com/site/ubiaccessmobile/sample_video.mp4";
    //public final static String DRIVE_URL = "http://70.12.227.21:8091/?action=stream";
    WebView webView;
    Button btn_driving_view_start, btn_driving_view_stop, btn_driving_stop;
    MyAsyncTask myAsyncTask;
    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Socket socket;
    OutputStream os;
    PrintWriter pw;

    @SuppressLint({"SourceLockedOrientationActivity", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("mykim", "onCreate");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_driving);
        webView = findViewById(R.id.driving_view);

        btn_driving_stop = findViewById(R.id.driving_stop);

        Button forward = findViewById(R.id.driving_forward);
        Button left = findViewById(driving_left);
        Button right = findViewById(R.id.driving_right);
        Button back = findViewById(R.id.driving_back);

        btn_driving_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Log.d("mykim", "선택됨");
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(10, 20);

        webView.loadUrl(DRIVE_URL);
        forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int action = event.getAction();
                        String message = "";
                        if (action == MotionEvent.ACTION_DOWN) {
                            message = "move/forward";
                        } else if (action == MotionEvent.ACTION_UP) {
                            message = "move/stop";
                        }
                        pw.println(MainActivity.member_family + "/" + MainActivity.loginID + "/" + message);
                    }
                }).start();

                return true;
            }
        });
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int action = event.getAction();
                        String message = "";

                        if (action == MotionEvent.ACTION_DOWN) {
                            message = "move/left";
                        } else if (action == MotionEvent.ACTION_UP) {
                            message = "move/stop";
                        }
                        pw.println(MainActivity.member_family + "/" + MainActivity.loginID + "/" + message);
                    }
                }).start();

                return true;
            }
        });
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int action = event.getAction();
                        String message = " ";

                        if (action == MotionEvent.ACTION_DOWN) {
                            message = "move/right";
                        } else if (action == MotionEvent.ACTION_UP) {
                            message = "move/stop";
                        }
                        pw.println(MainActivity.member_family + "/" + MainActivity.loginID + "/" + message);
                    }
                }).start();

                return true;
            }
        });
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int action = event.getAction();
                        String message = " ";

                        if (action == MotionEvent.ACTION_DOWN) {
                            message = "move/back";
                        } else if (action == MotionEvent.ACTION_UP) {
                            message = "move/stop";
                        }
                        pw.println(MainActivity.member_family + "/" + MainActivity.loginID + "/" + message);
                    }
                }).start();

                return true;
            }
        });
    }


    public void send_msg(final View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message = " ";
                if (view.getId() == R.id.driving_forward) {
                    message = "move/forward";
                } else if (view.getId() == driving_left) {
                    message = "move/left";
                } else if (view.getId() == R.id.driving_right) {
                    message = "move/right";
                } else if (view.getId() == R.id.driving_back) {
                    message = "move/back";
                } else if (view.getId() == R.id.velo_up) {
                    message = "move/up";
                } else if (view.getId() == R.id.velo_down) {
                    message = "move/down";
                } else if (view.getId() == R.id.driving_leftSpin) {
                    message = "move/leftspin";
                } else if (view.getId() == R.id.driving_rightSpin) {
                    message = "move/rightspin";
                }

                Log.d("msg", message);
                //서버로 메시지 전송하기
                if (message != "")
                    pw.println(MainActivity.member_family + "/" + MainActivity.loginID + "/" + message);
            }
        }).start();
    }

    class MyAsyncTask extends AsyncTask<Integer, String, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            try {

                //socket = new Socket("70.12.228.112", 12345);
                //socket = new Socket("70.12.225.188", 33334);
                socket = Car.socket;
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        void ioWork() {
            is = Car.is;
            isr = Car.isr;
            br = Car.br;
            os = Car.os;
            pw = Car.pw;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
