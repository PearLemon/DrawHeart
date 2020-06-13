package com.lemon.drawheart.module;

import android.os.Bundle;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.lemon.drawheart.R;
import com.lemon.drawheart.basic.BaseFragment;
import com.lemon.drawheart.dao.DaoSession;
import com.lemon.drawheart.dao.PresentDao;
import com.lemon.drawheart.entity.Present;
import com.lemon.drawheart.helper.GreenDaoHelper;

import butterknife.BindView;

/**
 * @author lemon92xy
 */
public class LaughFragment extends BaseFragment {

    @BindView(R.id.lottieView)
    LottieAnimationView lottieView;

    @BindView(R.id.tvLaugh)
    TextView tvLaugh;

    public static LaughFragment newInstance(int index) {
        LaughFragment fragment = new LaughFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frg_laugh;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        int index = getArguments() == null ? 0 : getArguments().getInt("index");
        DaoSession daoSession = GreenDaoHelper.getSingleton().getDaoSession();
        PresentDao presentDao = daoSession.getPresentDao();
        Present data = presentDao.queryBuilder().where(PresentDao.Properties.Id.eq(index)).uniqueOrThrow();
        tvLaugh.setText(data.getContent());
        lottieView.setAnimation("girl.json");
    }
}
