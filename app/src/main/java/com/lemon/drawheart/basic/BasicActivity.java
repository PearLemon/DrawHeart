package com.lemon.drawheart.basic;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * 所有Activity的基类
 */
public abstract class BasicActivity extends AppCompatActivity {

    protected BasicActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        afterCreate(savedInstanceState);
    }

    /**
     * 获取布局
     **/
    protected abstract int getLayoutId();

    /**
     * 布局创建之后
     **/
    protected abstract void afterCreate(Bundle savedInstanceState);
}
