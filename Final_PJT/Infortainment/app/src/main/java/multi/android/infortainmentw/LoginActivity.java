package multi.android.infortainmentw;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import multi.android.infortainmentw.db.MemberVO;
import multi.android.infortainmentw.db.Task;

public class LoginActivity extends AppCompatActivity {
    String id = "";
    String family = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = findViewById(R.id.login_submit);
        final EditText edId = findViewById(R.id.login_id);
        final EditText edpassword = findViewById(R.id.login_pass);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("method", "login");
                map.put("member_id",edId.getText().toString());
                map.put("member_pass",edpassword.getText().toString());
                Task networkTask = new Task();
                networkTask.execute(map);
                while(networkTask.getResult().equals("")){
                    SystemClock.sleep(10);
                }
                Log.d("check1",networkTask.getResult());
                if(!networkTask.getResult().equals("")){
                    Gson gson = new Gson();
                    MemberVO fvo = gson.fromJson(networkTask.getResult(),MemberVO.class);
                    id = fvo.getMember_id();
                    family= fvo.getMember_family();
                    Intent infortainment = new Intent(LoginActivity.this, MainActivity.class);
                    infortainment.putExtra("id", id);
                    infortainment.putExtra("family", family);
                    Toast.makeText(LoginActivity.this, "로그인이 되었습니다.",Toast.LENGTH_LONG).show();
                    startActivity(infortainment);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
