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
    private Paint dimPaint;
    private Paint dimTextPaint;


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

        dimPaint = new Paint();
        dimPaint.setColor(Color.BLACK);
        dimPaint.setStrokeWidth(3);

        dimTextPaint = new Paint();
        dimTextPaint.setColor(Color.BLACK);
        dimTextPaint.setTextSize(24);
        dimTextPaint.setAntiAlias(true);

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
        // Shaft width dimension
        drawHorizontalDimension(
                canvas,
                startX,
                startX + shaftWidthPx,
                startY + viewHeight + 80,
                "Shaft Width: " + p.shaftWidth + " mm"
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
        drawHorizontalDimension(
                canvas,
                doorX,
                doorX + doorWidthPx,
                startY + viewHeight + 130,
                "Clear Opening: " + p.clearOpening + " mm"
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

    private void drawHorizontalDimension(
            Canvas canvas,
            int x1, int x2,
            int y,
            String text
    ) {
        // Extension lines
        canvas.drawLine(x1, y - 20, x1, y + 20, dimPaint);
        canvas.drawLine(x2, y - 20, x2, y + 20, dimPaint);

        // Dimension line
        canvas.drawLine(x1, y, x2, y, dimPaint);

        // Arrowheads
        canvas.drawLine(x1, y, x1 + 10, y - 10, dimPaint);
        canvas.drawLine(x1, y, x1 + 10, y + 10, dimPaint);

        canvas.drawLine(x2, y, x2 - 10, y - 10, dimPaint);
        canvas.drawLine(x2, y, x2 - 10, y + 10, dimPaint);

        // Text
        canvas.drawText(text, (x1 + x2) / 2 - 40, y - 10, dimTextPaint);
    }

}
