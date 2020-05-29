package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.car.Car;
import com.example.android.home.HomeControlActivity;

public class MainActivity extends AppCompatActivity {
    ImageButton main_home_img;
    ImageButton main_car_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_home_img = findViewById(R.id.main_home_img);
        main_car_img = findViewById(R.id.main_car_img);

        main_home_img.setImageResource(R.mipmap.main_home_img);
        main_car_img.setImageResource(R.mipmap.main_car_img);

        main_car_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Car.class);
                startActivity(intent);
            }
        });

        main_home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeControlActivity.class);
                startActivity(intent);
            }
        });
    }
}
