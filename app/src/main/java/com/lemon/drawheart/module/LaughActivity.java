package com.lemon.drawheart.module;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lemon.drawheart.R;
import com.lemon.drawheart.basic.BaseActivity;
import com.lemon.drawheart.dao.DaoSession;
import com.lemon.drawheart.dao.PresentDao;
import com.lemon.drawheart.helper.GreenDaoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author lemon92xy
 */
public class LaughActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private List<Fragment> fragmentList;

    private int currentItem = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.aty_laugh;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        DaoSession daoSession = GreenDaoHelper.getSingleton().getDaoSession();
        PresentDao dataDao = daoSession.getPresentDao();
        int count = (int) dataDao.count();
        fragmentList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            fragmentList.add(LaughFragment.newInstance(i));
        }
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(6);
        viewPager.setCurrentItem(currentItem);
    }
}
