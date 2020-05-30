package com.example.android.car;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androdocs.httprequest.HttpRequest;
import com.example.android.R;
import com.example.android.driving.Driving_Info;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class Car extends AppCompatActivity {
    ImageButton car_handle_img;
    ToggleButton car_lock;
    ToggleButton car_air;
    ImageButton car_navi;
    ToggleButton car_seat;
    TextView edit_oil;
    ProgressBar progressBar;
    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Socket socket;
    OutputStream os;
     public static PrintWriter pw;

    /* ==========================날씨========================== */
    String CITY = "seoul,KR";
    String API = "1d05f37dc31eab19ba9ee3c97411cf25";
    String LAT = "";
    String LON = "";
    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        pw.println("login/good");
        carMap carMap = new carMap();
        FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.car_map, carMap);
        transaction.commit();
        MapAsyncTask mapAsyncTask = new MapAsyncTask();
        mapAsyncTask.execute(10,20);
        car_handle_img = findViewById(R.id.car_handle_img);
        car_lock = findViewById(R.id.car_lock);
        car_air = findViewById(R.id.car_air);
        car_navi = findViewById(R.id.car_navi);
        car_seat = findViewById(R.id.car_seat);
        progressBar = findViewById(R.id.oil_progressbar);
        edit_oil = findViewById(R.id.car_oil);

        car_handle_img.setImageResource(R.drawable.car_handle_img);
        car_navi.setImageResource(R.drawable.car_navi);
        edit_oil.setText("66");
        progressBar.setProgress(Integer.parseInt(edit_oil.getText().toString()));


        car_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(car_lock.isChecked()){
                    car_lock.setBackgroundDrawable(getResources().getDrawable(R.drawable.car_lock_open));
                    Toast.makeText(Car.this,"UNLOCK",Toast.LENGTH_SHORT).show();
                }else{
                    car_lock.setBackgroundDrawable(getResources().getDrawable(R.drawable.car_lock_close));
                    Toast.makeText(Car.this,"LOCK",Toast.LENGTH_SHORT).show();
                }
            }
        });

        car_air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(car_air.isChecked()){
                    car_air.setBackgroundDrawable(getResources().getDrawable(R.drawable.car_air_open));
                    Toast.makeText(Car.this,"FAN ON",Toast.LENGTH_SHORT).show();
                }else{
                    car_air.setBackgroundDrawable(getResources().getDrawable(R.drawable.car_air_close));
                    Toast.makeText(Car.this,"FAN OFF",Toast.LENGTH_SHORT).show();
                }
            }
        });

        car_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(car_seat.isChecked()){
                    car_seat.setBackgroundDrawable(getResources().getDrawable(R.drawable.car_seat_open));
                    Toast.makeText(Car.this,"SEAT ON",Toast.LENGTH_SHORT).show();
                }else{
                    car_seat.setBackgroundDrawable(getResources().getDrawable(R.drawable.car_seat_close));
                    Toast.makeText(Car.this,"SEAT OFF",Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        new weatherTask().execute();
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
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            //String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?lat=" + LAT + "&lon=" + LON + "&units=metric&appid=" + API);
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
    class MapAsyncTask extends AsyncTask<Integer,String,String> {

        @Override
        protected String doInBackground(Integer... integers) {
            Log.d("chat", "접속"
                    );
            try {

                //socket = new Socket("70.12.228.112", 12345);
                //socket = new Socket("70.12.225.188", 33334);
                socket = new Socket("70.12.224.148", 33334);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String lon = null;
        String lat = null;
        private void filteringMsg(String msg) {
            try {
                StringTokenizer token = new StringTokenizer(msg, "/");
                String protocol = token.nextToken();
                String message = token.nextToken();

                System.out.println("프로토콜:" + protocol + ",메시지:" + message);
                 if(protocol.equals("start")){
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
            } catch (NoSuchElementException e) {

            }
        }
        @Override
        protected void onProgressUpdate(String... values) {

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
    }
}

