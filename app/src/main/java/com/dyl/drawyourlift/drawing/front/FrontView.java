package com.dyl.drawyourlift.drawing.front;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.dyl.drawyourlift.data.model.LiftProject;
import com.dyl.drawyourlift.data.repository.ProjectRepository;

public class FrontView extends View {

    private Paint shaftPaint, cabinPaint, doorPaint, textPaint;

    private static final float SCALE = 0.2f;

    public FrontView(Context context) {
        super(context);
        init();
    }

    private void init() {

        shaftPaint = new Paint();
        shaftPaint.setColor(Color.BLACK);
        shaftPaint.setStyle(Paint.Style.STROKE);
        shaftPaint.setStrokeWidth(4);

        cabinPaint = new Paint();
        cabinPaint.setColor(Color.LTGRAY);
        cabinPaint.setStyle(Paint.Style.FILL);

        doorPaint = new Paint();
        doorPaint.setColor(Color.WHITE);
        doorPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(26);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        LiftProject p = ProjectRepository.getInstance().getProject();

        int shaftWidthPx = mmToPx(p.shaftWidth);
        int cabinWidthPx = (int) (shaftWidthPx * 0.7);
        int doorWidthPx = mmToPx(p.clearOpening);

        int viewHeight = mmToPx(p.floorHeight) + 200;

        int startX = 100;
        int startY = 50;

        // Shaft
        canvas.drawRect(
                startX,
                startY,
                startX + shaftWidthPx,
                startY + viewHeight,
                shaftPaint
        );

        // Cabin
        int cabinX = startX + (shaftWidthPx - cabinWidthPx) / 2;

        canvas.drawRect(
                cabinX,
                startY,
                cabinX + cabinWidthPx,
                startY + viewHeight,
                cabinPaint
        );

        // Door position logic
        int doorX;
        if (p.openingSide.equalsIgnoreCase("Left Opening")) {
            doorX = cabinX;
        } else if (p.openingSide.equalsIgnoreCase("Right Opening")) {
            doorX = cabinX + cabinWidthPx - doorWidthPx;
        } else { // Center Opening
            doorX = cabinX + (cabinWidthPx - doorWidthPx) / 2;
        }

        int doorY = startY + mmToPx(150);

        // Door opening
        canvas.drawRect(
                doorX,
                doorY,
                doorX + doorWidthPx,
                startY + viewHeight,
                doorPaint
        );

        // Door outline
        canvas.drawRect(
                doorX,
                doorY,
                doorX + doorWidthPx,
                startY + viewHeight,
                shaftPaint
        );

        // Label
        canvas.drawText(
                "Clear Opening: " + p.clearOpening + " mm",
                startX,
                startY + viewHeight + 40,
                textPaint
        );
    }

    private int mmToPx(int mm) {
        return (int) (mm * SCALE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        LiftProject p = ProjectRepository.getInstance().getProject();

        int desiredWidth = mmToPx(p.shaftWidth) + 200;
        int desiredHeight = mmToPx(p.floorHeight) + 300;

        setMeasuredDimension(desiredWidth, desiredHeight);
    }
}
