package multi.android
        .infortainmentw;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import multi.android.infortainmentw.control.Control;
import multi.android.infortainmentw.music.MusicFragment;
import multi.android.infortainmentw.music.PlayFragment;
import multi.android.infortainmentw.navi.FindAddress;
import multi.android.infortainmentw.navi.NaviFragment;
import multi.android.infortainmentw.navi.NaviMain;

public class MainActivity extends AppCompatActivity {
    // ================================
    // 프래그먼트
    MapView mapView;
    Control control = new Control();
    MusicFragment musicFragment = new MusicFragment();
    FindAddress findAddress = new FindAddress();
    NaviFragment naviFragment = new NaviFragment();
    PlayFragment playFragment=new PlayFragment();
    NaviMain naviMain = new NaviMain();
    // =================================
    // 소켓, 입출력변수
    Socket socket;
    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    OutputStream os;
    public static PrintWriter pw;

    String lon;
    String lat;
   // String ip = "70.12.224.148";
    //int port = 33336;
    Button btn;
    TMapGpsManager tMapGpsManager;

    String ip = "70.12.224.117";
    int port = 33336;

    String andId;

    //음성 인식추가
    Context cThis;
    String LogTT = "[STT]";

    Intent SttIntent;
    SpeechRecognizer mRecognizer;

    TextToSpeech tts;

    Button btnSttStart;
    EditText txtInMsg;
    EditText txtSystem;

    SpeechAsyncTast speechAsyncTast;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        } else {
            mapView =findViewById(R.id.map);

            setContentView(R.layout.activity_main);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            final FragmentManager fragmentManager;

            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction;
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_control, control);
            transaction.replace(R.id.fragment_music, musicFragment);
            transaction.replace(R.id.navi_frag, naviFragment);
            transaction.commit();


            btn = findViewById(R.id.sendGPS);
           // final Button button = naviFragment.findViewById(R.id.sendGPS);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn.callOnClick();
                }
            });
            //음성인식부분 시작
            cThis = this;

            SttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
            SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(cThis);
            mRecognizer.setRecognitionListener(listener);

            andId="noo";

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

                            mRecognizer.startListening(SttIntent);


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



            new AsyncTask<String, String, String>() {
                String temperature = "";
                String humidity = "";

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
                        if (protocol.equals("temperature")) {
                            temperature = message;
                            control.setTemperature(message);
                        } else if (protocol.equals("humidity")) {
                            humidity = message;
                             control.setHumidity(message);
                            publishProgress(message);
                        }else if(protocol.equals("start")){
                            String[] longlat = message.split(",");
                                lon = longlat[0];
                                lat = longlat[1];
                                Log.d("chat",lon+lat);
                                publishProgress(lon,lat);
                                 Log.d("chat","protocolstart");
                        }else if(protocol.equals("login")){

                            TMapPoint point = tMapGpsManager.getLocation();
                            double lati = point.getLatitude();
                            double longi = point.getLongitude();
                            String sendGPS = "sendGPS/"+lati+","+longi;
                            pw.println(sendGPS);

                        }
                    } catch (NoSuchElementException e) {

                    }
                }

                @Override
                protected void onProgressUpdate(String... values) {

                    TextView tvTemp = findViewById(R.id.temporature);
                    TextView tvHumi = findViewById(R.id.humidity);
                    tvTemp.setText(temporature + "℃");
                    tvHumi.setText(humidity + "％");
                    TextView a = findViewById(R.id.a);
                    TextView b = findViewById(R.id.b);
                    a.setText(lon);
                    b.setText(lat);
                    final Button button = findViewById(R.id.justforNavi);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button.callOnClick();

                        }
                    });



                }
            }.execute();




        }
        //*****************************************************************


    }
    public void resetKm(View v){
        control.setKm(0);
    }


    public void replaceFragmentFindAddress() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.navi_frag, findAddress);
        transaction.commit();
    }

    public void replaceFragmentMap() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.navi_frag, naviFragment);
        transaction.commit();
    }



    //////////////////////////////////
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

            speechAsyncTast=new SpeechAsyncTast();
            speechAsyncTast.execute(rs[0],formatDate);

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
        if (VoiceMsg.length() < 1) return;

        VoiceMsg = VoiceMsg.replace(" ", ""); //공백 제거

        if (VoiceMsg.indexOf("미리야") > -1) {
            tts.setSpeechRate(0.2f);
            FunVoiceOut("네");
            tts.setSpeechRate(1.0f);
        }

        if (VoiceMsg.indexOf("전등켜") > -1 || VoiceMsg.indexOf("불켜") > -1) {
            Log.i(LogTT, "메시지 확인 : 전등 ON");
            //imgViewLight.setImageAlpha(255);
            FunVoiceOut("전등을 켰습니다.");
        }
        if (VoiceMsg.indexOf("전등꺼") > -1 || VoiceMsg.indexOf("불꺼") > -1) {
            Log.i(LogTT, "메시지 확인 : 전등 OFF");
            //imgViewLight.setImageAlpha(50);
            FunVoiceOut("전등을 끕니다.");
        }
        if (VoiceMsg.equals("음악")) {
            tts.speak("음악을 재생합니다.", TextToSpeech.QUEUE_FLUSH, null);
            /*Intent intent = new Intent(this, MusicService.class);
            startService(intent);*/

            Bundle bundle = new Bundle();
            bundle.putInt("position", 0);
            bundle.putSerializable("playlist", MusicFragment.list);
            playFragment.setArguments(bundle);

            FragmentManager fragmentManager;
            fragmentManager = this.getSupportFragmentManager();
            FragmentTransaction transaction;
            transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.fragment_music, playFragment);
            transaction.commit();
            return;
        }
        if (VoiceMsg.equals("음악정지")) {
            tts.speak("음악을 정지합니다.", TextToSpeech.QUEUE_FLUSH, null);
            FragmentManager fragmentManager;
            fragmentManager = this.getSupportFragmentManager();
            FragmentTransaction transaction;
            transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.fragment_music, musicFragment);
            transaction.commit();
            return;
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

    class SpeechAsyncTast extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground (String...strings){
            try {
                socket = new Socket("70.12.227.93", 12345);
                Log.d("확인", "socket다음");
                if (socket != null) {
                    speechWork(strings[0],strings[1]);
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
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }

        void speechWork (String speech,String sysdate) {
            try {
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

                os = socket.getOutputStream();
                pw = new PrintWriter(os, true);
                pw.println("info/" + andId+"/speech/"+speech+"/sysdate/"+sysdate);
                pw.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
