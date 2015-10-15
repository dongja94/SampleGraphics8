package com.example.dongja94.samplegraphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by dongja94 on 2015-10-15.
 */
public class MyView extends View {
    public MyView(Context context) {
        super(context);
        init();
    }

    Paint mPaint;
    private void init() {
        mPaint = new Paint();
        makeLinePoint();
        makePath();
        makeBitmap();
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPathEffect(canvas);
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
