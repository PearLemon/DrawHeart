package com.lemon.drawheart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawHeart drawHeart = findViewById(R.id.drawHeart);
        drawHeart.setPath(drawHeart.getPath());
        drawHeart.startAnimation();
    }
}
