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

public class HeartView extends View {

    private Paint paint;

    private int color;

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
        color = Color.RED;
        paint.setColor(color);

        getScreenWH();
    }

    private void getScreenWH() {
        WindowManager windowManager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            screenWidth = displayMetrics.widthPixels;  // 屏幕宽
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(getPath(), paint);
        if (points != null) {
            canvas.drawPoints(points, paint);
        }
    }

//    /**
//     * 比onDraw先执行
//     * <p>
//     * 一个MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求。
//     * 一个MeasureSpec由大小和模式组成
//     * 它有三种模式：
//     * UNSPECIFIED(未指定),父元素部队自元素施加任何束缚，子元素可以得到任意想要的大小;
//     * EXACTLY(完全)，父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小；
//     * AT_MOST(至多)，子元素至多达到指定大小的值。
//     * <p>
//     * 它常用的三个函数：
//     * 1.static int getMode(int measureSpec):根据提供的测量值(格式)提取模式(上述三个模式之一)
//     * 2.static int getSize(int measureSpec):根据提供的测量值(格式)提取大小值(这个大小也就是我们通常所说的大小)
//     * 3.static int makeMeasureSpec(int size,int mode):根据提供的大小值和模式创建一个测量值(格式)
//     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);   //获取宽的模式
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec); //获取高的模式
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);   //获取宽的尺寸
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec); //获取高的尺寸
//        int width;
//        int height ;
//        if (widthMode == MeasureSpec.EXACTLY) {
//            //如果match_parent或者具体的值，直接赋值
//            width = widthSize;
//        } else {
//            //如果是wrap_content，我们要得到控件需要多大的尺寸
//            width = 520;
//        }
//        //高度跟宽度处理方式一样
//        if (heightMode == MeasureSpec.EXACTLY) {
//            height = heightSize;
//        } else {
//            height = 520;
//        }
//        //保存测量宽度和测量高度
//        setMeasuredDimension(width, height);
//    }

    public Path getPath() {
        Path path = new Path(); // 初始化 Path 对象
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
        valueAnimator.setStartDelay(520);
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
