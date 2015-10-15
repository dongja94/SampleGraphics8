package com.example.dongja94.samplegraphics;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by dongja94 on 2015-10-15.
 */
public class MyView extends View {
    public MyView(Context context) {
        this(context, null);
    }


    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyView);
            Drawable d = ta.getDrawable(R.styleable.MyView_image);
            int id = ta.getResourceId(R.styleable.MyView_image, -1);
            if (id != -1) {
                mBitmap = BitmapFactory.decodeResource(getResources(), id);
            }

            ta.recycle();
        }
    }

    GestureDetector mDetector;
    ScaleGestureDetector mScaleDetector;

    Paint mPaint;
    private void init() {
        mPaint = new Paint();
        makeLinePoint();
        makePath();
        makeBitmap();

        mM = new Matrix();
        mM.reset();

        mDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                mM.postTranslate(-distanceX, -distanceY);
                invalidate();
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                mM.postScale(2, 2, x, y);
                invalidate();
                return true;
            }
        });

        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener(){
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float factor = detector.getScaleFactor();
                float x = detector.getFocusX();
                float y = detector.getFocusY();
                mM.postScale(factor,factor, x, y);
                invalidate();
                return true;
            }
        });
    }

    private static final float LINE_LENGTH = 300;
    private static final float INTERVAL = 10;
    private int lineCount = (int)(LINE_LENGTH / INTERVAL + 1);
    private float[] points = new float[lineCount * 2 * 2];

    private void makeLinePoint() {
        for (int i = 0; i < lineCount; i++) {
            points[i * 4] = 0;
            points[i * 4 + 1] = i * INTERVAL;
            points[i * 4 + 2] = LINE_LENGTH - i * INTERVAL;
            points[i * 4 + 3] = 0;
        }
    }

    Path mPath;

    Path mTextPath;
    Path arrowPath;

    private void makePath() {
        mPath = new Path();
        mPath.moveTo(200, 200);
        mPath.lineTo(100, 100);
        mPath.lineTo(300, 100);
        mPath.lineTo(400, 200);
        mPath.lineTo(300, 300);
        mPath.lineTo(100, 300);

        mPath.moveTo(300, 400);
        mPath.quadTo(100, 600, 200, 800);

        mTextPath = new Path();

        mTextPath.addCircle(400, 400, 100, Path.Direction.CW);

        arrowPath = new Path();
        arrowPath.moveTo(0,0);
        arrowPath.lineTo(-5, -5);
        arrowPath.lineTo(0, -5);
        arrowPath.lineTo(5, 0);
        arrowPath.lineTo(0, 5);
        arrowPath.lineTo(-5,5);
    }

    Bitmap mBitmap;
    Matrix mMatrix;

    float[] vertics = {100, 400, 200, 500, 300, 500, 400, 400,
                        100, 600, 200, 800, 300, 700, 400, 600};

    private void makeBitmap() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.photo1);
        mMatrix = new Matrix();
        mMatrix.reset();
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mBitmap.getWidth() + getPaddingLeft() + getPaddingRight();
        int height = mBitmap.getHeight() + getPaddingTop() + getPaddingBottom();

        width = resolveSize(width, widthMeasureSpec);
        height = resolveSize(height, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    int mX, mY;

    Matrix mM;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = right - left - getPaddingLeft() - getPaddingRight();
        mX = getPaddingLeft() + (width - mBitmap.getWidth()) / 2;
        int height = bottom - top - getPaddingBottom() - getPaddingTop();
        mY = getPaddingTop() + (height - mBitmap.getHeight()) / 2;

        mM.setTranslate(mX,mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean consumed = mDetector.onTouchEvent(event);
        consumed = consumed || mScaleDetector.onTouchEvent(event);
        return consumed || super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawColorFilter(canvas);
    }

    private void drawColorFilter(Canvas canvas) {
        canvas.drawColor(Color.DKGRAY);
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorFilter filter = new ColorMatrixColorFilter(cm);
        mPaint.setColorFilter(filter);
        canvas.drawBitmap(mBitmap, mM, mPaint);
    }

    private void drawShader(Canvas canvas) {
        int[] colors = {Color.RED, Color.GREEN, Color.BLUE};
        float[] positions = {0, 0.3f, 1};
        Shader shader = new LinearGradient(100, 100, 300, 300, colors, positions, Shader.TileMode.REPEAT);
        mPaint.setShader(shader);

        canvas.drawRect( 0, 0, 300, 300, mPaint);
//        canvas.drawRect(100, 100, 300, 300, mPaint);

        shader = new RadialGradient(200, 500, 100, Color.RED, Color.BLUE, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        canvas.drawCircle(200, 500, 100, mPaint);

        int[] sweeps = {Color.RED, Color.BLUE, Color.RED};
        shader = new SweepGradient(200, 800, sweeps, null);
        mPaint.setShader(shader);
        canvas.drawCircle( 200, 800, 100, mPaint);
    }


    private void drawPathEffect(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);

        float[] intervals = {20, 10, 30, 10, 10, 10};
//        PathEffect effect = new DashPathEffect(intervals, 10);
        PathEffect effect = new PathDashPathEffect(arrowPath, 10, 0, PathDashPathEffect.Style.ROTATE);
        mPaint.setPathEffect(effect);
        canvas.drawPath(mPath, mPaint);
    }

    private void drawStroke(Canvas canvas) {
        mPaint.setColor(Color.BLUE);

        canvas.drawRect(100, 100, 300, 300, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(100, 400, 300, 600, mPaint);

        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawRect(100, 700, 300, 900, mPaint);
    }

    private void drawBitmap(Canvas canvas) {
//        mMatrix.setTranslate(100, 100);
//        mMatrix.postRotate(45, 100, 100);
        mMatrix.setScale(0.5f, -0.5f, mBitmap.getWidth() /2, mBitmap.getHeight()/2);
        mMatrix.postTranslate(100, 100);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);

        canvas.drawBitmapMesh(mBitmap, 3, 1, vertics, 0, null, 0, mPaint);
    }

    String mText = "Hello, Android!";

    int hOffset = 0;

    private void drawText(Canvas canvas) {

        canvas.drawColor(Color.TRANSPARENT);

        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(40f);
        canvas.drawText(mText, 0, 100, mPaint);

        canvas.drawTextOnPath(mText, mTextPath, hOffset, 0, mPaint);
        hOffset+=5;

        invalidate();
    }

    private void drawPath(Canvas canvas) {
        mPaint.setColor(Color.BLUE);
        canvas.drawPath(mPath,mPaint);
    }

    private void drawRect(Canvas canvas) {
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(100, 100, 300, 300, mPaint);

        mPaint.setColor(Color.BLUE);
        RectF rect = new RectF(100, 400, 300, 600);
        canvas.drawRoundRect(rect, 50, 50, mPaint);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setColor(Color.RED);
        canvas.drawCircle(200, 200, 100, mPaint);

        mPaint.setColor(Color.YELLOW);

        RectF rect = new RectF(100, 400, 300, 700);
        canvas.drawOval(rect, mPaint);
    }

    private void drawArc(Canvas canvas) {
        mPaint.setColor(Color.RED);
        RectF rect = new RectF(100, 100, 300, 300);
        canvas.drawArc(rect, 45, 90, true, mPaint);

        rect = new RectF(100, 400, 300, 600);
        canvas.drawArc(rect, -45, -90, false, mPaint);
    }

    private void drawLineAndPoints(Canvas canvas) {
        canvas.save();

        canvas.drawColor(Color.TRANSPARENT);

        canvas.translate(100, 100);
        canvas.rotate(45, 100, 100);

        mPaint.setAntiAlias(true);

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(3);
        canvas.drawLines(points, mPaint);

        mPaint.setColor(Color.BLUE);

        mPaint.setStrokeWidth(5);
        canvas.drawPoints(points, mPaint);

        canvas.restore();
    }
}
