package com.lemon.drawheart.module;

import android.content.Intent;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;

import android.os.Bundle;
import android.view.View;

import com.dk.view.patheffect.PathTextView;
import com.lemon.drawheart.R;
import com.lemon.drawheart.basic.BasicActivity;
import com.lemon.drawheart.view.FlowerView;
import com.lemon.drawheart.view.HeartView;

public class MainActivity extends BasicActivity {

    @BindView(R.id.heartView)
    HeartView heartView;

    @BindView(R.id.flowerView)
    FlowerView flowerView;

    @BindView(R.id.pathView1)
    PathTextView pathView1;

    @BindView(R.id.pathView2)
    PathTextView pathView2;

    @Override
    protected int getLayoutId() {
        return R.layout.aty_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        heartView.setPath(heartView.getPath());
        heartView.startAnimation();

        flowerView.startAnimation();

        pathView1.init("cherish the present");
        setCommon(pathView1);

        pathView2.init("believe the future");
        setCommon(pathView2);

        heartView.setOnClickListener((View v) -> {
            startActivity(new Intent(MainActivity.this, LaughActivity.class));
        });
    }

    public void setCommon(PathTextView pathTextView) {
        pathTextView.setPaintType(PathTextView.Type.SINGLE);
        pathTextView.setTextColor(Color.RED);
        pathTextView.setTextWeight(6);
        pathTextView.setTextSize(36);
        pathTextView.setDuration(5200);
    }
}
