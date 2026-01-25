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
    private Paint railPaint;
    private Paint counterPaint;
    private Paint dimPaint;
    private Paint dimTextPaint;



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
        railPaint = new Paint();
        railPaint.setColor(Color.GRAY);
        railPaint.setStrokeWidth(4);

        counterPaint = new Paint();
        counterPaint.setColor(Color.DKGRAY);
        counterPaint.setStyle(Paint.Style.FILL);

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
        // Cabin guide rails
        int railOffset = mmToPx(100);

        canvas.drawLine(
                startX + railOffset,
                startY,
                startX + railOffset,
                startY + totalHeightPx,
                railPaint
        );

        canvas.drawLine(
                startX + shaftWidthPx - railOffset,
                startY,
                startX + shaftWidthPx - railOffset,
                startY + totalHeightPx,
                railPaint
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
        drawVerticalDimension(
                canvas,
                startY + overheadPx,
                startY + overheadPx + floorHeightPx,
                startX + shaftWidthPx + 60,
                "Floor Height: " + p.floorHeight + " mm"
        );


        // ================= CABIN GEOMETRY =================
        int cabinWidthPx = (int) (shaftWidthPx * 0.7);
        int cabinHeightPx = floorHeightPx;

        int cabinX = startX + (shaftWidthPx - cabinWidthPx) / 2;
        int cabinY = startY + overheadPx + travelPx - floorHeightPx;

// ================= CABIN BODY =================
        canvas.drawRect(
                cabinX,
                cabinY,
                cabinX + cabinWidthPx,
                cabinY + cabinHeightPx,
                cabinPaint
        );

// ================= DOOR OPENING =================
        int doorWidthPx = mmToPx(p.clearOpening);
        int doorX = cabinX + (cabinWidthPx - doorWidthPx) / 2;
        int doorY = cabinY + mmToPx(200);

        Paint doorPaint = new Paint();
        doorPaint.setColor(Color.WHITE);
        doorPaint.setStyle(Paint.Style.FILL);

        canvas.drawRect(
                doorX,
                doorY,
                doorX + doorWidthPx,
                cabinY + cabinHeightPx,
                doorPaint
        );

// outline
        canvas.drawRect(
                doorX,
                doorY,
                doorX + doorWidthPx,
                cabinY + cabinHeightPx,
                shaftPaint
        );



        // Counterweight dimensions
        int counterWidth = mmToPx(p.counterDbgSize);
        int counterHeight = mmToPx(1200); // typical counter block height

        int counterX;
        if (p.counterFrameSide.equalsIgnoreCase("Left")) {
            counterX = startX + railOffset;
        } else if (p.counterFrameSide.equalsIgnoreCase("Right")) {
            counterX = startX + shaftWidthPx - railOffset - counterWidth;
        } else { // Back
            counterX = startX + (shaftWidthPx - counterWidth) / 2;
        }

        int counterY = startY + overheadPx + mmToPx(500);

        canvas.drawRect(
                counterX,
                counterY,
                counterX + counterWidth,
                counterY + counterHeight,
                counterPaint
        );
        // Counterweight guide rails
        canvas.drawLine(
                counterX,
                startY,
                counterX,
                startY + totalHeightPx,
                railPaint
        );

        canvas.drawLine(
                counterX + counterWidth,
                startY,
                counterX + counterWidth,
                startY + totalHeightPx,
                railPaint
        );

        //pit depth
        drawVerticalDimension(
                canvas,
                startY + totalHeightPx - pitPx,
                startY + totalHeightPx,
                startX - 80,
                "Pit: " + p.pitDepth + " mm"
        );
        //over head
        drawVerticalDimension(
                canvas,
                startY,
                startY + overheadPx,
                startX - 80,
                "Overhead: " + p.overheadHeight + " mm"
        );


        // ===== SHAFT WIDTH DIMENSION =====
        drawHorizontalDimension(
                canvas,
                startX,
                startX + shaftWidthPx,
                startY + totalHeightPx + 80,
                "Shaft Width: " + p.shaftWidth + " mm"
        );
        // ===== CLEAR OPENING DIMENSION =====
        drawHorizontalDimension(
                canvas,
                doorX,
                doorX + doorWidthPx,
                cabinY - 40,
                "Clear Opening: " + p.clearOpening + " mm"
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

        // Text (move higher to avoid overlap)
        canvas.drawText(
                text,
                (x1 + x2) / 2f - dimTextPaint.measureText(text) / 2f,
                y - 30,
                dimTextPaint
        );

    }
    private void drawVerticalDimension(
            Canvas canvas,
            int y1, int y2,
            int x,
            String text
    ) {
        // Extension lines
        canvas.drawLine(x - 20, y1, x + 20, y1, dimPaint);
        canvas.drawLine(x - 20, y2, x + 20, y2, dimPaint);

        // Dimension line
        canvas.drawLine(x, y1, x, y2, dimPaint);

        // Arrowheads
        canvas.drawLine(x, y1, x - 10, y1 + 10, dimPaint);
        canvas.drawLine(x, y1, x + 10, y1 + 10, dimPaint);

        canvas.drawLine(x, y2, x - 10, y2 - 10, dimPaint);
        canvas.drawLine(x, y2, x + 10, y2 - 10, dimPaint);

        // Rotate text for vertical reading
        canvas.save();
        canvas.rotate(-90, x - 25, (y1 + y2) / 2f);
        canvas.drawText(text, x - 25, (y1 + y2) / 2f, dimTextPaint);
        canvas.restore();
    }



}

