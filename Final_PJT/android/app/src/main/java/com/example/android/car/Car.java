package com.example.android.car;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androdocs.httprequest.HttpRequest;
import com.example.android.R;
import com.example.android.Variable;
import com.example.android.driving.Driving_Info;
import com.example.android.member.MemberVO;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Car extends AppCompatActivity {
    private String loginID;
    private String member_family;
    Button car_handle_img;
    ToggleButton car_lock;
    ToggleButton car_air;
    Button car_navi;
    Button car_seat;
    TextView edit_oil;
    ProgressBar progressBar;
    MemberVO fvo;
    /* ==========================날씨========================== */
    String CITY = "seoul,KR";
    private static String API = "1d05f37dc31eab19ba9ee3c97411cf25";
    private static String LAT;
    private static String LON;
    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;

    StateTask stateTask;
    public static InputStream is;
    public static InputStreamReader isr;
    public static BufferedReader br;
    public static Socket socket;
    public static OutputStream os;
    public static PrintWriter pw;

    stateSelect carStateTask;

    ImageView car_lock_imgView;
    ImageView car_air_imgView;
    ImageView car_seat_imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        carMap carMap = new carMap();
        FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.car_map, carMap);
        transaction.commit();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        car_handle_img = findViewById(R.id.car_handle_img);
        car_lock = findViewById(R.id.car_lock);
        car_air = findViewById(R.id.car_air);
        car_navi = findViewById(R.id.car_navi);
        car_seat = findViewById(R.id.car_seat);
        progressBar = findViewById(R.id.oil_progressbar);
        edit_oil = findViewById(R.id.car_oil);


        car_lock_imgView = findViewById(R.id.car_lock_imgView);
        car_air_imgView = findViewById(R.id.car_air_imgView);
        car_seat_imgView = findViewById(R.id.car_seat_imgView);

        //car_handle_img.setImageResource(R.drawable.car_handle_img);
        //car_navi.setImageResource(R.drawable.car_navi);

        car_air.setBackgroundColor(getResources().getColor(R.color.trans));
        car_lock.setBackgroundColor(getResources().getColor(R.color.trans));


        Bundle extras = getIntent().getExtras();
        loginID = extras.getString("loginID");
        member_family = extras.getString("member_family");
        Log.d("msg","로그인 아이디:::"+loginID);
        Log.d("msg","해당 fam:::"+member_family);

        //시동 버튼 눌렀을 때
        car_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(car_lock.isChecked()){
                    car_lock_imgView.setBackground(getResources().getDrawable(R.drawable.car_unlock));
                    Toast.makeText(Car.this,"UNLOCK",Toast.LENGTH_SHORT).show();
                    pw.println(member_family+"/"+loginID+"/control/engineOn");
                }else{
                    car_lock_imgView.setBackground(getResources().getDrawable(R.drawable.car_lock));
                    Toast.makeText(Car.this,"LOCK",Toast.LENGTH_SHORT).show();
                    pw.println(member_family+"/"+loginID+"/control/engineOff");

                }
            }
        });
        //에어컨 버튼 눌렀을 때
        car_air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carStateTask = new stateSelect();
                MemberVO vo = new MemberVO(loginID);
                carStateTask.execute(vo);
                while(carStateTask.getResponse().equals("")){
                    SystemClock.sleep(10);
                }
                Gson gson = new Gson();
                MemberVO fvo = gson.fromJson(carStateTask.getResponse(), MemberVO.class);
                String arr[] = fvo.getMember_car_state().split("/");
                String air=arr[1];

                if(car_air.isChecked()){
                    car_air_imgView.setBackground(getResources().getDrawable(R.drawable.car_air_open));
                    //car_air.setBackgroundDrawable(getResources().getDrawable(R.drawable.car_air_open));
                    Toast.makeText(Car.this,"FAN ON",Toast.LENGTH_SHORT).show();
                    pw.println(member_family+"/"+loginID+"/control/airOn/"+air);
                }else{
                    car_air_imgView.setBackground(getResources().getDrawable(R.drawable.car_air_close));
                    //car_air.setBackgroundDrawable(getResources().getDrawable(R.drawable.car_air_close));
                    Toast.makeText(Car.this,"FAN OFF",Toast.LENGTH_SHORT).show();
                    pw.println(member_family+"/"+loginID+"/control/airOff/"+air);
                }
            }
        });
        //시트 버튼 눌렀을 때
        car_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String,String,String>(){

                    @Override
                    protected String doInBackground(String... strings) {
                        publishProgress();
                        SystemClock.sleep(1000);
                        publishProgress();
                        return null;
                    }
                    @Override
                    protected void onProgressUpdate(String... values) {
                        super.onProgressUpdate(values);
                        if(car_seat_imgView.getVisibility()==View.VISIBLE){
                            car_seat_imgView.setVisibility(View.INVISIBLE);
                        } else {
                            car_seat_imgView.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        car_seat_imgView.setVisibility(View.VISIBLE);
                        carStateTask = new stateSelect();
                        MemberVO vo = new MemberVO(loginID);
                        carStateTask.execute(vo);
                        while(carStateTask.getResponse().equals("")){
                            SystemClock.sleep(10);
                        }
                        Gson gson = new Gson();
                        MemberVO fvo = gson.fromJson(carStateTask.getResponse(), MemberVO.class);
                        String arr[] = fvo.getMember_car_state().split("/");
                        String seat=arr[3];
                        pw.println(member_family+"/"+loginID+"/control/seatSetting/"+seat);
                    }
                }.execute();
            }
        });
        //운전하기 버튼 눌렀을 때
        car_handle_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Car.this, Driving_Info.class);
                startActivity(intent);
            }
        });

        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);

        stateTask = new StateTask();
        stateTask.execute(10, 20);

        carStateTask = new stateSelect();
        MemberVO vo = new MemberVO(loginID);
        carStateTask.execute(vo);
        while(carStateTask.getResponse().equals("")){
            SystemClock.sleep(10);
        }
        Gson gson = new Gson();
        fvo = gson.fromJson(carStateTask.getResponse(), MemberVO.class);

        Log.d("check1",carStateTask.getResponse());
        if(fvo.getMember_car_state()==null){
            fvo.setMember_car_state("airconditioner/0/seat/0/lat/37.4990887607019/lon/127.01712430744908");
        }
        String arr[] = fvo.getMember_car_state().split("/");
        LAT = arr[5];
        LON = arr[7];
        new weatherTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /* Showing the ProgressBar, Making the main design GONE */
            //findViewById(R.id.loader).setVisibility(View.VISIBLE);
            //findViewById(R.id.mainContainer).setVisibility(View.GONE);
            //findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {
            String response="";
            Log.d("msg","weather LAT::::"+LAT);
            Log.d("msg","weather LON::::"+LON);
            if(LAT.equals("")&&LAT==null){
                response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            }else{
                response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?lat=" + LAT + "&lon=" + LON + "&units=metric&appid=" + API);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");
                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");
                String address = jsonObj.getString("name") + ", " + sys.getString("country");
                /* Populating extracted data into our views */
                addressTxt.setText(address);
                updated_atTxt.setText(updatedAtText);
                statusTxt.setText(weatherDescription.toUpperCase());
                tempTxt.setText(temp);
                sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                windTxt.setText(windSpeed);
                pressureTxt.setText(pressure);
                humidityTxt.setText(humidity);
                /* Views populated, Hiding the loader, Showing the main design */
                //findViewById(R.id.loader).setVisibility(View.GONE);
                //findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                //findViewById(R.id.loader).setVisibility(View.GONE);
                //findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }
        }
    }
    class StateTask extends AsyncTask<Integer,String,String> {
        String lon="";
        String lat="";
        @Override
        protected String doInBackground(Integer... integers) {
            try {
                socket = new Socket(Variable.carServerIP, 33334);
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
                                if(msg==null){
                                    continue;
                                }
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
        void ioWork(){
            try {
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                os = socket.getOutputStream();
                pw = new PrintWriter(os,true);
                pw.println(member_family+"/"+loginID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            TextView lo = findViewById(R.id.lo);
            TextView la = findViewById(R.id.la);
            lo.setText(lon);
            la.setText(lat);
            final Button button = findViewById(R.id.carLoc);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.callOnClick();
                }
            });
        }

        void filteringMsg(String Msg){
            StringTokenizer st = new StringTokenizer(Msg,"/");
            String protocol = st.nextToken();
            final String message = st.nextToken();
            if(protocol.equals("Oil")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(Integer.parseInt(message));
                        edit_oil.setText(message+"%");
                    }
                });
            }else if(protocol.equals("start")){
                String[] longlat = message.split(",");
                lon = longlat[0];
                lat = longlat[1];
                Log.d("chat",lon+lat);
                publishProgress(lon,lat);
                Log.d("chat","protocolstart");
            }else if(protocol.equals("sendGPS")){
                String[] longlat = message.split(",");
                lon = longlat[0];
                lat = longlat[1];
                Log.d("chat",lon+lat);
                publishProgress(lon,lat);
            }

        }
    }
    // 상태값 조회 - 스프링으로 연결
    class stateSelect extends AsyncTask<MemberVO, Void, String> {
        URL url = null;
        BufferedReader br = null;
        String result;
        JSONObject object = new JSONObject();
        String response="";
        @Override
        protected String doInBackground(MemberVO... items) {
            try {
                object.put("member_id", items[0].getMember_id());

                String path = "http://"+Variable.carServerIP+":8088/miri/state/select";
                url = new URL(path);

                OkHttpClient client = new OkHttpClient();
                String json = object.toString();
                Log.d("msg", json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(MediaType.parse("application/json"), json))
                        .build();

                Response response = client.newCall(request).execute();
                result = response.body().string();
                Log.d("msg", "result::::"+result);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setResponse(result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        public void setResponse(String response) {
            this.response = response;
        }

        public String getResponse() {
            return response;
        }
    }
}

