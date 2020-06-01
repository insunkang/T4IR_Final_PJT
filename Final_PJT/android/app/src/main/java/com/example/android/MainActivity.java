package com.example.android;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.android.car.Car;
import com.example.android.member.MemberVO;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//import com.example.android.home.HomeControlActivity;

public class MainActivity extends AppCompatActivity {
    ImageButton main_home_img;
    ImageButton main_car_img;
    Button btn_login_submit;
    Button btn_login_register;
    EditText login_id;
    EditText login_pass;
    String res="";
    LinearLayout loginlayout;
    View view;
    String code;
    String phoneNo;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    public static String loginID;
    public static String member_family;

    Intent intentCar;
    Intent intentHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_home_img = findViewById(R.id.main_home_img);
        main_car_img = findViewById(R.id.main_car_img);
        btn_login_submit = findViewById(R.id.login_submit);
        btn_login_register = findViewById(R.id.login_register);
        loginlayout = findViewById(R.id.loginlayout);

        main_home_img.setImageResource(R.mipmap.main_home_img);
        main_car_img.setImageResource(R.mipmap.main_car_img);
        main_car_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginID!=null){
                    startActivity(intentCar);
                }else{
                    Toast.makeText(MainActivity.this, "로그인 할 수 없습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_id = findViewById(R.id.login_id);
                login_pass = findViewById(R.id.login_pass);
                String id = login_id.getText().toString();
                String pass= login_pass.getText().toString();
                MemberVO vo = new MemberVO(id, pass);

                HttpSelect task = new HttpSelect();
                task.execute(vo);
                res= task.getResponse();
                int count =0;
                while(res==""||res==null){
                    SystemClock.sleep(10);
                    res= task.getResponse();
                    count ++;
                    if(count>300){
                        Toast.makeText(MainActivity.this, "ID,PASSWORD를 확인하세요.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if(res!=""||res!=null) {

                    Log.d("check1",res);
                    Gson gson = new Gson();
                    MemberVO fvo = gson.fromJson(res, MemberVO.class);
                    String fvoMember_family = fvo.getMember_family();
                    String fvoMember_pass = fvo.getMember_pass();
                    String fvoMember_id = fvo.getMember_id();

                    if (fvoMember_id.equals(id) && fvoMember_pass.equals(pass)) {
                        loginlayout.setVisibility(View.INVISIBLE);
                        loginID = fvoMember_id;
                        member_family = fvoMember_family;
                        intentCar = new Intent(MainActivity.this, Car.class);
                        intentCar.putExtra("loginID", loginID);
                        intentCar.putExtra("member_family", member_family);
                        Toast.makeText(MainActivity.this, "로그인이 되었습니다.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(v.getContext());
                view = LayoutInflater.from(v.getContext()).inflate(R.layout.register_box, null, false);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
            }
        });


    }

    // android로 family user 회원가입
    class HttpInsert extends AsyncTask<MemberVO, Void, String> {

        @Override
        protected String doInBackground(MemberVO... items) {
            URL url = null;
            JSONObject object = new JSONObject();
            String data = "";
            try {
                object.put("member_id", items[0].getMember_id());
                object.put("member_pass", items[0].getMember_pass());
                object.put("member_family", items[0].getMember_family());

                url = new URL("http://"+Variable.springIP+":8088/miri/member/insert");

                OkHttpClient client = new OkHttpClient();
                String json = object.toString();
                Log.d("msg", json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(MediaType.parse("application/json"), json))
                        .build();

                Response response = client.newCall(request).execute();
                data = response.body().string();
                Log.d("msg", data);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return data;
        }
        /*main_home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeControlActivity.class);
                startActivity(intent);
            }
        });*/

    }

    // android로 family user 로그인 하기 위해 DB에 있는지 ID를 조회하고 있으면 true를 리턴
    class HttpSelect extends AsyncTask<MemberVO, Void, String> {
        URL url = null;
        BufferedReader br = null;
        String result;
        JSONObject object = new JSONObject();
        String response="";
        @Override
        protected String doInBackground(MemberVO... items) {
            try {
                object.put("member_id", items[0].getMember_id());
                object.put("member_pass", items[0].getMember_pass());

                String path = "http://"+Variable.springIP+":8088/miri/member/select";
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
                Log.d("msg", result);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setResponse(result+"");
            return result;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }

    public void clickSendSMS(View v) {
        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        code = "";
        for (int i = 0; i < 6; i++) {
            code += (int) (Math.random() * 10);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1000);
        } else {
            EditText et = view.findViewById(R.id.inputPhoneNumber);
            phoneNo = et.getText().toString();
        }
        try {
            //전송
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, code, null, null);
            Toast.makeText(getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Button btn = view.findViewById(R.id.btnAuthKeyNumber);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.findViewById(R.id.btnSendSMS).setBackgroundColor(getColor(R.color.white));
            btn.setBackgroundColor(getColor(R.color.blue));
        }
        btn.setEnabled(true);
    }

    public void clickCheckAuth(View v) {
        EditText et = view.findViewById(R.id.inputAuthKeyNumber);
        String key = et.getText().toString();
        final Button ButtonRegSubmit = (Button) view.findViewById(R.id.button_register_submit);
        ButtonRegSubmit.setVisibility(View.INVISIBLE);
        if (key.equals(code)) {
            //db작업
            final EditText regMemberId = (EditText) view.findViewById(R.id.register_member_id);
            final EditText regMemberPass = (EditText) view.findViewById(R.id.register_member_pass);
            final EditText regMemberFamily = (EditText) view.findViewById(R.id.register_member_family);
            ButtonRegSubmit.setText("회원가입");

            ButtonRegSubmit.setVisibility(View.VISIBLE);
            ButtonRegSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = regMemberId.getText().toString();
                    String pass = regMemberPass.getText().toString();
                    String family = regMemberFamily.getText().toString();
                    MemberVO vo = new MemberVO(id, pass, family);

                    HttpInsert task = new HttpInsert();
                    task.execute(vo);
                    Toast.makeText(MainActivity.this, "회원가입이 완료되었습니다.",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
        }
    }

}
