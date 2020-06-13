package com.lemon.drawheart.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.Arrays;

/**
 * @author lemon92xy
 */
public class HeartView extends View {

    private Paint paint;

    private float[] points;

    private int screenWidth;

    private Keyframes keyframes;

    public HeartView(Context context) {
        this(context, null);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);

        //默认颜色
        int color = Color.RED;
        paint.setColor(color);

        getScreenWh();
    }

    private void getScreenWh() {
        WindowManager windowManager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            // 屏幕宽
            screenWidth = displayMetrics.widthPixels;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStrokeWidth(20);
        canvas.drawPath(getPath(), paint);
        paint.setStrokeWidth(26);
        if (points != null) {
            canvas.drawPoints(points, paint);
        }
    }

    public Path getPath() {
        // 初始化 Path 对象
        Path path = new Path();
        path.addArc(screenWidth / 8f, screenWidth / 4f
                , screenWidth / 2f, screenWidth / 8f * 5f
                , -225, 225);
        path.arcTo(screenWidth / 2f, screenWidth / 4f
                , screenWidth / 8f * 7f, screenWidth / 8f * 5f
                , -180, 225, false);
        path.lineTo(screenWidth / 2f, screenWidth / 8f * 7f);
        path.close();
        return path;
    }

    public void setPath(Path path) {
        keyframes = new Keyframes(path);
    }

    private void setLineProgress(float start, float end) {
        points = keyframes.getRangeValue(start, end);
    }

    public void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(-1F, 1F).setDuration(5200);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setStartDelay(52);
        valueAnimator.addUpdateListener((ValueAnimator animation) -> {
            float currentProgress = (float) animation.getAnimatedValue();
            float startProgress, endProgress;
            startProgress = 1F + currentProgress;
            endProgress = currentProgress;
            if (endProgress < 0) {
                endProgress = 0;
            }
            if (startProgress > 1) {
                startProgress = 1;
            }
            setLineProgress(startProgress, endProgress);
            invalidate();
        });
        valueAnimator.start();
    }

    private static class Keyframes {
        static final float PRECISION = 1f; //精度(数值越小 numPoints 越大)
        int numPoints;
        float[] mData;

        Keyframes(Path path) {
            final PathMeasure pathMeasure = new PathMeasure(path, false);
            final float pathLength = pathMeasure.getLength();
            numPoints = (int) (pathLength / PRECISION) + 1;
            mData = new float[numPoints * 2];
            final float[] position = new float[2];
            int index = 0;
            for (int i = 0; i < numPoints; ++i) {
                final float distance = (i * pathLength) / (numPoints - 1);
                pathMeasure.getPosTan(distance, position, null);
                mData[index] = position[0];
                mData[index + 1] = position[1];
                index += 2;
            }
            numPoints = mData.length;
        }

        /**
         * 拿到start和end之间的x,y数据
         */
        float[] getRangeValue(float start, float end) {
            int startIndex = (int) (numPoints * start);
            int endIndex = (int) (numPoints * end);

            //必须是偶数，因为需要float[]{x,y}这样x和y要配对的
            if (startIndex % 2 != 0) {
                --startIndex;
            }
            if (endIndex % 2 != 0) {
                ++endIndex;
            }
            //根据起止点裁剪
            return startIndex > endIndex ? Arrays.copyOfRange(mData, endIndex, startIndex) : null;
        }
    }

}
