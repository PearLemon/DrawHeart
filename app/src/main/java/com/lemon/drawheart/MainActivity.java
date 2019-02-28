package com.lemon.drawheart;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dk.view.patheffect.PathTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HeartView heartView = findViewById(R.id.drawHeart);
        heartView.setPath(heartView.getPath());
        heartView.startAnimation();

        FlowerView flowerView = findViewById(R.id.flowerView);
        flowerView.startAnimation();

        PathTextView mPathTextView = findViewById(R.id.pathView);
        mPathTextView.init("believe the future");
        mPathTextView.setPaintType(PathTextView.Type.SINGLE);
        mPathTextView.setTextColor(Color.RED);
        mPathTextView.setTextWeight(6);
        mPathTextView.setTextSize(36);
        mPathTextView.setDuration(5200);
    }
}
