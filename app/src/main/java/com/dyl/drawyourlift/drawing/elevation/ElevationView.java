package com.dyl.drawyourlift.drawing.elevation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.dyl.drawyourlift.data.model.LiftProject;
import com.dyl.drawyourlift.data.repository.ProjectRepository;

public class ElevationView extends View {

    private Paint shaftPaint, floorPaint, textPaint, cabinPaint;

    // scale: 1 mm = 0.2 px (adjust later)
    private static final float SCALE = 0.2f;

    public ElevationView(Context context) {
        super(context);
        init();
    }

    private void init() {
        shaftPaint = new Paint();
        shaftPaint.setColor(Color.BLACK);
        shaftPaint.setStyle(Paint.Style.STROKE);
        shaftPaint.setStrokeWidth(4);

        floorPaint = new Paint();
        floorPaint.setColor(Color.DKGRAY);
        floorPaint.setStrokeWidth(3);

        cabinPaint = new Paint();
        cabinPaint.setColor(Color.LTGRAY);
        cabinPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(28);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        LiftProject p = ProjectRepository.getInstance().getProject();

        int shaftWidthPx = mmToPx(p.shaftWidth);
        int pitPx = mmToPx(p.pitDepth);
        int overheadPx = mmToPx(p.overheadHeight);
        int floorHeightPx = mmToPx(p.floorHeight);
        int floors = p.numberOfFloors;

        int travelPx = (floors - 1) * floorHeightPx;
        int totalHeightPx = pitPx + travelPx + overheadPx;

        int startX = 150;
        int startY = 50;

        // Shaft outline
        canvas.drawRect(
                startX,
                startY,
                startX + shaftWidthPx,
                startY + totalHeightPx,
                shaftPaint
        );

        int currentY = startY + overheadPx;

        // Draw floors
        for (int i = floors; i >= 1; i--) {

            // Floor line
            canvas.drawLine(
                    startX,
                    currentY,
                    startX + shaftWidthPx,
                    currentY,
                    floorPaint
            );

            // Floor label
            String label = (i == 1) ? "G" : "F" + (i - 1);
            canvas.drawText(
                    label,
                    startX - 60,
                    currentY + 10,
                    textPaint
            );

            currentY += floorHeightPx;
        }

        // Cabin (simple rectangle for now)
        int cabinWidthPx = (int) (shaftWidthPx * 0.7);
        int cabinX = startX + (shaftWidthPx - cabinWidthPx) / 2;
        int cabinY = startY + overheadPx + travelPx - floorHeightPx;

        canvas.drawRect(
                cabinX,
                cabinY,
                cabinX + cabinWidthPx,
                cabinY + floorHeightPx,
                cabinPaint
        );
    }

    private int mmToPx(int mm) {
        return (int) (mm * SCALE);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        LiftProject p = ProjectRepository.getInstance().getProject();

        int pitPx = mmToPx(p.pitDepth);
        int overheadPx = mmToPx(p.overheadHeight);
        int floorHeightPx = mmToPx(p.floorHeight);
        int floors = p.numberOfFloors;

        int travelPx = (floors - 1) * floorHeightPx;
        int totalHeightPx = pitPx + travelPx + overheadPx;

        int desiredWidth = mmToPx(p.shaftWidth) + 300; // margin
        int desiredHeight = totalHeightPx + 200;       // margin

        setMeasuredDimension(desiredWidth, desiredHeight);
    }
}
