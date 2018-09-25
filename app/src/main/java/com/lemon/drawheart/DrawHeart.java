package com.lemon.drawheart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

public class DrawHeart extends View {

    private Paint paint;

    private int color;

    private float[] points;

    private Keyframes keyframes;

    private ValueAnimator valueAnimator;

    public DrawHeart(Context context) {
        this(context, null);
    }

    public DrawHeart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawHeart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        color = Color.RED;
        paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        paint.setColor(Color.GRAY);
//        canvas.drawPath(getPath(), paint);
        paint.setColor(color);
        if(points != null) {
            canvas.drawPoints(points, paint);
        }
    }

//    public Path getPath() {
//        Path path = new Path(); // 初始化 Path 对象
//        path.addArc(200, 200, 400, 400, -225, 225);
//        path.arcTo(400, 200, 600, 400, -180, 225, false);
//        path.lineTo(400, 542);
//        path.close();
//        return path;
//    }

    public Path getPath(){
        Path path = new Path();
        path.moveTo(200,200);
        path.lineTo(600, 200);
        path.lineTo(350, 400);
        path.close();
        return path;
    }

    public void setPath(Path path){
        keyframes = new Keyframes(path);
    }

    private void setLineProgress(float start, float end) {
        points = keyframes.getRangeValue(start, end);
    }

    public void startAnimation() {
        valueAnimator = ValueAnimator.ofFloat(-1F, 1F).setDuration(4000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setStartDelay(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
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
            }
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
