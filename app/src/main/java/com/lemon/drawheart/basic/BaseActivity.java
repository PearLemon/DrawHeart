package com.lemon.drawheart.basic;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * @author lemon92xy
 * 所有Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected BaseActivity activity;

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
     * @return int
     **/
    protected abstract int getLayoutId();

    /**
     * 布局创建之后
     * @param savedInstanceState bundle params
     **/
    protected abstract void afterCreate(Bundle savedInstanceState);
}
