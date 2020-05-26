package com.example.android.car;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;
import com.example.android.driving.Driving_Info;

public class Car extends AppCompatActivity {
    ImageButton car_handle_img;
    ToggleButton car_lock;
    ImageButton car_air;
    ImageButton car_navi;
    ImageButton car_seat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car2);

        car_handle_img = findViewById(R.id.car_handle_img);
        car_lock = findViewById(R.id.car_lock);
        car_air = findViewById(R.id.car_air);
        car_navi = findViewById(R.id.car_navi);
        car_seat = findViewById(R.id.car_seat);

        car_handle_img.setImageResource(R.drawable.car_handle_img);
        car_air.setImageResource(R.drawable.car_air);
        car_navi.setImageResource(R.drawable.car_navi);
        car_seat.setImageResource(R.drawable.car_seat);

        car_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(car_lock.isChecked()){
                    Log.d("lock","check : true");
                    car_lock.setBackgroundDrawable(getResources().getDrawable(R.drawable.car_lock_open));
                }else{
                    Log.d("lock","check : false");
                    car_lock.setBackgroundDrawable(getResources().getDrawable(R.drawable.car_lock_close));
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

    }
}

