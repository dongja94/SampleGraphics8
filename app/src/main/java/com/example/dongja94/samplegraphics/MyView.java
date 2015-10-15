package com.example.dongja94.samplegraphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

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
