package com.lemon.drawheart.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lemon.drawheart.R;

import java.util.Random;

public class FlowerView extends FrameLayout {

    private PointF startPoint;

    private PointF endPoint;

    private int screenWidth;

    private int screenHeight;

    private Drawable[] drawables;

    private int flowerNumber = 0;

    public FlowerView(@NonNull Context context) {
        this(context, null);
    }

    public FlowerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        getScreenWH();
    }

    private void init() {
        drawables = new Drawable[2];
        drawables[0] = getResources().getDrawable(R.mipmap.flower);
        drawables[1] = getResources().getDrawable(R.mipmap.heart);
    }

    private void getScreenWH() {
        WindowManager windowManager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            screenWidth = displayMetrics.widthPixels;  // 屏幕宽
            screenHeight = displayMetrics.heightPixels;  // 屏幕高
        }
        startPoint = new PointF(screenWidth / 2f - 50, screenHeight);
        endPoint = new PointF(screenWidth / 2f - 50, 0);
    }

    public void startAnimation() {
        for (int i = 0; i < 32; i++) {
            addFlower();
            flowerNumber++;
        }
    }

    public void addFlower() {
        ImageView flower = new ImageView(getContext());
        flower.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        flower.setBackground(drawables[new Random().nextInt(drawables.length)]);
        flower.setX(startPoint.x);
        flower.setY(startPoint.y);
        addView(flower);
        toAnimation(flower);
    }

    private void toAnimation(final ImageView flower) {
        final AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(flower, "alpha", 1.0f, 0.52f);
        alphaAnim.setDuration(520);

        ObjectAnimator scaleAnimX = ObjectAnimator.ofFloat(flower, "scaleX", 0.78f, 1.0f);
        ObjectAnimator scaleAnimY = ObjectAnimator.ofFloat(flower, "scaleY", 0.52f, 1.0f);
        scaleAnimX.setDuration(3200);
        scaleAnimY.setDuration(3200);

        final ValueAnimator animator = ValueAnimator.ofObject(new MyTypeEvaluator(getPoint(0), getPoint(1)), endPoint, startPoint);
        animator.setDuration(4000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener((ValueAnimator animation) -> {
            PointF pointF = (PointF) animation.getAnimatedValue();
            flower.setX(pointF.x);
            flower.setY(pointF.y);
        });

        //  animator.start();
        animatorSet.play(animator);
        animatorSet.play(scaleAnimX).with(scaleAnimY).before(alphaAnim);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(flower);
                flowerNumber--;
                if (flowerNumber == 0) {
                    startAnimation();
                }
            }
        });
        animatorSet.start();
    }

    private PointF getPoint(int i) {
        PointF pointF = new PointF();
        pointF.x = new Random().nextFloat() * screenWidth;
        if (i == 0) {
            pointF.y = new Random().nextFloat() * 0.5f * screenHeight;
        } else {
            pointF.y = (new Random().nextFloat() * 0.5f * screenHeight) + 0.5f * screenHeight;
        }
        return pointF;
    }


    class MyTypeEvaluator implements TypeEvaluator<PointF> {

        private PointF pointF1, pointF2;

        MyTypeEvaluator(PointF pointF1, PointF pointF2) {
            this.pointF1 = pointF1;
            this.pointF2 = pointF2;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            //三阶贝塞尔曲线
            //B(t) = P0 * (1-t)^3 + 3 * P1 * (1-t)^2 + 3 * P2 * t^2 * (1-t) + P3 * t^3  ，其中 0 <= t <= 1
            float timeLeft = 1.0f - fraction;
            PointF pointF = new PointF();//结果
            pointF.x = timeLeft * timeLeft * timeLeft * (startValue.x)
                    + 3 * timeLeft * timeLeft * fraction * (pointF1.x)
                    + 3 * timeLeft * fraction * fraction * (pointF2.x)
                    + fraction * fraction * fraction * (endValue.x);

            pointF.y = timeLeft * timeLeft * timeLeft * (startValue.y)
                    + 3 * timeLeft * timeLeft * fraction * (pointF1.y)
                    + 3 * timeLeft * fraction * fraction * (pointF2.y)
                    + fraction * fraction * fraction * (endValue.y);
            return pointF;

        }
    }
}
