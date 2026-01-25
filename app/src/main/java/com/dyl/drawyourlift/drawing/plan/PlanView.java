package com.dyl.drawyourlift.drawing.plan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.dyl.drawyourlift.data.model.LiftProject;
import com.dyl.drawyourlift.data.repository.ProjectRepository;

public class PlanView extends View {

    private Paint shaftPaint, cabinPaint, counterPaint, doorPaint, textPaint;
    private static final float SCALE = 0.2f;
    private Paint dimPaint;
    private Paint dimTextPaint;


    public PlanView(Context context) {
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

        counterPaint = new Paint();
        counterPaint.setColor(Color.DKGRAY);
        counterPaint.setStyle(Paint.Style.FILL);

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
        int shaftDepthPx = mmToPx(p.shaftDepth);

        int cabinWidthPx = (int) (shaftWidthPx * 0.7);
        int cabinDepthPx = (int) (shaftDepthPx * 0.7);

        int startX = 100;
        int startY = 50;

        // Shaft outline
        canvas.drawRect(
                startX,
                startY,
                startX + shaftWidthPx,
                startY + shaftDepthPx,
                shaftPaint
        );
        // Shaft width dimension
        drawHorizontalDimension(
                canvas,
                startX,
                startX + shaftWidthPx,
                startY + shaftDepthPx + 80,
                "Shaft Width: " + p.shaftWidth + " mm"
        );
        // Shaft depth dimension
        drawVerticalDimension(
                canvas,
                startY,
                startY + shaftDepthPx,
                startX + shaftWidthPx + 40,
                "Shaft Depth: " + p.shaftDepth + " mm"
        );



        // Cabin footprint
        int cabinX = startX + (shaftWidthPx - cabinWidthPx) / 2;
        int cabinY = startY + (shaftDepthPx - cabinDepthPx) / 2;

        canvas.drawRect(
                cabinX,
                cabinY,
                cabinX + cabinWidthPx,
                cabinY + cabinDepthPx,
                cabinPaint
        );

        // Counterweight footprint
        int counterSize = mmToPx(p.counterDbgSize);

        int counterX, counterY;
        if (p.counterFrameSide.equalsIgnoreCase("Left")) {
            counterX = startX;
            counterY = cabinY;
        } else if (p.counterFrameSide.equalsIgnoreCase("Right")) {
            counterX = startX + shaftWidthPx - counterSize;
            counterY = cabinY;
        } else { // Back
            counterX = cabinX;
            counterY = startY;
        }

        canvas.drawRect(
                counterX,
                counterY,
                counterX + counterSize,
                counterY + counterSize,
                counterPaint
        );

        // Door opening (front side)
        int doorWidthPx = mmToPx(p.clearOpening);
        int doorX = cabinX + (cabinWidthPx - doorWidthPx) / 2;
        int doorY = startY + shaftDepthPx - mmToPx(80);

        canvas.drawRect(
                doorX,
                doorY,
                doorX + doorWidthPx,
                startY + shaftDepthPx,
                doorPaint
        );

        canvas.drawRect(
                doorX,
                doorY,
                doorX + doorWidthPx,
                startY + shaftDepthPx,
                shaftPaint
        );

        // Label
        canvas.drawText(
                "PLAN VIEW",
                startX,
                startY + shaftDepthPx + 40,
                textPaint
        );
    }

    private int mmToPx(int mm) {
        return (int) (mm * SCALE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        LiftProject p = ProjectRepository.getInstance().getProject();

        int desiredWidth = mmToPx(p.shaftWidth) + 300;
        int desiredHeight = mmToPx(p.shaftDepth) + 350;

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
        canvas.drawText(text, (x1 + x2)/2 - 120, y + 40, dimTextPaint);
    }
    private void drawVerticalDimension(
            Canvas canvas,
            int y1, int y2,
            int x,
            String text
    ) {
        canvas.drawLine(x - 20, y1, x + 20, y1, dimPaint);
        canvas.drawLine(x - 20, y2, x + 20, y2, dimPaint);

        canvas.drawLine(x, y1, x, y2, dimPaint);

        canvas.drawLine(x, y1, x - 10, y1 + 10, dimPaint);
        canvas.drawLine(x, y1, x + 10, y1 + 10, dimPaint);

        canvas.drawLine(x, y2, x - 10, y2 - 10, dimPaint);
        canvas.drawLine(x, y2, x + 10, y2 - 10, dimPaint);

        canvas.save();
        canvas.rotate(-90, x + 20, (y1 + y2) / 2);
        canvas.drawText(text, x - 60, (y1 + y2) / 2 + 25 , dimTextPaint);
        canvas.restore();
    }


}
