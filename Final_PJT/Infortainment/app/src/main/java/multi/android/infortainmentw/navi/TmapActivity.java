package multi.android.infortainmentw.navi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.skt.Tmap.TMapTapi;

import multi.android.infortainmentw.R;

public class TmapActivity extends AppCompatActivity  {


    Button btn ;
    float x ;
    float y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmap);
        btn =findViewById(R.id.goCom);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyThread().start();
            }
        });

    }
    class MyThread extends Thread{
        public void run(){
            TMapTapi tMapTapi = new TMapTapi(TmapActivity.this);
            tMapTapi.setSKTMapAuthentication("l7xxfce37b4d926e4607a7d14ba4bba09475");
            boolean isTmapApp = tMapTapi.isTmapApplicationInstalled();
            if (isTmapApp==true){

//                tMapTapi.invokeGoHome();
                tMapTapi.invokeNavigate(null,x,y,0,true);
                tMapTapi.invokeNavigate("T타워", 126.984098f, 37.566385f, 0, true);
            }
        }
    }


}
