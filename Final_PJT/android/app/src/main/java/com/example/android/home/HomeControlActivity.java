package com.example.android.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android.MainActivity;
import com.example.android.R;
import com.example.android.home.air.AirControlActivity;
import com.example.android.home.alarms.AlarmsService;
import com.example.android.home.light.LightControlActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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

    //**********************************************************************
    //음성인식 추가
    Context cThis;
    String LogTT = "[STT]";

    Intent SttIntent;
    SpeechRecognizer mRecognizer;

    TextToSpeech tts;

    Button btnSttStart;
    EditText txtInMsg;
    EditText txtSystem;


    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Socket socket;
    OutputStream os;
    PrintWriter pw;
    //**********************************************************************


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

        //*****************************************************************
        //음성인식부분 시작
        cThis = this;

        SttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(cThis);
        mRecognizer.setRecognitionListener(listener);



        //음성 출력 생성, 리스너 초기화
        tts = new TextToSpeech(cThis, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        btnSttStart = findViewById(R.id.btn_stt_start);
        btnSttStart.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(":::::::::::::음성인식 시작!");
                if (ContextCompat.checkSelfPermission(cThis, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeControlActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);

                } else {
                    //권한을 허용한 경우
                    try {
                        mRecognizer.startListening(SttIntent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //입력 박스 설정
        txtInMsg = findViewById(R.id.txtInMsg);
        txtSystem = findViewById(R.id.txtSystem);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txtSystem.setText("어플 실행됨::::자동실행" + "\r\n" + txtSystem.getText());
                btnSttStart.performClick();
            }
        }, 1000);

        //stt가 일정 시간이 되면 죽기 떄문에 일정 시간에 한번씩 계속 실행 처리
        // 테스트를 위한거라 실제에선 쓰면 안된다고 하는데 계속 실행상태 유지해야해서 우리는 써야할듯
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                btnSttStart.performClick();
            }
        };

        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
        };
        timer.schedule(timerTask, 0, 5000); // 5초에 한번 버튼 클릭


        //음성인식 (onCreate)      끝
        //******************************************************************


    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            txtSystem.setText("onReadyForSpeech::::::::::::::::" + "\r\n" + txtSystem.getText());
        }

        @Override
        public void onBeginningOfSpeech() {
            txtSystem.setText("지금부터 말하세요:::::::::" + "\r\n" + txtSystem.getText());
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            txtSystem.setText("onBufferReceived::::::::::::::::" + "\r\n" + txtSystem.getText());
        }

        @Override
        public void onEndOfSpeech() {
            txtSystem.setText("onEndOfSpeech:::: 종료됨::::::::::" + "\r\n" + txtSystem.getText());
        }

        @Override
        public void onError(int error) {
            Log.i(LogTT, "ERROR : 천천히 다시 말해주세요1::::::::::::");
            txtSystem.setText("천천히 다시 말해주세요:::::::::::" + "\r\n" + txtSystem.getText());
        }

        @Override
        public void onResults(Bundle results) {
            String key = "";
            Date date=new Date(System.currentTimeMillis());
            SimpleDateFormat sdfNow=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            String formatDate=sdfNow.format(date);

            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            Log.i(LogTT, "입력값 : " + rs[0]);
            txtInMsg.setText(rs[0] + "\r\n" + txtInMsg.getText());

            FunVoiceOrderCheck(rs[0]);



            mRecognizer.startListening(SttIntent); // 음성인식이 계속되는 구문이니 필요에 맞게 쓰길
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.i(LogTT, "onPartialResults:::::::::::");
            txtSystem.setText("onPartialResults:::::::::::" + "\r\n" + txtSystem.getText());
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.i(LogTT, "onEvent::::::::::::");
            txtSystem.setText("onEvent:::::::::::::::" + "\r\n" + txtSystem.getText());
        }
    };

    //*****************************************************************************************************
    //****************************     메시지들 ***********************************************************
    //입력한 음성 메시지 확인 후 동작 처리.
    private void FunVoiceOrderCheck(String VoiceMsg) {

        SpeechAsyncTast speechAsyncTast=new SpeechAsyncTast();
        String msg="";
        String androidId=family+"/"+id;
        if (VoiceMsg.length() < 1) return;

        VoiceMsg = VoiceMsg.replace(" ", ""); //공백 제거

        if (VoiceMsg.indexOf("미리야") > -1) {
            tts.setSpeechRate(0.2f);
            FunVoiceOut("네");
            tts.setSpeechRate(1.0f);
        }

        if (VoiceMsg.indexOf("팬켜") > -1 || VoiceMsg.indexOf("펜켜") > -1) {
            Log.i(LogTT, "메시지 확인 : fan ON");
            //imgViewLight.setImageAlpha(255);
            FunVoiceOut("팬을 켰습니다.");
            msg="FAN_on";
            Log.i(LogTT,"확인:::::"+"air/"+msg+"/"+"phone/" + androidId);
            speechAsyncTast.execute("air/"+msg+"/"+"phone/" + androidId);
        }
        if (VoiceMsg.indexOf("팬꺼") > -1 || VoiceMsg.indexOf("펜꺼") > -1) {
            Log.i(LogTT, "메시지 확인 : fan OFF");
            //imgViewLight.setImageAlpha(50);
            FunVoiceOut("팬을 껐습니다.");
            msg="FAN_off";
            Log.i(LogTT,"확인:::::"+"air/"+msg+"/"+"phone/" + androidId);
            speechAsyncTast.execute("air/"+msg+"/"+"phone/" + androidId);
        }
        //*****************************************************************************************************
    }

    private void FunVoiceOut(String OutMsg) {
        if (OutMsg.length() < 1) {
            return;
        }

        tts.setPitch(1.5f); //1.5톤 올려서
        tts.setSpeechRate(1.0f); //1배속으로  읽기
        tts.speak(OutMsg, TextToSpeech.QUEUE_FLUSH, null);
    }

    //어플이 종료될떄때 완전 제거
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        if (mRecognizer != null) {
            mRecognizer.destroy();
            mRecognizer.cancel();
            mRecognizer = null;
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class SpeechAsyncTast extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground (String...strings){

                socket = AlarmsService.socket;
                Log.d("확인", "socket다음");
                if (socket != null) {
                    speechWork(strings[0]);
                }
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            String msg;
                            try {
                                msg = br.readLine();
                                Log.d("확인", "서버로 부터 수신된 메시지>>" + msg);
                            } catch (IOException e) {
                                try {
                                    is.close();
                                    isr.close();
                                    br.close();
                                    os.close();
                                    pw.close();
                                    socket.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                break;//반복문 빠져나가도록 설정
                            }
                        }
                    }
                });
                t1.start();


            return "";
        }

        void speechWork (String speech) {
            try {
                is = AlarmsService.iowork.getIs();
                isr = AlarmsService.iowork.getIsr();
                br = AlarmsService.iowork.getBr();

                os = socket.getOutputStream();
                pw = new PrintWriter(os, true);
                pw.println(speech);
                pw.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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
