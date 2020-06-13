package com.lemon.drawheart.basic;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * @author lemon92xy
 * 所有Fragment的基类
 */
public abstract class BaseFragment extends Fragment {

    FragmentActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
